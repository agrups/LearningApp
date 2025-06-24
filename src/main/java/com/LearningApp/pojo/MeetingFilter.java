package com.LearningApp.pojo;

import com.LearningApp.dto.PersonDTO;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public final class MeetingFilter {
    private String description;
    private PersonDTO responsiblePerson;
    private Category category;
    private Type type;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
    private Integer minAttendees;
}
