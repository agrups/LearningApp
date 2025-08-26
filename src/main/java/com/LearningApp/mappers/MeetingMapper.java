package com.LearningApp.mappers;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.entity.Meeting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface MeetingMapper {
    MeetingDTO toDTO(Meeting meeting);

    Meeting fromDTO(MeetingDTO meetingDTO);
}