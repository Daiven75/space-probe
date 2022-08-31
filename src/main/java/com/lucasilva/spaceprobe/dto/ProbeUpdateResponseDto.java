package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import org.springframework.hateoas.RepresentationModel;

public class ProbeUpdateResponseDto extends RepresentationModel<ProbeUpdateResponseDto> {

    private String id;

    private String name;

    StatusProbe status;

    DirectionProbe direction;

    Integer positionX;

    Integer positionY;

    String statusPlanet;

    public ProbeUpdateResponseDto(String id, String name, StatusProbe status, DirectionProbe direction,
                                  Integer positionX, Integer positionY, String statusPlanet) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.direction = direction;
        this.positionX = positionX;
        this.positionY = positionY;
        this.statusPlanet = statusPlanet;
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

    public DirectionProbe getDirection() {
        return direction;
    }

    public void setDirection(DirectionProbe direction) {
        this.direction = direction;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public String getStatusPlanet() {
        return statusPlanet;
    }

    public void setStatusPlanet(String statusPlanet) {
        this.statusPlanet = statusPlanet;
    }
}
