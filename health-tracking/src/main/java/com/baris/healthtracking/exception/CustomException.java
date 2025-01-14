package com.baris.healthtracking.exception;

public class CustomException extends RuntimeException {
    private final String errorMessage;
    private final int errorCode;

    public CustomException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
