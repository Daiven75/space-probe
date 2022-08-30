package com.lucasilva.spaceprobe.service;

import com.lucasilva.spaceprobe.dto.PlanetDto;
import com.lucasilva.spaceprobe.dto.PlanetResponseDto;
import com.lucasilva.spaceprobe.dto.ProbeResponseDto;
import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import com.lucasilva.spaceprobe.repository.PlanetRepository;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
import com.lucasilva.spaceprobe.service.exceptions.PlanetAlreadyExistsException;
import com.lucasilva.spaceprobe.service.exceptions.ProbeAlreadyExistsException;
import com.lucasilva.spaceprobe.utils.CustomerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository repository;

    @Autowired
    private GalaxyService galaxyService;

    @Autowired
    @Lazy
    private CustomerConverter converter;

    public PlanetResponseDto discoveryPlanet(PlanetDto planetDto) {

        repository.findByName(planetDto.name()).ifPresent(e -> {
            throw new PlanetAlreadyExistsException(ErroType.PLANET_ALREADY_EXISTS.toString());
        });

        var planet = converter.toPlanet(planetDto);
        var listGalaxy = galaxyService.listAllGalaxy();

        if(Objects.isNull(planetDto.galaxyId())) {
            planet.setGalaxy(randomGalaxy(listGalaxy));
        } else {
            var galaxyFound = listGalaxy.stream()
                    .filter(g -> Objects.equals(g.getId(), planetDto.galaxyId()))
                    .findFirst();

            galaxyFound.ifPresentOrElse(planet::setGalaxy, () -> planet.setGalaxy(randomGalaxy(listGalaxy)));
        }

        var newPlanet = repository.save(planet);

        return converter.toPlanetResponseDto(newPlanet);
    }

    public Planet getPlanetById(String planetId) {
        var planet = repository.findById(planetId);
        return planet.orElseThrow(
                () -> new ObjectNotFoundException(ErroType.PLANET_NOT_FOUND.toString()));
    }

    public List<PlanetResponseDto> getAllPlanets() {
        var planetList = repository.findAll();
        return planetList.stream()
                .map(planet -> converter.toPlanetResponseDto(planet))
                .toList();
    }

    private Galaxy randomGalaxy(List<Galaxy> galaxyList) {
        return galaxyList.get(new Random().nextInt(galaxyList.size()));
    }
}
