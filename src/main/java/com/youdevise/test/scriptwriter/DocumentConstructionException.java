package com.youdevise.test.scriptwriter;

public class DocumentConstructionException extends RuntimeException {
    public DocumentConstructionException(String message) {
        super(message);
    }

    public DocumentConstructionException(String message, Throwable cause) {
        super(message, cause);
    }
}
