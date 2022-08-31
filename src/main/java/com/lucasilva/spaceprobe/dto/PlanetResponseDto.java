package com.lucasilva.spaceprobe.dto;

import org.springframework.hateoas.RepresentationModel;

public class PlanetResponseDto extends RepresentationModel<PlanetResponseDto> {

    private String id;

    private String name;

    private Integer sizeAreaPlanet;

    private String galaxyName;

    public PlanetResponseDto(String id, String name, Integer sizeAreaPlanet, String galaxyName) {
        this.id = id;
        this.name = name;
        this.sizeAreaPlanet = sizeAreaPlanet;
        this.galaxyName = galaxyName;
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

    public String getGalaxyName() {
        return galaxyName;
    }

    public void setGalaxyName(String galaxyName) {
        this.galaxyName = galaxyName;
    }
}
