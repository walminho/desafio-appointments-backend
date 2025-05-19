package com.medical.appointments.repository;

import com.medical.appointments.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByDoctorIdAndStartTimeAfter(Long doctorId, LocalDateTime startTime);
}