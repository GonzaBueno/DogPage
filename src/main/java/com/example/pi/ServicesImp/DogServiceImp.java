package com.example.pi.ServicesImp;

import com.example.pi.Controllers.Dtos.DogDto;
import com.example.pi.Models.*;
import com.example.pi.Services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DogServiceImp implements DogService {
    @Autowired
    private DogDao dogDao;
    private DogDto dogDto;
    private ApiDog apiDog;
    private Temperament temperament;

    @Override
    @Transactional
    public List<Dog> getDataBaseDogs(){
        return (List<Dog>) dogDao.findAll();
    }

    @Override
    public List<Dog> getApiDogs() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.thedogapi.com/v1/breeds";

        ParameterizedTypeReference<List<ApiDog>> responseType = new ParameterizedTypeReference<List<ApiDog>>() {};
        ResponseEntity<List<ApiDog>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        List<ApiDog> dogsFromExternalRequest = responseEntity.getBody();

        List<Dog> dogs = new ArrayList<>();
        // el JSON de la api estadesordenado y muchas veces no tiene la informacion en la misma instancia,
        //por eso es importante hacer una limpieza para tener todos los objetos lo mas parecidos posible
        for(ApiDog apiDog : dogsFromExternalRequest) {
            String weight = "Desconocido";
            Object weightObj = apiDog.getWeight();
            if (weightObj instanceof ApiDog.Weight) {
                weight = ((ApiDog.Weight) weightObj).getMetric();
            } else if (weightObj instanceof String) {
                weight = (String) weightObj;
            }

            String height = "Desconocido";
            Object heightObj = apiDog.getHeight();
            if (heightObj instanceof ApiDog.Height) {
                height = ((ApiDog.Height) heightObj).getMetric();
            } else if (heightObj instanceof String) {
                height = (String) heightObj;
            }

            String lifeSpan = apiDog.getLife_span() != null ? apiDog.getLife_span() : "Desconocido";

            String image = "Desconocido";
            Object imageObj = apiDog.getImage();
            if (imageObj instanceof ApiDog.Image) {
                image = ((ApiDog.Image) imageObj).getUrl();
            } else if (imageObj instanceof String) {
                image = (String) imageObj;
            }

            Dog dog = Dog.builder()
                    .name(apiDog.getName())
                    .weight(weight)
                    .height(height)
                    .life_span(lifeSpan)
                    .image(image)
                    .build();
            dog.setId(UUID.randomUUID());
            dogs.add(dog);
            //postDog(dog);
        }
        return dogs;
    }

    @Override
    @Transactional
    public List<Dog> getDogs() {
        List<Dog> apiDogs = getApiDogs();
        List<Dog> combinedDogsList = new ArrayList<>();
        List<Dog> dogsFromDataBase = getDataBaseDogs();
        combinedDogsList.addAll(dogsFromDataBase);
        combinedDogsList.addAll(apiDogs);
        return combinedDogsList;
    }

    @Override
    public Dog postDog(DogDto dogDto) {
        System.out.println(dogDto);

        if (dogDao.existsByName(dogDto.getName())) {
            throw new RuntimeException("EL NOMBRE DEL PERRO YA ESTA EN USO");
        }
        try {
            URL url = new URL(dogDto.getImage());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La URL de la imagen no es válida");
        }

        Dog dog = Dog.builder()
                .name(dogDto.getName())
                .weight(dogDto.getWeight())
                .height(dogDto.getHeight())
                .life_span(dogDto.getLife_span())
                .image(dogDto.getImage()).build();
        dog.setTemperaments(new HashSet<>(dog.getTemperaments()));
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
    @Override
    public List<Dog> sortedDogs(String property) {
        List<Dog> sortedList = getDogs();
        if(property.equals("height")){
            Collections.sort(sortedList, Comparator.comparing(Dog::getHeight));
        }
        if(property.equals("weight")){
            Collections.sort(sortedList, Comparator.comparing(Dog::getWeight));
        }
        if(property.equals("life_span")){
            Collections.sort(sortedList, Comparator.comparing(Dog::getLife_span));
        }
        if(property.equals("name")){
            Collections.sort(sortedList, Comparator.comparing(Dog::getName));
        }
        return sortedList;
    }
    @Override
    public List<Dog> filtredDogs(String property, String filter) {
        System.out.println(property);
        System.out.println(filter);
        List<Dog> dogList = getDogs();
        if(property.equals("height")){
            return dogList.stream().filter(dog -> dog.getHeight().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }
        if(property.equals("weight")){
            return dogList.stream().filter(dog -> dog.getWeight().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }
        if(property.equals("life_span")){
            return dogList.stream().filter(dog -> dog.getLife_span().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }
        if(property.equals("name")){
            return dogList.stream().filter(dog -> dog.getName().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }
        return null;
    }
}
