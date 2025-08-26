package com.LearningApp.entity;

import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    @ManyToMany
    @JoinTable(
            name = "meeting_attendees",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "attendees_id")
    )
    private List<Person> attendees = new ArrayList<>();


    public void setAttendees(List<Person> attendees) {
        this.attendees.clear();
        this.attendees.addAll(attendees);

        if (responsiblePerson != null && !this.attendees.contains(responsiblePerson)) {
            this.attendees.add(responsiblePerson);
        }
    }
}
