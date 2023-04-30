package com.example.pi.ServicesImp;

import com.example.pi.Models.Temperament;
import com.example.pi.Models.TemperamentDao;
import com.example.pi.Services.TemperamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemperamentServiceImp implements TemperamentService {
    @Autowired
    private TemperamentDao temperamentDao;
    private Temperament temperament;
    @Override
    public List<Temperament> getApiTemperaments() {
        List<Temperament> apiTemperaments = new ArrayList<>();
        try {
            ResponseEntity<List<Map<String, String>>> response = new RestTemplate().exchange(
                    "https://api.thedogapi.com/v1/breeds",
                    HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            List<Map<String, String>> breeds = response.getBody();
            for (Map<String, String> breed : breeds) {
                String temperament = breed.get("temperament");
                if (temperament != null && !temperament.isEmpty()) {
                    for (String tempName : temperament.split(", ")) {
                       postTemperament(tempName);
                    }
                }
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return apiTemperaments;
    }

    @Override
    public List<Temperament> getAllTemperaments() {
        List<Temperament> temperaments = new ArrayList<>();
        for (Temperament t : temperamentDao.findAll()) {
            temperaments.add(t);
        }
        if (temperaments.isEmpty()) {
          getApiTemperaments();
        }
        return temperaments;
    }

    @Override
    public Temperament postTemperament(String temperament) {
       // if (temperamentDao.existsByName(temperament)) {
       //     throw new RuntimeException("EL NOMBRE DEL TEMPERAMENTO YA ESTA EN USO");//}
        Temperament newTemp = Temperament.builder()
                .name(temperament).build();

        return newTemp;
    }
}
