package com.LearningApp.repository;

import com.LearningApp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person save(Person person);
    Optional<Person> findByEmail(String username);
}
