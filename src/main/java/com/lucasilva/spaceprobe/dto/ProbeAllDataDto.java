package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class ProbeAllDataDto extends RepresentationModel<ProbeAllDataDto> {

    private String id;

    private String name;

    private StatusProbe status;

    private Integer positionX;

    private Integer positionY;

    private DirectionProbe direction;

    private Planet planet;

    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    public ProbeAllDataDto(Probe probe) {
        this.id = probe.getId();
        this.name = probe.getName();
        this.status = probe.getStatus();
        this.positionX = probe.getPositionX();
        this.positionY = probe.getPositionY();
        this.direction = probe.getDirection();
        this.createdAt = probe.getCreatedAt();
        this.editedAt = probe.getEditedAt();
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

    public DirectionProbe getDirection() {
        return direction;
    }

    public void setDirection(DirectionProbe direction) {
        this.direction = direction;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }
}