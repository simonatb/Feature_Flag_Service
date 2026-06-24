package com.simonatb.featureflag.dto;

public record CreateFeatureFlagRequest(String name, String description, boolean enabled) { }
