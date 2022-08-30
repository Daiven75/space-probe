package com.lucasilva.spaceprobe.service;

import com.lucasilva.spaceprobe.dto.GalaxyDto;
import com.lucasilva.spaceprobe.dto.GalaxyResponseDto;
import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.repository.GalaxyRepository;
import com.lucasilva.spaceprobe.service.exceptions.GalaxyAlreadyExistsException;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
import com.lucasilva.spaceprobe.utils.CustomerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalaxyService {

    @Autowired
    private GalaxyRepository repository;

    @Autowired
    @Lazy
    private CustomerConverter converter;

    public GalaxyResponseDto discoveryGalaxy(GalaxyDto galaxyDto) {

        repository.findByName(galaxyDto.name()).ifPresent(e -> {
            throw new GalaxyAlreadyExistsException(ErroType.GALAXY_ALREADY_EXISTS.toString());
        });

        var galaxy = converter.toGalaxy(galaxyDto);

        var newGalaxy = repository.save(galaxy);

        return converter.toGalaxyResponseDto(newGalaxy);
    }

    public List<GalaxyResponseDto> listAllGalaxySummarized() {
        var galaxyList = repository.findAll();
        return galaxyList.stream()
                .map(galaxy -> converter.toGalaxyResponseDto(galaxy))
                .toList();
    }

    public List<Galaxy> listAllGalaxy() {
        return repository.findAll();
    }

    public Galaxy getGalaxyById(String galaxyId) {
        var galaxy = repository.findById(galaxyId);
        return galaxy.orElseThrow(
                () -> new ObjectNotFoundException(ErroType.GALAXY_NOT_FOUND.toString()));
    }
}
