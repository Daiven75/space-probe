package com.lucasilva.spaceprobe.resources;

import com.lucasilva.spaceprobe.dto.GalaxyDto;
import com.lucasilva.spaceprobe.dto.GalaxyResponseDto;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.service.GalaxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/galaxies")
public class GalaxyResource {

    @Autowired
    private GalaxyService service;

    @PostMapping
    public ResponseEntity<GalaxyResponseDto> discoveryGalaxy(@RequestBody @Valid GalaxyDto galaxyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.discoveryGalaxy(galaxyDto));
    }

    @GetMapping
    public ResponseEntity<List<GalaxyResponseDto>> getAllGalaxies() {
        return ResponseEntity.ok(service.listAllGalaxySummarized());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Galaxy> getGalaxyById(@PathVariable String id) {
        return ResponseEntity.ok(service.getGalaxyById(id));
    }
}
