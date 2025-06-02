package com.LearningApp.entity;

import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
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
    //@JoinColumn(name = "responsible_person_id", nullable = false)
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date endDate;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Person> attendees = new ArrayList<>(); // Use a mutable collection

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
