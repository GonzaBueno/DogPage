package com.example.pi.ServicesImp;

import com.example.pi.Models.Dog;
import com.example.pi.Models.DogDao;
import com.example.pi.Models.DogDto;
import com.example.pi.Services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DogServiceImp implements DogService {
    @Autowired
    private DogDao dogDao;
    private DogDto dogDto;

    @Override
    @Transactional
    public List<Dog> getDataBaseDogs(){
        return (List<Dog>) dogDao.findAll();
    }
    @Override
    @Transactional
    public List<Dog> getDogs() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.thedogapi.com/v1/breeds";
        List<Dog> dogsFromExternalRequest = restTemplate.getForObject(url, List.class);
        List<Dog> dogsFromDataBase = getDataBaseDogs();
        List<Dog> combinedDogsList = new ArrayList<>();
        combinedDogsList.addAll(dogsFromDataBase);
        combinedDogsList.addAll(dogsFromExternalRequest);
        return combinedDogsList;
    }

    @Override
    public Dog postDog(DogDto dogDto) {
        System.out.println(dogDto);
        Dog dog = Dog.builder()
                .name(dogDto.getName())
                .weight(dogDto.getWeight())
                .height(dogDto.getHeight())
                .life_span(dogDto.getLife_span())
                .image(dogDto.getImage()).build();
        return dogDao.save(dog);
    }

    @Override
    public Dog deleteDog(UUID idDog) {
        dogDao.deleteById(idDog);
        return null;
    }

    @Override
    public Dog findDog(UUID idDog) {
        return dogDao.findById(idDog).orElse(null);
    }

    @Override
    public Dog putDog(UUID idDog, Dog dog) {
        Dog updatedDog = dogDao.findById(idDog).orElse(null);
        if(dog.getName() != null){
            updatedDog.setName(dog.getName());
        }
        if(dog.getWeight() != null){
            updatedDog.setWeight(dog.getWeight());
        }
        if(dog.getHeight() != null){
            updatedDog.setHeight(dog.getHeight());
        }
        if(dog.getLife_span() != null){
            updatedDog.setLife_span(dog.getLife_span());
        }
        if(dog.getImage() != null){
            updatedDog.setImage(dog.getImage());
        }
        Dog newDog = dogDao.save(updatedDog);
        return newDog;
    }
}
