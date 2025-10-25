package com.LearningApp.service;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.entity.Meeting;
import com.LearningApp.entity.Person;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.LearningApp.errors.MeetingErrorStatus;
import com.LearningApp.errors.MeetingException;
import com.LearningApp.mappers.MeetingMapper;
import com.LearningApp.repository.MeetingRepository;
import com.LearningApp.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MeetingServiceTest {

    private static final Long PERSON_ID = 1L;
    private static final String PERSON_NAME = "John";
    private static final String PERSON_SURNAME = "Doe";
    private static final String PERSON_EMAIL = "somemeail@test.com";
    private static final Long MEETING_ID = 1L;

    @Mock
    private MeetingRepository meetingRepositoryMock;
    @Mock
    private PersonRepository personRepositoryMock;
    @Mock
    private MeetingMapper meetingMapper;

    @InjectMocks
    private MeetingService meetingService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrUpdateMeeting() throws MeetingException {
        Meeting meeting = createMeetingEntity();
        MeetingDTO meetingDTO = createMeetingDTO();

        when(meetingMapper.toDTO(any(Meeting.class))).thenReturn(meetingDTO);
        when(meetingMapper.fromDTO(any(MeetingDTO.class))).thenReturn(meeting);
        when(meetingRepositoryMock.save(any(Meeting.class))).thenReturn(meeting);
        when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(meeting.getResponsiblePerson()));

        MeetingDTO createdMeeting = meetingService.createOrUpdateMeeting(meetingDTO);

        assertNotNull(createdMeeting.getId());
        assertEquals(meeting.getName(), createdMeeting.getName());
    }

    @Test
    void shouldDeleteMeeting() throws MeetingException {
        Meeting meeting = createMeetingEntity();
        MeetingDTO meetingDTO = createMeetingDTO();
        PersonDTO responsiblePerson = meetingDTO.getResponsiblePerson();

        when(meetingMapper.toDTO(meeting)).thenReturn(meetingDTO);
        when(meetingMapper.fromDTO(meetingDTO)).thenReturn(meeting);
        when(meetingRepositoryMock.save(any(Meeting.class))).thenReturn(meeting);
        when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(meeting.getResponsiblePerson()));
        when(meetingRepositoryMock.findById(MEETING_ID)).thenReturn(Optional.of(meeting));

        meetingService.createOrUpdateMeeting(meetingDTO);

        assertDoesNotThrow(() -> meetingService.deleteMeeting(MEETING_ID, responsiblePerson));
    }

    @Test
    void shouldThrowExceptionWhenMeetingNotFoundOnDelete() {
        PersonDTO responsiblePerson = createPersonDTO();

        when(meetingRepositoryMock.findById(999L)).thenReturn(Optional.empty());
        MeetingException exception = assertThrows(MeetingException.class, () -> meetingService.deleteMeeting(999L, responsiblePerson));

        assertEquals(MeetingErrorStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Meeting not found", exception.getMessage());
    }

    @Test
    void shouldAddPersonToMeeting() throws MeetingException {
        Meeting meeting = createMeetingEntity();
        MeetingDTO meetingDTO = createMeetingDTO();

        Person newAttendee = new Person();
        newAttendee.setId(2L);
        newAttendee.setName("Vardas");
        newAttendee.setSurname("Pavarde");

        when(meetingMapper.toDTO(any(Meeting.class))).thenReturn(meetingDTO);
        when(meetingMapper.fromDTO(any(MeetingDTO.class))).thenReturn(meeting);
        when(meetingRepositoryMock.save(any(Meeting.class))).thenReturn(meeting);
        when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(meeting.getResponsiblePerson()));
        when(personRepositoryMock.findById(2L)).thenReturn(Optional.of(newAttendee));
        when(meetingRepositoryMock.findById(MEETING_ID)).thenReturn(Optional.of(meeting));
        when(meetingRepositoryMock.findOverlappingMeetingsByAttendee(any(), any(), any())).thenReturn(new ArrayList<>());

        meetingService.createOrUpdateMeeting(meetingDTO);

        PersonDTO attendee = new PersonDTO();
        attendee.setId(2L);
        attendee.setName("Vardas");
        attendee.setSurname("Pavarde");

        assertDoesNotThrow(() -> meetingService.addPersonToMeeting(MEETING_ID, attendee));
    }

    private Meeting createMeetingEntity() {
        Meeting meeting = new Meeting();
        meeting.setId(MEETING_ID);
        meeting.setName("Team Meeting");
        meeting.setDescription("Discuss project updates");
        Person person = new Person();
        person.setId(PERSON_ID);
        person.setName(PERSON_NAME);
        person.setSurname(PERSON_SURNAME);
        person.setEmail(PERSON_EMAIL);
        meeting.setResponsiblePerson(person);
        meeting.setCategory(Category.TEAM_BUILDING);
        meeting.setType(Type.LIVE);
        meeting.setStartDate(java.time.LocalDateTime.now());
        meeting.setEndDate(java.time.LocalDateTime.now().plusHours(1));        List<Person> attendees = new ArrayList<>();
        attendees.add(person);
        meeting.setAttendees(attendees);
        return meeting;
    }

    private MeetingDTO createMeetingDTO() {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(MEETING_ID);
        meetingDTO.setName("Team Meeting");
        meetingDTO.setDescription("Discuss project updates");
        meetingDTO.setCategory(Category.TEAM_BUILDING);
        meetingDTO.setType(Type.LIVE);
        meetingDTO.setStartDate(java.time.LocalDateTime.now());
        meetingDTO.setEndDate(java.time.LocalDateTime.now().plusHours(1));
        PersonDTO responsiblePerson = createPersonDTO();
        meetingDTO.setResponsiblePerson(responsiblePerson);
        List<PersonDTO> attendees = new ArrayList<>();
        attendees.add(responsiblePerson);
        meetingDTO.setAttendees(attendees);
        return meetingDTO;
    }

    private PersonDTO createPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(PERSON_ID);
        personDTO.setName(PERSON_NAME);
        personDTO.setSurname(PERSON_SURNAME);
        personDTO.setEmail(PERSON_EMAIL);
        return personDTO;
    }
}