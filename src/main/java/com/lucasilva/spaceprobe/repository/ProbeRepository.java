package com.lucasilva.spaceprobe.repository;

import com.lucasilva.spaceprobe.model.Probe;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProbeRepository extends JpaRepository<Probe, String> {

    @Transactional
    @Override
    <S extends Probe> S save(S entity);

    Optional<Probe> findByName(String name);
}
