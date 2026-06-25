package com.simonatb.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFeatureFlagRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be 100 characters or fewer")
    String name,

    @Size(max = 500, message = "Description must be 500 characters or fewer")
    String description,

    boolean enabled
) { }