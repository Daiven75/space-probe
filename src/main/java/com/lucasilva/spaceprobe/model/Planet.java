package com.lucasilva.spaceprobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Planet implements Serializable {

    @Serial
    private static final long serialVersionUID = 981237521L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(updatable = false, nullable = false, columnDefinition = "integer default 5")
    private Integer sizeAreaPlanet = 5;

    @OneToMany(mappedBy = "planet")
    private List<Probe> probes = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "galaxy_id")
    private Galaxy galaxy;

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
