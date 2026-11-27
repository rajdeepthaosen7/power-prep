package com.rajdeep.powerprep.pyq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.rajdeep.powerprep.common.error.BadRequestException;
import org.junit.jupiter.api.Test;

class PaperRefTest {
    @Test
    void plainYearIsOnePaper() {
        PaperRef p = PaperRef.of("GATE-2012", "14");
        assertThat(p.key()).isEqualTo("GATE-2012");
        assertThat(p.set()).isNull();
        assertThat(p.qPrefix()).isNull();
        assertThat(p.title()).isEqualTo("GATE EE 2012");
    }

    @Test
    void setInQuestionNumberMakesItsOwnPaper() {
        PaperRef p = PaperRef.of("GATE-2014", "S2-Q14");
        assertThat(p.key()).isEqualTo("GATE-2014-S2");
        assertThat(p.sourceCode()).isEqualTo("GATE-2014");
        assertThat(p.qPrefix()).isEqualTo("S2-Q");
        assertThat(p.title()).isEqualTo("GATE EE 2014, Set 2");
        assertThat(PaperRef.fromKey("GATE-2014-S2")).isEqualTo(p);
    }

    @Test
    void setInSourceCode() {
        PaperRef p = PaperRef.fromKey("GATE-2017_1");
        assertThat(p.sourceCode()).isEqualTo("GATE-2017_1");
        assertThat(p.year()).isEqualTo(2017);
        assertThat(p.set()).isEqualTo(1);
        assertThat(p.title()).isEqualTo("GATE EE 2017, Set 1");
    }

    @Test
    void questionNumbers() {
        assertThat(PaperRef.number("S3-Q41")).contains(41);
        assertThat(PaperRef.number("7")).contains(7);
        assertThat(PaperRef.number("~load-diversity-factor")).isEmpty();
    }

    @Test
    void rejectsJunk() {
        assertThatThrownBy(() -> PaperRef.fromKey("GATE-20x")).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> PaperRef.fromKey("x'; DROP TABLE")).isInstanceOf(BadRequestException.class);
    }
}
