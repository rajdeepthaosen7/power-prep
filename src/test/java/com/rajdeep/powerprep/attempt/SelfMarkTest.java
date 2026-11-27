package com.rajdeep.powerprep.attempt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.rajdeep.powerprep.common.error.BadRequestException;
import org.junit.jupiter.api.Test;

class SelfMarkTest {
    @Test
    void parses() {
        assertThat(SelfMark.parseOrNull(null)).isNull();
        assertThat(SelfMark.parseOrNull(" ")).isNull();
        assertThat(SelfMark.parseOrNull("guessed")).isEqualTo(SelfMark.GUESSED);
        assertThatThrownBy(() -> SelfMark.parseOrNull("maybe")).isInstanceOf(BadRequestException.class);
    }

    @Test
    void onlyKnewIsSure() {
        assertThat(SelfMark.KNEW.isUnsure()).isFalse();
        assertThat(SelfMark.GUESSED.isUnsure()).isTrue();
        assertThat(SelfMark.DONT_KNOW.isUnsure()).isTrue();
    }
}
