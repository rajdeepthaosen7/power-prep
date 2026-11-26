"""Golden test for the backend refactor.
Fresh copy of the snapshot DB -> start the given jar -> run a fixed script of API calls (reads and writes)
-> save normalised responses as JSON lines. Run once with the old jar and once with the new one, then diff.
usage: python3 golden.py <jar> <out.jsonl>"""
import base64, hashlib, hmac, json, os, re, subprocess, sys, time, urllib.request, urllib.error

JAR, OUT = sys.argv[1], sys.argv[2]
SECRET = "local-dev-secret-0123456789-abcdefghij"
PORT = 8081
BASE = f"http://localhost:{PORT}"
PSQL = ["psql", "-h", "/tmp/pg18", "-U", "postgres", "-q", "-At"]

def sh(sql, db="postgres"):
    return subprocess.run(PSQL + ["-d", db, "-c", sql], check=True, capture_output=True, text=True).stdout.strip()

def token(uid):
    b = lambda d: base64.urlsafe_b64encode(json.dumps(d, separators=(",", ":")).encode()).rstrip(b"=")
    now = int(time.time())
    msg = b({"alg": "HS256"}) + b"." + b({"iss": "power-prep", "sub": str(uid), "iat": now, "exp": now + 3600})
    key = hashlib.sha256(SECRET.encode()).digest()
    sig = base64.urlsafe_b64encode(hmac.new(key, msg, hashlib.sha256).digest()).rstrip(b"=")
    return (msg + b"." + sig).decode()

ADMIN, PENDING = token(4), token(5)

# ---- fresh database + server
subprocess.run(["pkill", "-f", f"server.port={PORT}"])
time.sleep(1)
sh("DROP DATABASE IF EXISTS powerprep_golden")
sh("CREATE DATABASE powerprep_golden TEMPLATE powerprep_snap")
env = dict(os.environ, DATABASE_URL="jdbc:postgresql://localhost:5432/powerprep_golden", DB_USER="postgres", DB_PASSWORD="x",
           GOOGLE_CLIENT_ID="test-client", ADMIN_EMAIL="admin@example.com", JWT_SECRET=SECRET)
log = open(OUT + ".log", "w")
srv = subprocess.Popen(["java", "-jar", JAR, f"--server.port={PORT}", "--app.auth.google-jwk-set-uri=http://localhost:9999/certs"],
                       env=env, stdout=log, stderr=subprocess.STDOUT)
for _ in range(90):
    try:
        urllib.request.urlopen(BASE + "/actuator/health/liveness", timeout=2); break
    except Exception:
        time.sleep(1)

TS_KEY = re.compile(r"(At|^at|Now|_at)$")

def norm(v, key=""):
    if isinstance(v, dict):
        return {k: norm(x, k) for k, x in sorted(v.items()) if k not in ("qid", "timestamp", "trace", "path")}
    if isinstance(v, list):
        return [norm(x) for x in v]
    if TS_KEY.search(key) and v is not None:
        return "<ts:%s>" % type(v).__name__
    if isinstance(v, bool) or v is None or isinstance(v, str):
        return v
    if isinstance(v, (int, float)):
        return float(v)
    return v

out = open(OUT, "w")
def call(name, method, path, body=None, tok=ADMIN, keep=None):
    req = urllib.request.Request(BASE + path, method=method,
                                 data=None if body is None else json.dumps(body).encode(),
                                 headers={"Content-Type": "application/json", **({"Authorization": "Bearer " + tok} if tok else {})})
    try:
        r = urllib.request.urlopen(req); status, raw = r.status, r.read()
    except urllib.error.HTTPError as e:
        status, raw = e.code, e.read()
    try: data = json.loads(raw) if raw else None
    except Exception: data = raw.decode(errors="replace")
    if status >= 400 and isinstance(data, dict):
        data = {"hasMessage": bool(data.get("message"))}   # error bodies: only the status and whether a message is given
    out.write(json.dumps({"name": name, "status": status, "body": norm(data)}, sort_keys=True) + "\n")
    return status, data

# ---------------- reads
call("subjects", "GET", "/api/subjects")
call("subjects anon", "GET", "/api/subjects", tok=None)
for s in ("electric-circuits", "power-systems"):
    call("topics " + s, "GET", f"/api/subjects/{s}/topics")
    call("chapters " + s, "GET", f"/api/subjects/{s}/chapters")
    call("chapters anon " + s, "GET", f"/api/subjects/{s}/chapters", tok=None)
    call("tests " + s, "GET", f"/api/tests?subject={s}")
for tid in sh("SELECT id FROM topic WHERE subject_id = 1 ORDER BY id LIMIT 2", "powerprep_golden").split():
    call("topic questions " + tid, "GET", f"/api/topics/{tid}/questions")
call("pivot default", "GET", "/api/practice/pivot")
call("pivot topic x year", "GET", "/api/practice/pivot?rows=topic&cols=year&subject=electric-circuits")
call("pivot paper x nature", "GET", "/api/practice/pivot?rows=paper&cols=nature&kind=GATE&year=2010")
call("pivot bad dim", "GET", "/api/practice/pivot?rows=foo")
call("pivot same dims", "GET", "/api/practice/pivot?rows=exam&cols=exam")
call("practice questions", "GET", "/api/practice/questions?subject=power-systems&limit=15")
call("practice questions exam", "GET", "/api/practice/questions?exam=APGCL&limit=5")
chapters = {}
for s, c in (("electric-circuits", "circuit-elements"), ("electric-circuits", "basic-laws"), ("power-systems", "generation-economics")):
    st, chapters[c] = call(f"chapter {c}", "GET", f"/api/subjects/{s}/chapters/{c}")
    call(f"chapter anon {c}", "GET", f"/api/subjects/{s}/chapters/{c}", tok=None)
call("chapter missing", "GET", "/api/subjects/electric-circuits/chapters/nope")
call("syllabus", "GET", "/api/syllabus")
call("syllabus anon", "GET", "/api/syllabus", tok=None)
call("today", "GET", "/api/today")
call("mistakes", "GET", "/api/mistakes")
call("me", "GET", "/api/me")
call("me pending", "GET", "/api/me", tok=PENDING)
call("today pending", "GET", "/api/today", tok=PENDING)
call("today anon", "GET", "/api/today", tok=None)
call("admin users", "GET", "/api/admin/users")
call("admin users pending", "GET", "/api/admin/users?status=PENDING")
call("admin users bad", "GET", "/api/admin/users?status=FOO")
call("admin as pending", "GET", "/api/admin/users", tok=PENDING)
call("google blank", "POST", "/api/auth/google", {"credential": ""}, tok=None)
call("google bad", "POST", "/api/auth/google", {"credential": "abc"}, tok=None)

# ---------------- study attempts
qs = [q for t in chapters["basic-laws"]["topics"] for q in t["questions"]]
mcq = [q for q in qs if q["answerType"] == "MCQ" and q.get("attempt") is None]
nat = [q for q in qs if q["answerType"] == "NAT"]
q1 = mcq[0]["id"]
st, r = call("attempt mcq first", "POST", f"/api/questions/{q1}/attempts", {"chosen": ["a"], "timeMs": 1000})
right = r["correctLabels"]
wrong = [o["label"] for o in mcq[0]["options"] if o["label"] not in right][0]
call("attempt mcq wrong", "POST", f"/api/questions/{q1}/attempts", {"chosen": [wrong], "mode": "study"})
call("attempt mcq bad option", "POST", f"/api/questions/{q1}/attempts", {"chosen": ["Z"]})
call("attempt mcq none", "POST", f"/api/questions/{q1}/attempts", {"chosen": []})
call("attempt bad mode", "POST", f"/api/questions/{mcq[1]['id']}/attempts", {"chosen": ["B"], "mode": "weird"})
if nat:
    call("attempt nat no value", "POST", f"/api/questions/{nat[0]['id']}/attempts", {})
    call("attempt nat", "POST", f"/api/questions/{nat[0]['id']}/attempts", {"natValue": 1.5, "mode": "REVISION"})
pq = sh("SELECT q.id FROM question q WHERE q.origin = 'PRACTICE' AND q.status = 'READY' AND NOT EXISTS "
        "(SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = 4) ORDER BY q.id LIMIT 1", "powerprep_golden")
call("attempt unseen practice", "POST", f"/api/questions/{pq}/attempts", {"chosen": ["A"]})
call("attempt anon", "POST", f"/api/questions/{q1}/attempts", {"chosen": ["A"]}, tok=None)
call("attempt pending", "POST", f"/api/questions/{q1}/attempts", {"chosen": ["A"]}, tok=PENDING)
call("chapter after attempts", "GET", "/api/subjects/electric-circuits/chapters/basic-laws")

# ---------------- mistakes
call("reason set", "POST", f"/api/mistakes/{q1}/reason", {"reason": "concept"})
call("reason bad", "POST", f"/api/mistakes/{q1}/reason", {"reason": "lazy"})
call("mistakes after reason", "GET", "/api/mistakes")
call("reason clear", "POST", f"/api/mistakes/{q1}/reason", {"reason": ""})
never = sh("SELECT q.id FROM question q WHERE NOT EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = 4) ORDER BY id LIMIT 1", "powerprep_golden")
call("reason not a mistake", "POST", f"/api/mistakes/{never}/reason", {"reason": "GUESS"})

# ---------------- chapter progress
cid = chapters["basic-laws"]["id"]
for i, a in enumerate(["revised", "studied", "studied", "revised", "revised", "revised", "revised", "bogus"]):
    call(f"progress {i} {a}", "POST", f"/api/chapters/{cid}/progress", {"action": a})
call("today after progress", "GET", "/api/today")
call("progress reset", "POST", f"/api/chapters/{cid}/progress", {"action": "reset"})
call("progress missing chapter", "POST", "/api/chapters/999999/progress", {"action": "studied"})

# ---------------- tests
st, t = call("start test 4", "POST", "/api/tests/4/start")
call("start test 4 again", "POST", "/api/tests/4/start")
aid = t["attemptId"]
qq = t["questions"]
answers = [{"questionId": qq[0]["id"], "chosen": "a", "timeMs": 5000},
           {"questionId": qq[1]["id"], "chosen": "B", "timeMs": 7000, "marked": True},
           {"questionId": qq[2]["id"], "chosen": "  ", "timeMs": 100}]
call("result before submit", "GET", f"/api/test-attempts/{aid}")
call("submit bad option", "POST", f"/api/test-attempts/{aid}/submit", {"answers": [{"questionId": qq[0]["id"], "chosen": "Q"}], "timeMs": 1})
call("submit", "POST", f"/api/test-attempts/{aid}/submit", {"answers": answers, "timeMs": 123456})
call("submit again", "POST", f"/api/test-attempts/{aid}/submit", {"answers": [], "timeMs": 1})
call("result", "GET", f"/api/test-attempts/{aid}")
call("result other user", "GET", f"/api/test-attempts/{aid}", tok=token(6))
st, t2 = call("start test 3", "POST", "/api/tests/3/start")
call("submit empty", "POST", f"/api/test-attempts/{t2['attemptId']}/submit", {"timeMs": 10})
call("start missing test", "POST", "/api/tests/99999/start")
call("tests after", "GET", "/api/tests?subject=electric-circuits")
call("today after tests", "GET", "/api/today")
call("mistakes after tests", "GET", "/api/mistakes")
call("syllabus after", "GET", "/api/syllabus")

# ---------------- admin
call("approve self", "POST", "/api/admin/users/4/status", {"status": "DISABLED"})
call("approve bad", "POST", "/api/admin/users/5/status", {"status": "PENDING"})
call("approve", "POST", "/api/admin/users/5/status", {"status": "APPROVED"})
call("role self", "POST", "/api/admin/users/4/role", {"role": "USER"})
call("role bad", "POST", "/api/admin/users/5/role", {"role": "KING"})
call("role", "POST", "/api/admin/users/5/role", {"role": "ADMIN"})
call("approve missing", "POST", "/api/admin/users/9999/status", {"status": "APPROVED"})
call("today newly approved", "GET", "/api/today", tok=PENDING)

srv.terminate(); srv.wait()
print("wrote", OUT)
