import json,sys
old=[json.loads(l) for l in open('old.jsonl')]; new=[json.loads(l) for l in open('new.jsonl')]
def strip(v):
    if isinstance(v,dict): return {k:strip(x) for k,x in v.items() if x is not None}
    if isinstance(v,list): return [strip(x) for x in v]
    return v
bad=0; errfix=0
for o,n in zip(old,new):
    if o['status']!=n['status']: bad+=1; print('STATUS', o['name'], o['status'], n['status']); continue
    if o['status']>=400:
        if n['body']!={'hasMessage': True}: print('ERRBODY', o['name'], n['body'])
        errfix+= o['body']!=n['body']; continue
    if strip(o['body'])!=strip(n['body']): bad+=1; print('DIFF', o['name'])
print(len(old), len(new), 'real diffs', bad, '| errors that now carry a message:', errfix)
