package com.LearningApp.repository;

import com.LearningApp.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer>, JpaSpecificationExecutor<Meeting> {
    public Meeting findById(Long id);
    public void deleteById(Long id);
    public Meeting save(Meeting meeting);
    public List<Meeting> findAll();

    @Query("SELECT m FROM Meeting m JOIN m.attendees a " +
            "WHERE LOWER(a.name) = LOWER(:name) AND LOWER(a.surname) = LOWER(:surname) " +
            "AND m.startDate < :endDate AND m.endDate > :startDate")
    List<Meeting> findOverlappingMeetingsByAttendee(@Param("name") String name,
                                                    @Param("surname") String surname,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);
}
