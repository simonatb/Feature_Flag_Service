package com.simonatb.featureflag.dto;

import jakarta.validation.constraints.Size;

public record UpdateFeatureFlagRequest(
    Boolean enabled,

    @Size(max = 500, message = "Description must be 500 characters or fewer")
    String description
) { }