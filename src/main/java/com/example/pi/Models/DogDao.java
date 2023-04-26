package com.example.pi.Models;

import com.example.pi.Models.Dog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DogDao extends CrudRepository<Dog, UUID> {
}
