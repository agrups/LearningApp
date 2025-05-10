package com.LearningApp.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MeetingErrorResponse {
    private HttpStatus status;
    private String message;
}
