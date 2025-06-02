//package com.LearningApp.service;
//
//import com.LearningApp.dto.MeetingDTO;
//import com.LearningApp.entity.Meeting;
//import com.LearningApp.entity.Person;
//import com.LearningApp.enums.Category;
//import com.LearningApp.enums.Type;
//import com.LearningApp.errors.MeetingErrorStatus;
//import com.LearningApp.errors.MeetingException;
//import com.LearningApp.mappers.MeetingMapper;
//import com.LearningApp.mappers.PersonMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class MeetingServiceTest {
//
//    private MeetingService meetingService;
//    private MeetingMapper meetingMapper;
//    private PersonMapper personMapper;
//
//    @BeforeEach
//    void setUp() {
//        meetingMapper = new MeetingMapper(); // Initialize the mapper
//        meetingService = new MeetingService();
//        personMapper = new PersonMapper();
//    }
//
//    @Test
//    void testCreateOrUpdateMeeting() {
//        Meeting meeting = getMeetingEntity("Team Meeting", Category.TeamBuilding, Type.Live);
//        MeetingDTO meetingDTO = meetingMapper.toDto(meeting);
//        MeetingDTO createdMeeting = meetingService.createOrUpdateMeeting(meetingDTO);
//
//        assertNotNull(createdMeeting.getId());
//        assertEquals("Team Meeting", createdMeeting.getName());
//    }

//    @Test
//    void testDeleteMeeting() throws MeetingException {
//        Meeting meeting = getMeetingEntity("Team Meeting Delete", Category.CodeMonkey, Type.InPerson);
//        Meeting createdMeeting = meetingService.createOrUpdateMeeting(meeting);
//
//        Person responsiblePerson = new Person();
//        responsiblePerson.setName("John");
//        responsiblePerson.setSurname("Doe");
//        assertNotNull(createdMeeting.getId());
//        assertDoesNotThrow(() -> meetingService.deleteMeeting(createdMeeting.getId(), responsiblePerson));
//    }
//
//    private static Meeting getMeetingEntity(String meetingName, Category codeMonkey, Type inPerson) {
//        Meeting meeting = new Meeting();
//        meeting.setName(meetingName);
//        meeting.setDescription("Discuss project updates");
//        Person person = new Person();
//        person.setName("John");
//        person.setSurname("Doe");
//        meeting.setResponsiblePerson(person);
//        meeting.setCategory(codeMonkey);
//        meeting.setType(inPerson);
//        meeting.setStartDate(new java.sql.Date(new Date().getTime()));
//        meeting.setEndDate(new java.sql.Date(new Date().getTime()));
//        meeting.setAttendees(new ArrayList<>());
//        return meeting;
//    }
//
//    @Test
//    void testDeleteMeetingThrowsExceptionWhenNotFound() {
//        Person responsiblePerson = new Person();
//        responsiblePerson.setName("John");
//        responsiblePerson.setSurname("Doe");
//        MeetingException exception = assertThrows(MeetingException.class, () -> {
//            meetingService.deleteMeeting("invalid-id", responsiblePerson);
//        });
//
//        assertEquals(MeetingErrorStatus.NOT_FOUND, exception.getStatus());
//        assertEquals("Meeting not found", exception.getMessage());
//    }
//
//    @Test
//    void testAddPersonToMeeting() throws MeetingException {
//        Meeting meeting = getMeetingEntity("Team Meeting Add Person", Category.CodeMonkey, Type.InPerson);
//        Meeting createdMeeting = meetingService.createOrUpdateMeeting(meeting);
//
//        Person attendee = new Person();
//        attendee.setName("Vardas");
//        attendee.setSurname("Pavarde");
//        String result = meetingService.addPersonToMeeting(createdMeeting.getId(), attendee);
//
//        assertTrue(result.contains("added successfully"));
//        assertEquals(2, meeting.getAttendees().size());
//        assertEquals(attendee.getName(), meeting.getAttendees().get(1).getName());
//        assertEquals(attendee.getSurname(), meeting.getAttendees().get(1).getSurname());
//    }
//}
