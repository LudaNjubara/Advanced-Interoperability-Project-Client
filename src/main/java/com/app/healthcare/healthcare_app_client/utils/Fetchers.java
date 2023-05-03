package com.app.healthcare.healthcare_app_client.utils;

import com.app.healthcare.healthcare_app_client.model.Appointment;
import com.app.healthcare.healthcare_app_client.model.Facility;
import com.app.healthcare.healthcare_app_client.model.Patient;
import com.app.healthcare.healthcare_app_client.model.Provider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.app.healthcare.healthcare_app_client.utils.Utils.setupRestTemplate;

public class Fetchers {

    // PROVIDER FETCHERS
    public static Provider[] fetchAllProviders() {
        Provider[] returnedProviders = null;
        String URL = "http://localhost:8081/api/providers";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Provider[]> responseEntity = restTemplate.getForEntity(URL, Provider[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedProviders = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedProviders;
    }

    public static Provider[] fetchAllProvidersByFacilityId(Long facilityId) {
        Provider[] returnedProviders = null;
        String URL = "http://localhost:8081/api/providers?facilityId=" + facilityId;

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Provider[]> responseEntity = restTemplate.getForEntity(URL, Provider[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedProviders = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedProviders;
    }

    // PATIENT FETCHERS
    public static Patient[] fetchAllPatientsByFacilityId(Long facilityId) {
        Patient[] returnedPatients = null;
        String URL = "http://localhost:8081/api/patients?facilityId=" + facilityId;

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Patient[]> responseEntity = restTemplate.getForEntity(URL, Patient[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedPatients = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedPatients;
    }

    public static Patient[] fetchAllPatientsByProviderId(Long providerId) {
        Patient[] returnedPatients = null;
        String URL = "http://localhost:8081/api/patients?providerId=" + providerId;

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Patient[]> responseEntity = restTemplate.getForEntity(URL, Patient[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedPatients = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedPatients;
    }

    // APPOINTMENT FETCHERS
    public static Appointment[] fetchAllAppointments() {
        Appointment[] returnedAppointments = null;
        String URL = "http://localhost:8081/api/appointments";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Appointment[]> responseEntity = restTemplate.getForEntity(URL, Appointment[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedAppointments = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedAppointments;
    }

    public static Appointment[] fetchAllAppointmentsByProviderId(Long providerId) {
        Appointment[] returnedAppointments = null;
        String URL = "http://localhost:8081/api/appointments?providerId=" + providerId;

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Appointment[]> responseEntity = restTemplate.getForEntity(URL, Appointment[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedAppointments = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedAppointments;
    }

    public static Appointment[] fetchAllAppointmentsByPatientId(Long patientId) {
        Appointment[] returnedAppointments = null;
        String URL = "http://localhost:8081/api/appointments?patientId=" + patientId;

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Appointment[]> responseEntity = restTemplate.getForEntity(URL, Appointment[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedAppointments = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedAppointments;
    }


    // FACILITY FETCHERS
    public static Facility[] fetchAllFacilities() {
        Facility[] returnedFacilities = null;
        String URL = "http://localhost:8081/api/facilities";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Facility[]> responseEntity = restTemplate.getForEntity(URL, Facility[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedFacilities = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedFacilities;
    }
}
