package com.LearningApp.pojo;

import com.LearningApp.dto.PersonDTO;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public final class MeetingFilter {
    private String description;
    private PersonDTO responsiblePerson;
    private Category category;
    private Type type;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Integer minAttendees;
}
