package com.lucasilva.spaceprobe.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.lucasilva.spaceprobe.dto.*;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import com.lucasilva.spaceprobe.resources.PlanetResource;
import com.lucasilva.spaceprobe.resources.ProbeResource;
import com.lucasilva.spaceprobe.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerConverter {

    @Autowired
    private PlanetService planetService;

    public Probe toProbe(ProbeDto probeDto) {
        var probe = new Probe();
        probe.setName(probeDto.name());

        probe.setStatus(StatusProbe.WALKING_IN_SPACE);

        if(Objects.nonNull(probeDto.planetId())) {
            var planet = planetService.getPlanetById(probeDto.planetId());
            probe.setPlanet(planet);
            probe.setStatus(StatusProbe.ACTIVE);
        }

        return probe;
    }

    public Probe toProbe(ProbeAllDataDto probeAllDataDto) {
        var probe = new Probe();
        probe.setId(probeAllDataDto.getId());
        probe.setName(probeAllDataDto.getName());
        probe.setStatus(probeAllDataDto.getStatus());
        probe.setDirection(probeAllDataDto.getDirection());
        probe.setPlanet(probeAllDataDto.getPlanet());
        probe.setPositionX(probeAllDataDto.getPositionX());
        probe.setPositionY(probeAllDataDto.getPositionY());
        probe.setCreatedAt(probeAllDataDto.getCreatedAt());
        probe.setEditedAt(probeAllDataDto.getEditedAt());
        return probe;
    }

    public ProbeAllDataDto toProbeAllDataDto(Probe probe) {
        var probeAllDataDto = new ProbeAllDataDto(probe);

        probeAllDataDto.add(linkTo(methodOn(ProbeResource.class)
                .getProbeById(probe.getId()))
                .withSelfRel());

        probeAllDataDto.add(linkTo(ProbeResource.class)
                .slash(probe.getId())
                .withRel("update-probe"));

        probeAllDataDto.add(linkTo(methodOn(ProbeResource.class)
                .getAllProbes())
                .withRel("all-probes"));

        return probeAllDataDto;
    }

    public ProbeResponseDto toProbeResponseDto(Probe probe) {
        var message = "this probe is not on any planets at the moment!";

        var planetName = Objects.isNull(probe.getPlanet()) ? message : probe.getPlanet().getName();

        var probeResponseDto = new ProbeResponseDto(probe.getId(), probe.getName(), probe.getStatus(), planetName);

        probeResponseDto.add(linkTo(methodOn(ProbeResource.class)
                .getProbeById(probe.getId()))
                .withSelfRel());

        probeResponseDto.add(linkTo(methodOn(ProbeResource.class)
                .getAllProbes())
                .withRel("all-probes"));

        probeResponseDto.add(linkTo(ProbeResource.class)
                .slash(probe.getId())
                .withRel("update-probe"));

        return probeResponseDto;
    }

    public ProbeUpdateResponseDto toProbeUpdateResponseDto(Probe probe, String messageStatusPlanet) {
        var probeUpdateResponseDto = new ProbeUpdateResponseDto(
                probe.getId(),
                probe.getName(),
                probe.getStatus(),
                probe.getDirection(),
                probe.getPositionX(),
                probe.getPositionY(),
                messageStatusPlanet);

        probeUpdateResponseDto.add(linkTo(methodOn(ProbeResource.class)
                .getProbeById(probe.getId()))
                .withSelfRel());

        probeUpdateResponseDto.add(linkTo(methodOn(ProbeResource.class)
                .getAllProbes())
                .withRel("all-probes"));

        probeUpdateResponseDto.add(linkTo(ProbeResource.class)
                .slash(probe.getId())
                .withRel("update-probe"));

        return probeUpdateResponseDto;
    }

    public Planet toPlanet(PlanetDto planetDto) {
        var planet = new Planet();
        planet.setName(planetDto.name());
        return planet;
    }

    public PlanetResponseDto toPlanetResponseDto(Planet planet) {
        var planetResponseDto = new PlanetResponseDto(
                planet.getId(),
                planet.getName(),
                planet.getSizeAreaPlanet(),
                planet.getGalaxy().getName());

        planetResponseDto.add(linkTo(methodOn(PlanetResource.class)
                .getPlanetById(planet.getId()))
                .withSelfRel());

        planetResponseDto.add(linkTo(methodOn(PlanetResource.class)
                .getAllPlanets())
                .withRel("all-planets"));

        return planetResponseDto;
    }

    public PlanetAllDataDto toPlanetAllDataDto(Planet planet) {
        var planetAllDataDto = new PlanetAllDataDto(planet);

        planetAllDataDto.add(linkTo(methodOn(PlanetResource.class)
                .getPlanetById(planet.getId()))
                .withSelfRel());

        planetAllDataDto.add(linkTo(methodOn(PlanetResource.class)
                .getAllPlanets())
                .withRel("all-planets"));

        return planetAllDataDto;
    }

    public Galaxy toGalaxy(GalaxyDto galaxyDto) {
        var galaxy = new Galaxy();
        galaxy.setName(galaxyDto.name());
        return galaxy;
    }

    public GalaxyResponseDto toGalaxyResponseDto(Galaxy galaxy) {
        return new GalaxyResponseDto(galaxy.getId(), galaxy.getName());
    }
}
