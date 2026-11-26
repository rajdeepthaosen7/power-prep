package com.rajdeep.powerprep.auth;

/** A verified sign-in from an identity provider (Google). */
public record IdentityToken(String subject, String email, String name, String picture) {}
