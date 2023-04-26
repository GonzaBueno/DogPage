package com.example.pi.Models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DogDto {
    private UUID id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String weight;
    @NotEmpty
    private String height;
    @NotEmpty
    private String life_span;
    @NotEmpty
    private String image;
}
