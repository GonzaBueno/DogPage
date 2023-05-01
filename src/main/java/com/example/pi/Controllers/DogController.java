package com.example.pi.Controllers;

import com.example.pi.Models.Dog;
import com.example.pi.Controllers.Dtos.DogDto;
import com.example.pi.Services.DogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class DogController {
    @Autowired
    private DogService dogService;

    @GetMapping("/dogs")
    public ResponseEntity<List<Dog>> Get(Model model) {
        List<Dog> dogList = dogService.getDogs();
        return new ResponseEntity<>(dogList, HttpStatus.OK);
    }
    @GetMapping("/dogsApi")
    public ResponseEntity<List<Dog>> GetDogsApi(Model model) {
        List<Dog> dogList = dogService.getApiDogs();
        return new ResponseEntity<>(dogList, HttpStatus.OK);
    }

    @PostMapping("/createDog")
    public ResponseEntity<Dog> post(@Valid @RequestBody DogDto dogDto) {
        Dog dog = dogService.postDog(dogDto);
        return new ResponseEntity<>(dog, HttpStatus.CREATED);
    }

    @DeleteMapping("deleteDog/{idDog}")
    public ResponseEntity<Dog> delete(@PathVariable("idDog") @NotEmpty UUID idDog) {
        if (idDog == null) {
            Dog dogFail = null;
            return new ResponseEntity<>(dogFail, HttpStatus.BAD_REQUEST);
        }
        log.info(idDog.toString());
        Dog deletedDog = dogService.deleteDog(idDog);
        return new ResponseEntity<>(deletedDog, HttpStatus.ACCEPTED);
    }

    @PutMapping("/edit/{idDog}")
    public ResponseEntity<Dog> put(@PathVariable("idDog") @NotEmpty UUID idDog, @Valid @RequestBody Dog dog, Model model) {
        if (idDog == null) {
            Dog dogFail = null;
            return new ResponseEntity<>(dogFail, HttpStatus.BAD_REQUEST);
        }
        Dog updatedDog = dogService.putDog(idDog, dog);
        model.addAttribute("dog", updatedDog);
        return new ResponseEntity<>(updatedDog, HttpStatus.ACCEPTED);
    }

    @GetMapping("/sortedDogs/{property}")
    public ResponseEntity<List<Dog>> sort(@PathVariable("property") String property, Model model){
        List<Dog> dogList = dogService.sortedDogs(property);
        model.addAttribute("dogs", dogList);
        return new ResponseEntity<>(dogList, HttpStatus.OK);
    }
    @GetMapping("/filtredDogs/{property}/{filter}")
    public ResponseEntity<List<Dog>> filter(@PathVariable("property") String property, @PathVariable("filter") String filter, Model model){
        log.info(property);
        log.info(filter);
        List<Dog> dogList = dogService.filtredDogs(property, filter);
        model.addAttribute("dogs");
        return new ResponseEntity<>(dogList, HttpStatus.OK);
    }

}
