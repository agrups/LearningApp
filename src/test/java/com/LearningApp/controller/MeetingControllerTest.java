package com.LearningApp.controller;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.LearningApp.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class MeetingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeetingService meetingService;

    @Test
    void testCreateMeeting() throws Exception {
        MeetingDTO meetingDTO = getMeetingDTO("Team Meeting", Category.CodeMonkey, Type.InPerson);
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        String meetingJson = objectMapper.writeValueAsString(meetingDTO);
        when(meetingService.createOrUpdateMeeting(any(MeetingDTO.class))).thenReturn(meetingDTO);

        mockMvc.perform(post("/api/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(meetingJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Team Meeting"));

        verify(meetingService, times(1)).createOrUpdateMeeting(any(MeetingDTO.class));
    }

    private static MeetingDTO getMeetingDTO(String meetingName, Category category, Type type) {
        MeetingDTO meeting = new MeetingDTO();
        meeting.setName(meetingName);
        meeting.setDescription("Discuss project updates");
        PersonDTO person = new PersonDTO();
        person.setId(1L);
        person.setName("John");
        person.setSurname("Doe");
        person.setEmail("somemeail@test.com");
        meeting.setResponsiblePerson(person);
        meeting.setCategory(category);
        meeting.setType(type);
        meeting.setStartDate(java.time.LocalDateTime.now());
        meeting.setEndDate(java.time.LocalDateTime.now().plusHours(1));

        List<PersonDTO> attendees = new ArrayList<>();
        attendees.add(person);
        meeting.setAttendees(attendees);
        return meeting;
    }

    @Test
    void testDeleteMeeting() throws Exception {
        doNothing().when(meetingService).deleteMeeting(eq(12345L), any(PersonDTO.class));

        String personJson = "{" +
                "\"id\": \"123\"," +
                "\"name\": \"John\"," +
                "\"surname\": \"Doe\"," +
                "\"email\": \"someemail@test.com\"" +
                "}";

        mockMvc.perform(delete("/api/meeting/{id}", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk());

        verify(meetingService, times(1)).deleteMeeting(eq(12345L), any(PersonDTO.class));
    }
}