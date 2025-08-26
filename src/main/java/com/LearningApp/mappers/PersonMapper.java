package com.LearningApp.mappers;

import com.LearningApp.dto.PersonCreationDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonDTO toDTO(Person person);

    Person toPerson(PersonCreationDTO personCreationDTO);
}