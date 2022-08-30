package com.lucasilva.spaceprobe.repository;

import com.lucasilva.spaceprobe.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, String> {

    @Transactional
    @Override
    <S extends Planet> S save(S entity);

    Optional<Planet> findByName(String name);
}
