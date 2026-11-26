package com.rajdeep.powerprep.attempt;

import java.math.BigDecimal;
import java.time.Instant;

/** The user's latest attempt at a question, and how many times they have tried it. */
public record LastAttempt(String chosen, BigDecimal natValue, Boolean correct, Instant at, long tries) {}
