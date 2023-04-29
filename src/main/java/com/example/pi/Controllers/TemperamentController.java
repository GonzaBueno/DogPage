package com.example.pi.Controllers;

import com.example.pi.Models.Temperament;
import com.example.pi.Services.TemperamentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Controller
public class TemperamentController {
    @Autowired
    private TemperamentService temperamentService;

    @GetMapping("/temperaments")
    public ResponseEntity<List<Temperament>> get(Model model){
        List<Temperament> temperaments = temperamentService.getAllTemperaments();
        return new ResponseEntity<>(temperaments, HttpStatus.OK);
    }
    @PostMapping("/temperaments/newTemp")
    public ResponseEntity<String> post(@Valid @RequestBody String temperament){
        temperamentService.postTemperament(temperament);
        return new ResponseEntity<>(temperament, HttpStatus.CREATED);
    }
}
