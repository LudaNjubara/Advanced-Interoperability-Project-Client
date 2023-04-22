package com.app.healthcare.healthcare_app_client.utils;

import com.app.healthcare.healthcare_app_client.model.Facility;
import com.app.healthcare.healthcare_app_client.model.Provider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.app.healthcare.healthcare_app_client.utils.Utils.setupRestTemplate;

public class Fetchers {
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
