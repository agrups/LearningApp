package com.LearningApp.mappers;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.entity.Meeting;
import com.LearningApp.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeetingMapper {
    @Autowired
    private PersonMapper personMapper;

    public MeetingDTO toDto(Meeting meeting){
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(meeting.getId());
        meetingDTO.setName(meeting.getName());
        meetingDTO.setResponsiblePerson(personMapper.toDto(meeting.getResponsiblePerson()));
        meetingDTO.setDescription(meeting.getDescription());
        meetingDTO.setCategory(meeting.getCategory());
        meetingDTO.setType(meeting.getType());
        meetingDTO.setStartDate(meeting.getStartDate());
        meetingDTO.setEndDate(meeting.getEndDate());
        meetingDTO.setAttendees(meeting.getAttendees().stream().map(personMapper::toDto).toList()); //pasiaiskinti
        return meetingDTO;
    }

    public Meeting fromDto(MeetingDTO meetingDTO) {
        if (meetingDTO == null) {
            return null;
        }
        Meeting meeting = new Meeting();
        meeting.setName(meetingDTO.getName());
        meeting.setResponsiblePerson(personMapper.fromDto(meetingDTO.getResponsiblePerson()));
        meeting.setDescription(meetingDTO.getDescription());
        meeting.setCategory(meetingDTO.getCategory());
        meeting.setType(meetingDTO.getType());
        meeting.setStartDate(meetingDTO.getStartDate());
        meeting.setEndDate(meetingDTO.getEndDate());
        meeting.setAttendees(mapAttendeesFromDto(meetingDTO.getAttendees()));

        return meeting;
    }

    private List<Person> mapAttendeesFromDto(List<PersonDTO> attendees) {
        if (attendees == null) {
            return List.of();
        }
        return attendees.stream()
                .map(personMapper::fromDto)
                .collect(Collectors.toList());
    }
}