package com.youdevise.test.scriptwriter;

public class PrintingException extends RuntimeException {
    public PrintingException(String message) {
        super(message);
    }

    public PrintingException(String message, Throwable cause) {
        super(message, cause);
    }
}
