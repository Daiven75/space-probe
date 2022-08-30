package com.lucasilva.spaceprobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Probe implements Serializable {

    @Serial
    private static final long serialVersionUID = 312931293L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private StatusProbe status;

    @Column(nullable = true)
    private Integer positionX;

    @Column(nullable = true)
    private Integer positionY;

    @Enumerated(EnumType.STRING)
    private DirectionProbe direction;

    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "planet_id", nullable = true)
    private Planet planet;

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
