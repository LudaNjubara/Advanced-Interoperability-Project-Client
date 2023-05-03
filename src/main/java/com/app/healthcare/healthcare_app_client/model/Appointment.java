package com.app.healthcare.healthcare_app_client.model;

import java.time.LocalDate;


public class Appointment {
    private Long id;
    private String title;
    private String description;
    private LocalDate appointmentDate;
    private Facility facility;
    private Long facilityId;
    private Patient patient;
    private Long patientId;
    private Provider provider;
    private Long providerId;

    public Appointment() {
    }

    public Appointment(String title, String description, LocalDate date, Provider provider, Patient patient, Facility facility) {
        this.title = title;
        this.description = description;
        this.appointmentDate = date;
        this.provider = provider;
        this.patient = patient;
        this.facility = facility;
    }

    public Appointment(String title, String description, LocalDate date, Long providerId, Long patientId, Long facilityId) {
        this.title = title;
        this.description = description;
        this.appointmentDate = date;
        this.providerId = providerId;
        this.patientId = patientId;
        this.facilityId = facilityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", facility=" + facility +
                ", facilityId=" + facilityId +
                ", patient=" + patient +
                ", patientId=" + patientId +
                ", provider=" + provider +
                ", providerId=" + providerId +
                '}';
    }
}
