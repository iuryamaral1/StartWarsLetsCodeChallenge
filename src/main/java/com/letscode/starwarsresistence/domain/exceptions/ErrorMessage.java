package com.letscode.starwarsresistence.domain.exceptions;

import java.util.Date;

public class ErrorMessage {

    private String message;
    private Date timeOfOccurrence;

    public ErrorMessage(String message, Date timeOfOccurrence) {
        this.message = message;
        this.timeOfOccurrence = timeOfOccurrence;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeOfOccurrence() {
        return timeOfOccurrence;
    }

    public void setTimeOfOccurrence(Date timeOfOccurrence) {
        this.timeOfOccurrence = timeOfOccurrence;
    }
}
