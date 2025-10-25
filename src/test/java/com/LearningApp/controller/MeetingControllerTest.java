package com.LearningApp.controller;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.enums.Category;
import com.LearningApp.enums.Type;
import com.LearningApp.security.JwtAuthenticationFilter;
import com.LearningApp.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = MeetingController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class MeetingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeetingService meetingService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testCreateMeeting() throws Exception {
        MeetingDTO meetingDTO = getMeetingDTO("Team Meeting", Category.CODE_MONKEY, Type.IN_PERSON);
        String meetingJson = objectMapper.writeValueAsString(meetingDTO);
        when(meetingService.createOrUpdateMeeting(any(MeetingDTO.class))).thenReturn(meetingDTO);

        mockMvc.perform(post("/v1/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(meetingJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Team Meeting"));

        verify(meetingService, times(1)).createOrUpdateMeeting(any(MeetingDTO.class));
    }

    @Test
    void testDeleteMeeting() throws Exception {
        doNothing().when(meetingService).deleteMeeting(eq(12345L), any(PersonDTO.class));
        PersonDTO person = getPersonDTO();
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(delete("/v1/meetings/{id}", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Meeting deleted successfully"));

        verify(meetingService, times(1)).deleteMeeting(eq(12345L), any(PersonDTO.class));
    }

    @Test
    void testAddPersonToMeeting() throws Exception {
        doNothing().when(meetingService).addPersonToMeeting(eq(12345L), any(PersonDTO.class));
        PersonDTO person = getPersonDTO();
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(put("/v1/meetings/{id}/attendees", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Person added successfully"));

        verify(meetingService, times(1)).addPersonToMeeting(eq(12345L), any(PersonDTO.class));
    }

    @Test
    void testRemovePersonFromMeeting() throws Exception {
        when(meetingService.removePersonFromMeeting(eq(12345L), any(PersonDTO.class)))
                .thenReturn("Person removed successfully");
        PersonDTO person = getPersonDTO();
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(delete("/v1/meetings/{id}/attendees", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Person removed successfully"));

        verify(meetingService, times(1)).removePersonFromMeeting(eq(12345L), any(PersonDTO.class));
    }

    @Test
    void testGetMeetings() throws Exception {
        List<MeetingDTO> meetings = List.of(getMeetingDTO("Team Meeting", Category.CODE_MONKEY, Type.IN_PERSON));
        when(meetingService.getFilteredMeetings(any())).thenReturn(meetings);

        mockMvc.perform(get("/v1/meetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Team Meeting"));

        verify(meetingService, times(1)).getFilteredMeetings(any());
    }

    private static MeetingDTO getMeetingDTO(String meetingName, Category category, Type type) {
        MeetingDTO meeting = new MeetingDTO();
        meeting.setName(meetingName);
        meeting.setDescription("Discuss project updates");
        PersonDTO person = getPersonDTO();
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

    private static PersonDTO getPersonDTO() {
        PersonDTO person = new PersonDTO();
        person.setId(1L);
        person.setName("John");
        person.setSurname("Doe");
        person.setEmail("someemail@test.com");
        return person;
    }
}