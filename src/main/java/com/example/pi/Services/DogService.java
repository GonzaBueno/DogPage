package com.example.pi.Services;

import com.example.pi.Models.Dog;
import com.example.pi.Models.DogDto;

import java.util.List;
import java.util.UUID;

public interface DogService {
    public List<Dog> getDataBaseDogs();
    public List<Dog> getDogs();
    public Dog postDog(DogDto dogDto);
    public Dog deleteDog(UUID idDog);
    public Dog findDog(UUID idDog);
    public Dog putDog(UUID idDog, Dog dog);
}