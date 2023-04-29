package com.example.pi.Services;

import com.example.pi.Models.Temperament;
import java.util.List;
import java.util.UUID;

public interface TemperamentService {
    public List<Temperament> getApiTemperaments();
    public List<Temperament> getAllTemperaments();
    public Temperament postTemperament(String temperament);

}
