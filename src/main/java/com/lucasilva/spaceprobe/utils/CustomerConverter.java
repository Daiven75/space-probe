package com.lucasilva.spaceprobe.utils;

import com.lucasilva.spaceprobe.dto.*;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
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

    public ProbeResponseDto toProbeResponseDto(Probe probe) {
        var message = "this probe is not on any planets at the moment!";
        var planetName = Objects.isNull(probe.getPlanet()) ? message : probe.getPlanet().getName();
        return new ProbeResponseDto(
                probe.getId(), probe.getName(), probe.getStatus(), planetName);
    }

    public ProbeUpdateResponseDto toProbeUpdateResponseDto(Probe probe, String messageStatusPlanet) {
        return new ProbeUpdateResponseDto(
                probe.getId(),
                probe.getName(),
                probe.getStatus(),
                probe.getDirection(),
                probe.getPositionX(),
                probe.getPositionY(),
                messageStatusPlanet);
    }

    public Planet toPlanet(PlanetDto planetDto) {
        var planet = new Planet();
        planet.setName(planetDto.name());
        return planet;
    }

    public PlanetResponseDto toPlanetResponseDto(Planet planet) {
        return new PlanetResponseDto(
                planet.getId(),
                planet.getName(),
                planet.getSizeAreaPlanet(),
                planet.getGalaxy().getName());
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
