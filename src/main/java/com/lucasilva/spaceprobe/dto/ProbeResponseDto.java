package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.enums.StatusProbe;
import org.springframework.hateoas.RepresentationModel;

public class ProbeResponseDto extends RepresentationModel<ProbeResponseDto> {

    private String id;
    private String name;
    private StatusProbe status;
    private String planetName;

    public ProbeResponseDto(String id, String name, StatusProbe status, String planetName) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.planetName = planetName;
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

    public StatusProbe getStatus() {
        return status;
    }

    public void setStatus(StatusProbe status) {
        this.status = status;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }
}