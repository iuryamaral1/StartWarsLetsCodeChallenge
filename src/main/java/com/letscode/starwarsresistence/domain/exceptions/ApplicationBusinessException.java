package com.letscode.starwarsresistence.domain.exceptions;

public class ApplicationBusinessException extends Exception {

    private String message;

    public ApplicationBusinessException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
