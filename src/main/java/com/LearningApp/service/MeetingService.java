package com.LearningApp.service;

import com.LearningApp.entity.Meeting;
import com.LearningApp.entity.Person;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.LearningApp.errors.MeetingErrorStatus;
import com.LearningApp.errors.MeetingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {

    private static HashMap<String, Meeting> meetingEntityHashMap = new HashMap<String, Meeting>();
    @Value("${app.file.path}")
    private String filePath;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void loadMeetingsFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                List<Meeting> meetings = objectMapper.readValue(file, new TypeReference<>() {
                });
                meetings.forEach(meeting -> meetingEntityHashMap.put(meeting.getId(), meeting));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMeetingsToFile() {
        try {
            objectMapper.writeValue(new File(filePath), meetingEntityHashMap.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Meeting createOrUpdateMeeting(Meeting meeting) {
        meeting.setId(UUID.randomUUID().toString());
        meetingEntityHashMap.put(meeting.getId(), meeting);
        saveMeetingsToFile();
        return meeting;
    }

    public void deleteMeeting(String id, Person person) throws MeetingException {
        Meeting meeting = meetingEntityHashMap.get(id);
        if (meeting == null) {
            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
        }

        if (!meeting.getResponsiblePerson().getName().equalsIgnoreCase(person.getName()) ||
                !meeting.getResponsiblePerson().getSurname().equalsIgnoreCase(person.getSurname())) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED, "Only the responsible person can delete the meeting");
        }
        meetingEntityHashMap.remove(id);
        saveMeetingsToFile();
    }

    public String addPersonToMeeting(String id, Person person) throws MeetingException{
        Meeting currentMeeting = meetingEntityHashMap.get(id);
        if (currentMeeting == null) {
            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
        }

        boolean isPersonInAnotherMeeting = meetingEntityHashMap.values().stream()
                .anyMatch(meeting -> meeting.getAttendees().stream()
                        .anyMatch(attendee -> attendee.getName() != null && attendee.getSurname() != null &&
                                attendee.getName().equalsIgnoreCase(person.getName()) &&
                                attendee.getSurname().equalsIgnoreCase(person.getSurname())));

        if (isPersonInAnotherMeeting) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED, "Person is already in a meeting which intersects with the one being added");
        }

        currentMeeting.getAttendees().add(person);
        saveMeetingsToFile();
        return person.getName() + " " + person.getSurname() + " added successfully at" + Date.from(java.time.Instant.now());
    }

    public String removePersonFromMeeting(String id, Person attendee) throws MeetingException {
        Meeting meeting = meetingEntityHashMap.get(id);
        if (meeting == null) {
            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
        }

        if (meeting.getResponsiblePerson().getName().equalsIgnoreCase(attendee.getName()) &&
                meeting.getResponsiblePerson().getSurname().equalsIgnoreCase(attendee.getSurname())) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED, "Cannot remove the responsible person from the meeting");
        }

        meeting.getAttendees().removeIf(person ->
                person.getName() != null && person.getSurname() != null &&
                        person.getName().equalsIgnoreCase(attendee.getName()) &&
                        person.getSurname().equalsIgnoreCase(attendee.getSurname())
        );

        saveMeetingsToFile();
        return attendee.getName() + " " + attendee.getSurname() + " removed successfully.";
    }


    public List<Meeting> getFilteredMeetings(String description, Person responsiblePerson, Category category, Type type, Date startDate, Date endDate, Integer minAttendees) {
        return meetingEntityHashMap.values().stream()
                .filter(meeting -> description == null || meeting.getDescription().toLowerCase().contains(description.toLowerCase()))
                .filter(meeting -> responsiblePerson == null || meeting.getResponsiblePerson().getName().equalsIgnoreCase(responsiblePerson.getName()) && meeting.getResponsiblePerson().getSurname().equalsIgnoreCase(responsiblePerson.getSurname()))
                .filter(meeting -> category == null || meeting.getCategory() == category)
                .filter(meeting -> type == null || meeting.getType() == type)
                .filter(meeting -> startDate == null || !meeting.getStartDate().before(startDate))
                .filter(meeting -> endDate == null || !meeting.getEndDate().after(endDate))
                .filter(meeting -> minAttendees == null || meeting.getAttendees().size() > minAttendees)
                .toList();
    }

    public Meeting getMeeting(String id) {
        return meetingEntityHashMap.get(id);
    }
}
