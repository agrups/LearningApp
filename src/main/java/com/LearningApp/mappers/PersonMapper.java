package com.LearningApp.mappers;

import com.LearningApp.dto.PersonCreationDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public PersonDTO toDto(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setName(person.getName());
        personDTO.setSurname(person.getSurname());
        personDTO.setEmail(person.getEmail());
        return personDTO;
    }
    public Person toPerson(PersonCreationDTO personCreationDTO){
        Person person = new Person();
        person.setName(personCreationDTO.getName());
        person.setSurname(personCreationDTO.getSurname());
        person.setPassword(personCreationDTO.getPassword());
        person.setEmail(personCreationDTO.getEmail());
        return person;
    }

    public Person fromDto(PersonDTO dto) {
        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setEmail(dto.getEmail());
        return person;
    }
}
