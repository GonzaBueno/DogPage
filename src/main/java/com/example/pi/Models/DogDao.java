package com.example.pi.Models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DogDao extends CrudRepository<Dog, UUID> {
    public boolean existsByName(String name);
}
