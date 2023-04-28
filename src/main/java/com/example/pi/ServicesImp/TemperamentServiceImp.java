package com.example.pi.ServicesImp;

import com.example.pi.Models.Temperament;
import com.example.pi.Models.TemperamentDao;
import com.example.pi.Services.TemperamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TemperamentServiceImp implements TemperamentService {
    @Autowired
    private TemperamentDao temperamentDao;
    private Temperament temperament;
    @Override
    public List<Temperament> getApiTemperaments() {
        String url = "https://api.thedogapi.com/v1/breeds";
        return null;
    }

    @Override
    public List<Temperament> getAllTemperaments() {
        return null;
    }

    @Override
    public Temperament postTemperament() {

        return null;
    }
}
