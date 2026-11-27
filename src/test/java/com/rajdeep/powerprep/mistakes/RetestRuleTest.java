package com.rajdeep.powerprep.mistakes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RetestRuleTest {
    @Test
    void staysOpenUntilTwoSureDays() {
        assertThat(RetestRule.isOpen(true, 0)).isTrue();
        assertThat(RetestRule.isOpen(true, 1)).isTrue();
        assertThat(RetestRule.isOpen(true, 2)).isFalse();
    }

    @Test
    void neverOpenWithoutABadAnswer() {
        assertThat(RetestRule.isOpen(false, 0)).isFalse();
    }
}
