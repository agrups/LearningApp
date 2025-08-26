package com.LearningApp.controller;

import com.LearningApp.dto.PersonCreationDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping("/registration")
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonCreationDTO personCreationDTO) {
        PersonDTO createdPerson = personService.createPerson(personCreationDTO);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    @GetMapping("")
    public List<PersonDTO> getPersons() {
        return personService.getPersonList();
    }
}