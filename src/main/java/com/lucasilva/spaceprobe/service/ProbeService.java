package com.lucasilva.spaceprobe.service;

import com.lucasilva.spaceprobe.dto.ProbeDto;
import com.lucasilva.spaceprobe.dto.ProbeResponseDto;
import com.lucasilva.spaceprobe.dto.ProbeUpdateDto;
import com.lucasilva.spaceprobe.dto.ProbeUpdateResponseDto;
import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.repository.ProbeRepository;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
import com.lucasilva.spaceprobe.service.exceptions.PlanetFullyExploredException;
import com.lucasilva.spaceprobe.service.exceptions.ProbeAlreadyExistsException;
import com.lucasilva.spaceprobe.service.exceptions.ProbeWanderingInSpaceException;
import com.lucasilva.spaceprobe.utils.CustomerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProbeService {

    @Autowired
    private ProbeRepository repository;

    @Autowired
    private CustomerConverter converter;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private CommandService commandService;

    public ProbeResponseDto createProbe(ProbeDto probeDto) {

        repository.findByName(probeDto.name()).ifPresent(e -> {
            throw new ProbeAlreadyExistsException(ErroType.PROBE_ALREADY_EXISTS.toString());
        });

        var probe = converter.toProbe(probeDto);

        if(Objects.nonNull(probe.getPlanet())) {
            var direction = DirectionProbe.toEnum(randomDirectionForLanding());
            probe.setDirection(direction);

            var areaToExplore = checkAreaPlanetExploration(probe.getPlanet());

            var randomPositionsX = areaToExplore.keySet();
            probe.setPositionX(randomPositionForLanding(randomPositionsX));

            var randomPositionsY = areaToExplore.get(probe.getPositionX());
            probe.setPositionY(randomPositionForLanding(randomPositionsY));
        } else {
            probeLostInSpace(probe);
        }

        probe.setCreatedAt(LocalDateTime.now());
        probe.setEditedAt(LocalDateTime.now());

        var newProbe = repository.save(probe);

        return converter.toProbeResponseDto(newProbe);
    }

    public List<ProbeResponseDto> getAllProbes() {
        var probeList = repository.findAll();
        return probeList.stream()
                .map(probe -> converter.toProbeResponseDto(probe))
                .toList();
    }

    public Probe getProbeById(String probeId) {
        var probe = repository.findById(probeId);
        return probe.orElseThrow(
                () -> new ObjectNotFoundException(ErroType.PROBE_NOT_FOUND.toString()));
    }

    public ProbeUpdateResponseDto updateProbe(String probeId, ProbeUpdateDto probeUpdateDto) {
        var probe = getProbeById(probeId);
        String planetId = probeUpdateDto.planetId();

        if(Objects.isNull(planetId) && probe.getStatus().equals(StatusProbe.WALKING_IN_SPACE)) {
            throw new ProbeWanderingInSpaceException(ErroType.PROBE_WANDERING_IN_SPACE.toString());
        }

        var planet = planetService.getPlanetById(probe.getPlanet().getId());

        var probeWithPlanetChangeOrWithoutAssociatedPlanet = !Objects.deepEquals(planet, probe.getPlanet());

        if(probeWithPlanetChangeOrWithoutAssociatedPlanet) {
            probe.setPlanet(planet);

            var direction = DirectionProbe.toEnum(randomDirectionForLanding());
            probe.setDirection(direction);

            var areaToExplore = checkAreaPlanetExploration(planet);

            var randomPositionsX = areaToExplore.keySet();
            probe.setPositionX(randomPositionForLanding(randomPositionsX));

            var randomPositionsY = areaToExplore.get(probe.getPositionX());
            probe.setPositionY(randomPositionForLanding(randomPositionsY));

            probe.setStatus(StatusProbe.ACTIVE);
        }

        commandService.moveProbe(probe, probeUpdateDto.commands().toCharArray());

        boolean probeIsStillOnThePlanet = probeIsStillOnThePlanet(probe.getPositionX(), probe.getPositionY());

        String messageStatusPlanet;

        if(!probeIsStillOnThePlanet) {
            probeLostInSpace(probe);
            messageStatusPlanet = "Putz, commands took the probe off the planet, now it is in the cold of space :(";
        } else {
            messageStatusPlanet = checkingPossibleCollisionBetweenProbes(probe, planet);
        }

        probe.setEditedAt(LocalDateTime.now());
        var updateProbe = repository.save(probe);

        return converter.toProbeUpdateResponseDto(updateProbe, messageStatusPlanet);
    }

    private String checkingPossibleCollisionBetweenProbes(Probe probe, Planet planet) {

        var probePushedIntoSpace = planet.getProbes().stream()
                .filter(p -> Objects.equals(probe.getPositionX(), p.getPositionX()) &&
                        Objects.equals(probe.getPositionY(), p.getPositionY()) &&
                        !Objects.equals(probe.getName(), p.getName()))
                .findAny();

        if(probePushedIntoSpace.isPresent()) {
            probePushedIntoSpace.get().setEditedAt(LocalDateTime.now());

            repository.save(probeLostInSpace(probePushedIntoSpace.get()));

            return String.format("Houston we had a problem!...probe collided with the %s, and it left the planet %s and is wandering through space",
                    probePushedIntoSpace.get().getName(), planet.getName());
        }

        return "Ok, exploration in progress and everything under control....";
    }

    private boolean probeIsStillOnThePlanet(int x, int y) {
        return (x < 5 && x >= 0) && (y < 5 && y >= 0);
    }

    private HashMap<Integer, Set<Integer>> checkAreaPlanetExploration(Planet planet) {

        HashMap<Integer, Set<Integer>> areaToExplore = new HashMap<>();

        for (int i = 0; i < planet.getSizeAreaPlanet(); i++) {

            var area = i;

            var existsExploration = planet.getProbes().stream()
                    .filter(probe -> probe.getPositionX().equals(area))
                    .toList();

            var totalAreaExploration = setTotalAreaExploration(planet.getSizeAreaPlanet() - 1);
            areaToExplore.put(area, totalAreaExploration);

            if(!existsExploration.isEmpty()) {
                existsExploration.forEach(probe -> areaToExplore.get(area).remove(probe.getPositionY()));

                if(areaToExplore.get(area).isEmpty()) {
                    areaToExplore.remove(area);
                }
            }
        }

        if(areaToExplore.isEmpty()) {
            throw new PlanetFullyExploredException(ErroType.PLANET_FULLY_EXPLORED.toString());
        }

        return areaToExplore;
    }

    private int randomDirectionForLanding() {
        return new Random().nextInt(3);
    }

    private int randomPositionForLanding(Set<Integer> areaToExplore) {
        var arrayPositions = areaToExplore.toArray(new Integer[]{});
        var randomPosition = new Random().nextInt(areaToExplore.size());
        return arrayPositions[randomPosition];
    }

    private Set<Integer> setTotalAreaExploration(int end) {
        return IntStream.rangeClosed(0, end)
                .boxed()
                .collect(Collectors.toSet());
    }

    private Probe probeLostInSpace(Probe probe) {
        probe.setPlanet(null);
        probe.setStatus(StatusProbe.WALKING_IN_SPACE);
        probe.setDirection(null);
        probe.setPositionX(null);
        probe.setPositionY(null);
        return probe;
    }
}
