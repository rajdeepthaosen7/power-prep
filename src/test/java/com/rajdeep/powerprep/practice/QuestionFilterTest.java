package com.rajdeep.powerprep.practice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.rajdeep.powerprep.common.error.BadRequestException;
import org.junit.jupiter.api.Test;

class QuestionFilterTest {
    @Test
    void onlyGivenFiltersBecomeConditionsAndEveryConditionHasItsParameter() {
        QuestionFilter f = new QuestionFilter("power-systems", null, null, 2019, null, null);
        assertThat(f.where()).isEqualTo(" AND s.slug = :subject AND so.year = :year");
        assertThat(f.params()).containsOnlyKeys("subject", "year");
    }

    @Test
    void unknownDimensionIsABadRequestNotSql() {
        assertThat(PivotDimension.parse("Year")).isEqualTo(PivotDimension.YEAR);
        assertThatThrownBy(() -> PivotDimension.parse("1; DROP TABLE question")).isInstanceOf(BadRequestException.class);
    }
}
