package com.example.pi.Controllers;

import com.example.pi.Models.Temperament;
import com.example.pi.Models.TemperamentDao;
import com.example.pi.Services.TemperamentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("temperaments")
public class TemperamentController {
    @Autowired
    private TemperamentService temperamentService;
    @Autowired
    private TemperamentDao temperamentDao;

    @GetMapping("/temperaments")
    public ResponseEntity<List<Temperament>> get(Model model){
        List<Temperament> temperaments = temperamentService.getAllTemperaments();
        return new ResponseEntity<>(temperaments, HttpStatus.OK);
    }
    @GetMapping("/temperamentsApi")
    public ResponseEntity<List<Temperament>> getApi(){
        List<Temperament> temperaments = temperamentService.getApiTemperaments();
        return new ResponseEntity<>(temperaments, HttpStatus.OK);
    }

    @PostMapping("/temperaments/newTemp")
    public ResponseEntity<String> post(@Valid @RequestBody String temperament){
        temperamentService.postTemperament(temperament);
        return new ResponseEntity<>(temperament, HttpStatus.CREATED);
    }
}
