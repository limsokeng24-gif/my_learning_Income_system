package com.school_management.overseas_language_centre.exception;

public class EncryptionException extends RuntimeException {

    public EncryptionException(String message, Throwable cause) {
        super(message, cause); // cause kept for server logs
    }
}
