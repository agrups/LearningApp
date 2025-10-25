package com.LearningApp.repository;

import com.LearningApp.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, JpaSpecificationExecutor<Meeting> {

    @Query("SELECT m FROM Meeting m JOIN m.attendees a WHERE a.email = :email AND " +
            "((m.startDate < :endDate) AND (m.endDate > :startDate))")
    List<Meeting> findOverlappingMeetingsByAttendee(@Param("email") String email,
                                                    @Param("startDate") java.time.LocalDateTime startDate,
                                                    @Param("endDate") java.time.LocalDateTime endDate);
}
