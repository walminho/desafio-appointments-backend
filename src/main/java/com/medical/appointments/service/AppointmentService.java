package com.medical.appointments.service;

import com.medical.appointments.model.Appointment;
import com.medical.appointments.model.Availability;
import com.medical.appointments.repository.AppointmentRepository;
import com.medical.appointments.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Appointment scheduleAppointment(Appointment appointment) throws Exception {
        // Verifica se o horário está disponível
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTime(
                appointment.getDoctor().getId(), appointment.getAppointmentTime());

        if (!existingAppointments.isEmpty()) {
            throw new Exception("Horário já agendado!");
        }

        // Verifica se o horário está dentro de uma disponibilidade do médico
        List<Availability> availabilities = availabilityRepository.findByDoctorIdAndStartTimeAfter(
                appointment.getDoctor().getId(), appointment.getAppointmentTime().minusHours(1));

        boolean isAvailable = availabilities.stream().anyMatch(availability ->
                appointment.getAppointmentTime().isAfter(availability.getStartTime()) &&
                appointment.getAppointmentTime().isBefore(availability.getEndTime()));

        if (!isAvailable) {
            throw new Exception("Horário não disponível!");
        }

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
    }
}