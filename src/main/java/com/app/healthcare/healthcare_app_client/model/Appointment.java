package com.app.healthcare.healthcare_app_client.model;

import java.time.LocalDate;


public class Appointment {
    private Long id;
    private LocalDate appointmentDate;
    private String description;
    private Facility facility;
    private Patient patient;
    private Provider provider;

    public Appointment() {
    }

    public Appointment(Long id, LocalDate appointmentDate, String description, Facility facility, Patient patient, Provider provider) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.description = description;
        this.facility = facility;
        this.patient = patient;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", appointmentDate=" + appointmentDate +
                ", description='" + description + '\'' +
                ", facility=" + facility +
                ", patient=" + patient +
                ", provider=" + provider +
                '}';
    }
}
