package com.LearningApp.service;

import com.LearningApp.dto.MeetingDTO;
import com.LearningApp.dto.PersonCreationDTO;
import com.LearningApp.dto.PersonDTO;
import com.LearningApp.entity.Meeting;
import com.LearningApp.entity.Person;
import com.LearningApp.errors.MeetingErrorStatus;
import com.LearningApp.errors.MeetingException;
import com.LearningApp.mappers.MeetingMapper;
import com.LearningApp.mappers.PersonMapper;
import com.LearningApp.pojo.MeetingFilter;
import com.LearningApp.repository.MeetingRepository;
import com.LearningApp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MeetingService {
    @Autowired
    MeetingMapper meetingMapper;
    @Autowired
    PersonMapper personMapper;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    PersonRepository personRepository;

//    public MeetingDTO createOrUpdateMeeting(MeetingDTO meetingDto) {
////        Meeting meeting = meetingMapper.fromDto(meetingDto);
////        // meeting.setId(Long.valueOf(UUID.randomUUID().toString()));
////        meetingRepository.save(meeting);
////        return meetingMapper.toDto(meeting);
//
//        // Map MeetingDTO to Meeting entity
//        Meeting meeting = meetingMapper.fromDto(meetingDto);
//
//        // Fetch the responsible person from the database
//        Long personId = meetingDto.getResponsiblePerson().getId();
//        Person responsiblePerson = personRepository.findById(personId)
//                .orElseThrow(() -> new RuntimeException("Person with id " + personId + " not found"));
//
//        // Set the managed Person entity to the Meeting
//        meeting.setResponsiblePerson(responsiblePerson);
//
//        // Persist the Meeting entity
//        Meeting savedMeeting = meetingRepository.save(meeting);
//
//        // Map the saved Meeting entity back to MeetingDTO
//        return meetingMapper.toDto(savedMeeting);
//    }

public MeetingDTO createOrUpdateMeeting(MeetingDTO meetingDto) throws MeetingException {
    try {
        // Debug log
        System.out.println("Received MeetingDTO: " + meetingDto);

        // Map MeetingDTO to Meeting entity
        Meeting meeting = meetingMapper.fromDto(meetingDto);

        // Fetch the responsible person from the database
        Long personId = meetingDto.getResponsiblePerson().getId();
        System.out.println("Fetching responsible person with ID: " + personId);
        Person responsiblePerson = personRepository.findById(personId)
                .orElseThrow(() -> new MeetingException(MeetingErrorStatus.NOT_FOUND, "Person with id " + personId + " not found"));

        // Explicitly set the managed Person entity to the Meeting
        meeting.setResponsiblePerson(responsiblePerson);

        // Fetch and replace detached attendees with managed entities
        List<Person> managedAttendees = meetingDto.getAttendees().stream()
                .filter(attendee -> attendee.getId() != null)
                .map(attendee -> {
                    try {
                        return personRepository.findById(attendee.getId())
                                .orElseThrow(() -> new MeetingException(MeetingErrorStatus.NOT_FOUND, "Person with id " + attendee.getId() + " not found"));
                    } catch (MeetingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        meeting.setAttendees(managedAttendees);

        // Persist the Meeting entity
        Meeting savedMeeting = meetingRepository.save(meeting);

        // Map the saved Meeting entity back to MeetingDTO
        return meetingMapper.toDto(savedMeeting);
    } catch (Exception ex) {
        ex.printStackTrace();
        throw ex; // Re-throw to be handled by the global exception handler
    }
}

    @Transactional
    public void deleteMeeting(Long id, PersonDTO personDTO) throws MeetingException {
        Meeting meeting = meetingRepository.findById(id);
        if (meeting == null) {
            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
        }
        if (!meeting.getResponsiblePerson().getId().equals(personDTO.getId())) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED, "Only the responsible person can delete the meeting");
        }
        meetingRepository.deleteById(id);
    }

    @Transactional
    public void addPersonToMeeting(Long id, PersonDTO personDTO) throws MeetingException {
//        Meeting currentMeeting = meetingRepository.findById(id);
//        if (currentMeeting == null) {
//            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
//        }
//        boolean isPersonInAnotherMeeting = !meetingRepository
//                .findOverlappingMeetingsByAttendee(
//                        personDTO.getName(),
//                        personDTO.getSurname(),
//                        currentMeeting.getStartDate(),
//                        currentMeeting.getEndDate()
//                ).isEmpty();
//
//        if (isPersonInAnotherMeeting) {
//            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED,
//                    "Person is already in another meeting during the same time.");
//        }
//        Person person = personMapper.fromDto(personDTO);
//        currentMeeting.getAttendees().add(person);
//        meetingRepository.save(currentMeeting);



//        Meeting currentMeeting = meetingRepository.findById(id);
//        if (currentMeeting == null) {
//            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
//        }
//
//        // Check if the person is already in another meeting
//        boolean isPersonInAnotherMeeting = !meetingRepository
//                .findOverlappingMeetingsByAttendee(
//                        personDTO.getName(),
//                        personDTO.getSurname(),
//                        currentMeeting.getStartDate(),
//                        currentMeeting.getEndDate()
//                ).isEmpty();
//
//        if (isPersonInAnotherMeeting) {
//            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED,
//                    "Person is already in another meeting during the same time.");
//        }
//
//        // Add the person to the meeting\
//        System.out.println("haha");
//        Person person = personMapper.fromDto(personDTO);
//        currentMeeting.getAttendees().add(person);
//        meetingRepository.save(currentMeeting);

        // Fetch the meeting by ID
        Meeting currentMeeting = meetingRepository.findById(id);
        if (currentMeeting == null) {
            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
        }

        // Check if the person is already in another meeting during the same time
        boolean isPersonInAnotherMeeting = !meetingRepository
                .findOverlappingMeetingsByAttendee(
                        personDTO.getName(),
                        personDTO.getSurname(),
                        currentMeeting.getStartDate(),
                        currentMeeting.getEndDate()
                ).isEmpty();

        if (isPersonInAnotherMeeting) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED,
                    "Person is already in another meeting during the same time.");
        }

        // Fetch the existing Person entity from the database
        Person person = personRepository.findById(personDTO.getId())
                .orElseThrow(() -> new MeetingException(MeetingErrorStatus.NOT_FOUND, "Person not found"));

        // Check if the person is already in the current meeting
        if (currentMeeting.getAttendees().contains(person)) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED, "Person is already in this meeting.");
        }

        // Add the person to the meeting
        currentMeeting.getAttendees().add(person);

        // Save the meeting
        meetingRepository.save(currentMeeting);
    }

    public String removePersonFromMeeting(Long id, PersonDTO personDTO) throws MeetingException {
        Meeting meeting = meetingRepository.findById(id);
        if (meeting == null) {
            throw new MeetingException(MeetingErrorStatus.NOT_FOUND, "Meeting not found");
        }

        if (meeting.getResponsiblePerson().getName().equalsIgnoreCase(personDTO.getName()) &&
                meeting.getResponsiblePerson().getSurname().equalsIgnoreCase(personDTO.getSurname())) {
            throw new MeetingException(MeetingErrorStatus.METHOD_NOT_ALLOWED, "Cannot remove the responsible person from the meeting");
        }

        //prideti checka ar zmogus isviso yra tame meete

        meeting.getAttendees().removeIf(person ->
                person.getName() != null && person.getSurname() != null &&
                        person.getName().equalsIgnoreCase(personDTO.getName()) &&
                        person.getSurname().equalsIgnoreCase(personDTO.getSurname())
        );

        meetingRepository.save(meeting);
        return personDTO.getName() + " " + personDTO.getSurname() + " removed successfully.";
    }


    public List<MeetingDTO> getFilteredMeetings(MeetingFilter meetingFilter) {
//        return meetingEntityHashMap.values().stream()
//                .filter(meeting -> meetingFilter.getDescription() == null || meeting.getDescription().toLowerCase().contains(meetingFilter.getDescription().toLowerCase()))
//                .filter(meeting -> meetingFilter.getResponsiblePerson() == null ||
//                        (meeting.getResponsiblePerson().getName().equalsIgnoreCase(meetingFilter.getResponsiblePerson().getName()) &&
//                                        meeting.getResponsiblePerson().getSurname().equalsIgnoreCase(meetingFilter.getResponsiblePerson().getSurname())))
//                .filter(meeting -> meetingFilter.getCategory() == null || meeting.getCategory() == meetingFilter.getCategory())
//                .filter(meeting -> meetingFilter.getType() == null || meeting.getType() == meetingFilter.getType())
//                .filter(meeting -> meetingFilter.getStartDate() == null || !meeting.getStartDate().before(meetingFilter.getStartDate()))
//                .filter(meeting -> meetingFilter.getEndDate() == null || !meeting.getEndDate().after(meetingFilter.getEndDate()))
//                .filter(meeting -> meetingFilter.getMinAttendees() == null || meeting.getAttendees().size() > meetingFilter.getMinAttendees())
//                .map(meetingMapper::toDto)
//                .toList();

        Specification<Meeting> specification = Specification.where(null);

        if (meetingFilter.getDescription() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + meetingFilter.getDescription().toLowerCase() + "%"));
        }

        if (meetingFilter.getResponsiblePerson() != null) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                var join = root.join("responsiblePerson");
                return criteriaBuilder.and(
                        criteriaBuilder.equal(criteriaBuilder.lower(join.get("name")), meetingFilter.getResponsiblePerson().getName().toLowerCase()),
                        criteriaBuilder.equal(criteriaBuilder.lower(join.get("surname")), meetingFilter.getResponsiblePerson().getSurname().toLowerCase())
                );
            });
        }

        if (meetingFilter.getCategory() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category"), meetingFilter.getCategory()));
        }

        if (meetingFilter.getType() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), meetingFilter.getType()));
        }

        if (meetingFilter.getStartDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), meetingFilter.getStartDate()));
        }

        if (meetingFilter.getEndDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), meetingFilter.getEndDate()));
        }

        if (meetingFilter.getMinAttendees() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(criteriaBuilder.size(root.get("attendees")), meetingFilter.getMinAttendees()));
        }

        List<Meeting> meetings = meetingRepository.findAll(specification);
        return meetings.stream().map(meetingMapper::toDto).toList();
    }

    public Meeting getMeeting(Long id) {
        return meetingRepository.findById(id);
    }

    public PersonDTO createPerson(PersonCreationDTO personCreationDTO) {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        personCreationDTO.setPassword(encoder.encode(personCreationDTO.getPassword()));

        var db_user = getUserByEmail(personCreationDTO.getEmail());
        if(db_user != null) {
            // user already exists, only update a few fields
            db_user.setName(personCreationDTO.getName());
            db_user.setSurname(personCreationDTO.getSurname());
            db_user.setPassword(personCreationDTO.getPassword());
            personRepository.saveAndFlush(db_user);
            return personMapper.toDto(db_user);
        }

        Person person = personMapper.toPerson(personCreationDTO);
        personRepository.saveAndFlush(person);
        return personMapper.toDto(person);
    }

    public Person getUserByEmail(String email) {
        var o_user = personRepository.findByEmail(email);
        if(o_user.isPresent()) {
            return o_user.get();
        }
        return null;
    }
}
