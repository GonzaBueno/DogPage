package com.example.pi.ServicesImp;

import aj.org.objectweb.asm.TypeReference;
import com.example.pi.Controllers.Dtos.DogDto;
import com.example.pi.Models.Dog;
import com.example.pi.Models.Temperament;
import com.example.pi.Models.TemperamentDao;
import com.example.pi.Services.TemperamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.util.json.JSONArray;
import org.h2.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TemperamentServiceImp implements TemperamentService {
    @Autowired
    private TemperamentDao temperamentDao;
    private Temperament temperament;
    @Override
    public List<Temperament> getApiTemperaments() {
        List<Temperament> temperaments = new ArrayList<>();
        List<String> existingTemperaments = new ArrayList<>();

        // Hacer una solicitud GET a la API y obtener todos los perros
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Dog>> response = restTemplate.exchange(
                "https://api.thedogapi.com/v1/breeds",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Dog>>() {}
        );
        List<Dog> dogs = response.getBody();

        // Iterar a través de cada perro y extraer sus temperamentos
        for (Dog dog : dogs) {
            Set<Temperament> temperamentSet = dog.getTemperaments();
            Set<String> temperamentStringSet = temperamentSet.stream()
                    .map(Temperament::getName)
                    .collect(Collectors.toSet());
            String temperamentString = String.join(", ", temperamentStringSet);
            if (temperamentString != null) {
                String[] temperamentArray = temperamentString.split(", ");
                for (String temperament : temperamentArray) {
                    if (!existingTemperaments.contains(temperament)) {
                        existingTemperaments.add(temperament);
                        Temperament newTemperament = new Temperament();
                        newTemperament.setName(temperament);
                        temperaments.add(temperamentDao.save(newTemperament));
                    }
                }
            }
        }

        // Ordenar los temperamentos alfabéticamente antes de devolverlos
       // Collections.sort(temperaments, new Comparator<Temperament>() {
        //    public int compare(Temperament t1, Temperament t2) {
         //       return t1.getName().compareTo(t2.getName());
        //    }
       // });

        return temperaments;
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
