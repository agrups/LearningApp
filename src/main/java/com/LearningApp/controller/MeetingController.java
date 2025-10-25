package com.LearningApp.controller;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.errors.MeetingException;
import com.LearningApp.pojo.MeetingFilter;
import com.LearningApp.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrUpdateMeeting(@Valid @RequestBody MeetingDTO meetingDTO) throws MeetingException {
        MeetingDTO createdMeeting = meetingService.createOrUpdateMeeting(meetingDTO);
        return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) throws MeetingException {
        meetingService.deleteMeeting(id, personDTO);
        return ResponseEntity.ok("Meeting deleted successfully");
    }

    @PutMapping("/{id}/attendees")
    public ResponseEntity<?> addPersonToMeeting(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) throws MeetingException {
        meetingService.addPersonToMeeting(id, personDTO);
        return ResponseEntity.ok("Person added successfully");
    }

    @DeleteMapping("/{id}/attendees")
    public String removePersonFromMeeting(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) throws MeetingException {
        return meetingService.removePersonFromMeeting(id, personDTO);
    }

    @GetMapping("")
    public List<MeetingDTO> getMeetings(MeetingFilter meetingFilter) {
        return meetingService.getFilteredMeetings(meetingFilter);
    }

}
