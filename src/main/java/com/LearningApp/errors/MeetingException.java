package com.LearningApp.errors;

import lombok.Getter;

@Getter
public class MeetingException extends Exception {
    private final MeetingErrorStatus status;

    public MeetingException(MeetingErrorStatus status, String message) {
        super(message);
        this.status = status;
    }
}