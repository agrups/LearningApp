package com.LearningApp.dto;

import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MeetingDTO {
    private Long id;
    @NotBlank(message = "Meeting name is required")
    private String name;
    @NotNull(message = "Responsible person is required")
    private PersonDTO responsiblePerson;
    private String description;
    @NotNull(message = "Category is required")
    private Category category;
    @NotNull(message = "Type is required")
    private Type type;
    @NotNull(message = "Start date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @NotNull(message = "End date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    @NotEmpty(message = "Attendees list cannot be empty")
    private List<PersonDTO> attendees;
}
