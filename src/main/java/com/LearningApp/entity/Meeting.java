package com.LearningApp.entity;

import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class Meeting {
    private String id;

    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Responsible person is required")
    private Person responsiblePerson;
    private String description;
    @NotNull(message = "Category is required")
    private Category category;
    @NotNull(message = "Type is required")
    private Type type;

    @NotNull(message = "Start date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date startDate;
    @NotNull(message = "End date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date endDate;

    @NotEmpty(message = "Attendees list cannot be empty")
    private List<Person> attendees;

    @JsonSetter("attendees")
    public void setAttendees(List<Person> attendees) {
        attendees.removeIf(person -> person == null || person.getName() == null || person.getSurname() == null);

        this.attendees = attendees;
        if (responsiblePerson != null ) {
            boolean isAlreadyInAttendees = this.attendees.stream()
                    .anyMatch(person -> person.getName() != null && person.getSurname() != null &&
                            person.getName().equalsIgnoreCase(responsiblePerson.getName()) &&
                            person.getSurname().equalsIgnoreCase(responsiblePerson.getSurname()));

            if (!isAlreadyInAttendees) {
                this.attendees.add(responsiblePerson);
            }
        }
    }
}
