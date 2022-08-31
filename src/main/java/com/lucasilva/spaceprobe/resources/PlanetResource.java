package com.lucasilva.spaceprobe.resources;

import com.lucasilva.spaceprobe.dto.PlanetAllDataDto;
import com.lucasilva.spaceprobe.dto.PlanetDto;
import com.lucasilva.spaceprobe.dto.PlanetResponseDto;
import com.lucasilva.spaceprobe.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetResource {

    @Autowired
    private PlanetService service;

    @PostMapping
    public ResponseEntity<PlanetResponseDto> discoveryPlanet(@RequestBody @Valid PlanetDto planetDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.discoveryPlanet(planetDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanetAllDataDto> getPlanetById(@PathVariable String id) {
        return ResponseEntity.ok(service.getPlanetAllDataById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanetResponseDto>> getAllPlanets() {
        return ResponseEntity.ok(service.getAllPlanets());
    }
}
