package com.lucasilva.spaceprobe.repository;

import com.lucasilva.spaceprobe.model.Galaxy;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface GalaxyRepository extends JpaRepository<Galaxy, String> {

    @Transactional
    @Override
    <S extends Galaxy> S save(S entity);

    Optional<Galaxy> findByName(String name);
}
