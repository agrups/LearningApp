package com.LearningApp.controller;

import com.LearningApp.entity.Meeting;
import com.LearningApp.entity.Person;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.LearningApp.errors.MeetingException;
import com.LearningApp.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/meeting")
    public Meeting createOrUpdateMeeting(@Valid @RequestBody Meeting meeting) {
        return meetingService.createOrUpdateMeeting(meeting);
    }

    @DeleteMapping("/meeting/{id}")
    public void deleteMeeting(@PathVariable String id, @Valid @RequestBody Person person) throws MeetingException {
        meetingService.deleteMeeting(id, person);
    }

    @PutMapping("/meeting/{id}/attendee")
    public void addPersonToMeeting(@PathVariable String id, @Valid @RequestBody Person attendee) throws MeetingException {
        meetingService.addPersonToMeeting(id, attendee);
    }

    @DeleteMapping("/meeting/{id}/attendee")
    public String removePersonFromMeeting(@PathVariable String id, @Valid @RequestBody Person attendee) throws MeetingException {
        return meetingService.removePersonFromMeeting(id, attendee);
    }

    @GetMapping("/meetings")
    public List<Meeting> getMeetings(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String responsiblePersonName,
            @RequestParam(required = false) String responsiblePersonSurname,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer minAttendees) {


        Person person = null;
        if (responsiblePersonName != null && responsiblePersonSurname != null) {
            person = new Person();
            person.setName(responsiblePersonName);
            person.setSurname(responsiblePersonSurname);
        }

        return meetingService.getFilteredMeetings(description, person, category, type, startDate, endDate, minAttendees);
    }
}
