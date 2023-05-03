package com.app.healthcare.healthcare_app_client.model;

public class Patient {


    private Long id;
    private String firstName;
    private String lastName;
    private Facility facility;
    private Long facilityId;
    private Provider provider;
    private Long providerId;
    private String address;
    private String phoneNumber;
    private String dateOfBirth;
    private String oib;
    private String imageUrl;

    public Patient() {
    }

    public Patient(String firstName, String lastName, Long facilityId, Long providerId, String address, String phoneNumber, String dob, String oib, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.facilityId = facilityId;
        this.providerId = providerId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dob;
        this.oib = oib;
        this.imageUrl = imageURL;
    }

    public Patient(String firstName, String lastName, Facility facility, Provider provider, String address, String phoneNumber, String dob, String oib, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.facility = facility;
        this.provider = provider;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dob;
        this.oib = oib;
        this.imageUrl = imageURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", facility=" + facility +
                ", facilityId=" + facilityId +
                ", provider=" + provider +
                ", providerId=" + providerId +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", oib='" + oib + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
