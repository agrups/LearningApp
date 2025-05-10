package com.LearningApp.controller;

import com.LearningApp.entity.Meeting;
import com.LearningApp.entity.Person;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.LearningApp.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(Meeting.class)
public class MeetingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeetingService meetingService;

    @Test
    void testCreateMeeting() throws Exception {
        Meeting meeting = getMeetingEntity("Team Meeting", Category.CodeMonkey, Type.InPerson);
        ObjectMapper objectMapper = new ObjectMapper();
        String meetingJson = objectMapper.writeValueAsString(meeting);

        when(meetingService.createOrUpdateMeeting(any(Meeting.class))).thenReturn(meeting);

        mockMvc.perform(post("/api/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(meetingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Team Meeting"));

        verify(meetingService, times(1)).createOrUpdateMeeting(any(Meeting.class));
    }

    private static Meeting getMeetingEntity(String meetingName, Category codeMonkey, Type inPerson) {
        Meeting meeting = new Meeting();
        meeting.setName(meetingName);
        meeting.setDescription("Discuss project updates");
        Person person = new Person();
        person.setName("John");
        person.setSurname("Doe");
        meeting.setResponsiblePerson(person);
        meeting.setCategory(codeMonkey);
        meeting.setType(inPerson);
        meeting.setStartDate(new java.sql.Date(new Date().getTime()));
        meeting.setEndDate(new java.sql.Date(new Date().getTime()));
        meeting.setAttendees(new ArrayList<>());
        return meeting;
    }

    @Test
    void testDeleteMeeting() throws Exception {
        doNothing().when(meetingService).deleteMeeting(anyString(), any(Person.class));

        mockMvc.perform(delete("/api/meeting/{id}", "12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{" +
                                        "\"name\": \"John\"," +
                                        "\"surname\": \"Doe\"" +
                                        "}"
                        ))
                .andExpect(status().isOk());

        verify(meetingService, times(1)).deleteMeeting(eq("12345"), any(Person.class));
    }

}
