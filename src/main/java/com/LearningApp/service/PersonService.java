package com.LearningApp.service;

import com.LearningApp.dto.PersonCreationDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.entity.Person;
import com.LearningApp.mappers.PersonMapper;
import com.LearningApp.repository.PersonRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    public PersonDTO createPerson(PersonCreationDTO personCreationDTO) {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        personCreationDTO.setPassword(encoder.encode(personCreationDTO.getPassword()));

        var dbUser = getUserByEmail(personCreationDTO.getEmail());
        if (dbUser != null) {
            // user already exists, only update a few fields
            dbUser.setName(personCreationDTO.getName());
            dbUser.setSurname(personCreationDTO.getSurname());
            dbUser.setPassword(personCreationDTO.getPassword());
            personRepository.save(dbUser);
            return personMapper.toDTO(dbUser);
        }

        Person person = personMapper.toPerson(personCreationDTO);
        // Set role if provided, otherwise default to USER
        if (personCreationDTO.getRole() != null && !personCreationDTO.getRole().isBlank()) {
            person.setRole(personCreationDTO.getRole().toUpperCase());
        } else {
            person.setRole("USER");
        }
        personRepository.saveAndFlush(person);
        return personMapper.toDTO(person);
    }

    public Person getUserByEmail(String email) {
        var user = personRepository.findByEmail(email);
        return user.orElse(null);
    }

    public List<PersonDTO> getPersonList() {
        PersonMapper mapper = Mappers.getMapper(PersonMapper.class);
        return personRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}
