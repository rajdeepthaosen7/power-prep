package com.rajdeep.powerprep.mocktest;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TestScoreTest {
    @Test
    void apscMarkingIsPlusOneMinusQuarter() {
        TestScore s = TestScore.of(BigDecimal.ONE, new BigDecimal("0.25"), 30, 8, 12);
        assertThat(s.score()).isEqualByComparingTo("28");          // 30 - 8 x 0.25
        assertThat(s.maxScore()).isEqualByComparingTo("50");
    }

    @Test
    void accuracyIsRoundedPercentOfAttempted() {
        assertThat(TestScore.accuracyPct(2, 1)).isEqualByComparingTo("67");
        assertThat(TestScore.accuracyPct(0, 0)).isNull();
    }
}
