package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

public class PlanetAllDataDto extends RepresentationModel<PlanetAllDataDto> {

    private String id;

    private String name;

    private Integer sizeAreaPlanet = 5;

    private List<Probe> probes = new ArrayList<>();

    private Galaxy galaxy;

    public PlanetAllDataDto(Planet planet) {
        this.id = planet.getId();
        this.name = planet.getName();
        this.sizeAreaPlanet = planet.getSizeAreaPlanet();
        this.probes = planet.getProbes();
        this.galaxy = planet.getGalaxy();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSizeAreaPlanet() {
        return sizeAreaPlanet;
    }

    public void setSizeAreaPlanet(Integer sizeAreaPlanet) {
        this.sizeAreaPlanet = sizeAreaPlanet;
    }

    public List<Probe> getProbes() {
        return probes;
    }

    public void setProbes(List<Probe> probes) {
        this.probes = probes;
    }

    public Galaxy getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(Galaxy galaxy) {
        this.galaxy = galaxy;
    }
}
