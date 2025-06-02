package com.LearningApp.controller;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonCreationDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.errors.MeetingException;
import com.LearningApp.pojo.MeetingFilter;
import com.LearningApp.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/meeting")
    public ResponseEntity<?> createOrUpdateMeeting(@Valid @RequestBody MeetingDTO meetingDTO) {
        try{
            MeetingDTO createdMeeting = meetingService.createOrUpdateMeeting(meetingDTO);
            return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
        } catch (MeetingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonCreationDTO personCreationDTO) {
        PersonDTO createdPerson = meetingService.createPerson(personCreationDTO);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    @DeleteMapping("/meeting/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) {
        try {
            meetingService.deleteMeeting(id, personDTO);
            return ResponseEntity.ok("Meeting deleted successfully");
        } catch (MeetingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/meeting/{id}/attendee")
    public ResponseEntity<?> addPersonToMeeting(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) {
        try {
            meetingService.addPersonToMeeting(id, personDTO);
            return ResponseEntity.ok("Person added successfully");
        } catch (MeetingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    @DeleteMapping("/meeting/{id}/attendee")
    public String removePersonFromMeeting(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) throws MeetingException {
        return meetingService.removePersonFromMeeting(id, personDTO);
    }

    @GetMapping("/meetings")
    public List<MeetingDTO> getMeetings(MeetingFilter meetingFilter) {
        return meetingService.getFilteredMeetings(meetingFilter);
    }

}
