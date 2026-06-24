package com.simonatb.featureflag.exception;

public class DuplicateFeatureFlagNameException extends RuntimeException {

    public DuplicateFeatureFlagNameException(String message) {
        super(message);
    }

    public DuplicateFeatureFlagNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
