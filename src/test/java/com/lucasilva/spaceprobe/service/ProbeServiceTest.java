package com.lucasilva.spaceprobe.service;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

import com.lucasilva.spaceprobe.dto.*;
import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import com.lucasilva.spaceprobe.repository.ProbeRepository;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
import com.lucasilva.spaceprobe.service.exceptions.ProbeWanderingInSpaceException;
import com.lucasilva.spaceprobe.utils.CustomerConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ProbeServiceTest {

    @Mock
    private ProbeRepository repository;

    @InjectMocks
    private ProbeService service;

    @Mock
    private CustomerConverter converter;

    @Mock
    private CommandService commandService;

    @Mock
    private PlanetService planetService;

    private Probe probe;

    private ProbeAllDataDto probeAllDataDto;

    private ProbeResponseDto probeResponseDto;

    private ProbeUpdateResponseDto probeUpdateResponseDto;

    @BeforeEach
    void init() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId("09f9e805-24dd-11ed-8f60-0242ac110002");
        galaxy.setName("GalaxyA");

        Planet planet = new Planet();
        planet.setId("41829c9682e5d9530182e5d96bf10000");
        planet.setName("PlanetA");
        planet.setSizeAreaPlanet(5);
        planet.setGalaxy(galaxy);

        this.probe = new Probe();
        probe.setId("41829c9682e347290182e3480c300000");
        probe.setName("probe7");
        probe.setStatus(StatusProbe.ACTIVE);
        probe.setDirection(DirectionProbe.NORTH);
        probe.setPlanet(planet);
        probe.setPositionX(2);
        probe.setPositionY(3);
        probe.setEditedAt(LocalDateTime.now());
        probe.setCreatedAt(LocalDateTime.now());

        this.probeAllDataDto = new ProbeAllDataDto(this.probe);

        this.probeResponseDto = new ProbeResponseDto(
                probe.getId(),
                probe.getName(),
                probe.getStatus(),
                probe.getPlanet().getName());

        String messageStatusPlanet = "Ok, exploration in progress and everything under control....";
        this.probeUpdateResponseDto = new ProbeUpdateResponseDto(
                probe.getId(),
                probe.getName(),
                probe.getStatus(),
                probe.getDirection(),
                probe.getPositionX(),
                probe.getPositionY(),
                messageStatusPlanet);
    }

    @Test
    public void createProbeWithSucess() {

        var probeDto = new ProbeDto(probe.getName(), probe.getPlanet().getId());

        when(converter.toProbe(probeDto)).thenReturn(probe);

        when(repository.findByName("test")).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.createProbe(probeDto)).doesNotThrowAnyException();
    }

    @DisplayName("Try create probe, but already exists probe with same name")
    @Test
    public void createProbeButThrowExceptionBecauseAlreadyExists() {

        var probeDto = new ProbeDto("probe7", probe.getPlanet().getId());

        when(repository.save(probe)).thenReturn(any());

        when(repository.findByName("probe7")).thenReturn(Optional.of(probe));

        Assertions.assertThatCode(() -> service.createProbe(probeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("PRB-0001");
    }

    @Test
    public void createProbeButWithoutPlanetToExplore() {

        var probeDto = new ProbeDto("probe", null);

        probe.setPlanet(null);

        when(repository.findByName("probe")).thenReturn(Optional.empty());

        when(converter.toProbe(probeDto)).thenReturn(probe);

        when(repository.save(probe)).thenReturn(probe);

        var message = "this probe is not on any planets at the moment!";
        var probeResponseDtoWithoutPlanet = new ProbeResponseDto(
                probe.getId(),
                probe.getName(),
                StatusProbe.WALKING_IN_SPACE,
                message);

        when(converter.toProbeResponseDto(probe)).thenReturn(probeResponseDtoWithoutPlanet);

        Assertions.assertThatCode(() -> {
            var probe = service.createProbe(probeDto);
            if(!Objects.equals(StatusProbe.WALKING_IN_SPACE, probe.getStatus())) {
                throw new RuntimeException("probe is not wandering through space");
            }
        }).doesNotThrowAnyException();
    }

    @Test
    public void findAllProbesWithSucess() {

        var probe = new Probe();
        probe.setId("41829c9682e347290182e3480c30004124");
        probe.setName("probe8");
        probe.setStatus(StatusProbe.WALKING_IN_SPACE);
        probe.setEditedAt(LocalDateTime.now());
        probe.setCreatedAt(LocalDateTime.now());

        given(repository.findAll()).willReturn(of(probe, this.probe));

        var probeList = service.getAllProbes();

        Assertions.assertThat(probeList).isNotNull();
        Assertions.assertThat(probeList.size()).isEqualTo(2);
    }

    @DisplayName("Get probe by id works properly")
    @Test
    public void getProbeByIdThenReturnProbeObject(){
        given(repository.findById(probe.getId())).willReturn(Optional.of(probe));
        given(converter.toProbeAllDataDto(probe)).willReturn(probeAllDataDto);

        var savedProbe = service.getProbeAllDataById(probe.getId());

        Assertions.assertThat(savedProbe).isNotNull();
    }

    @DisplayName("Get probe by id but not found")
    @Test
    public void getProbeByIdButThrowException(){
        given(repository.findById("fsad3421432")).willReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.getProbeAllDataById("fsad3421432"))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("PRB-0003");
    }

    @DisplayName("Update X and Y positions, and probe will still remain on the planet")
    @Test
    public void updateProbeAlreadyWithPlanetExplorationExistent() {

        given(repository.save(probe)).willReturn(probe);
        given(repository.findById(probe.getId())).willReturn(Optional.of(probe));
        given(planetService.getPlanetById(probe.getPlanet().getId())).willReturn(probe.getPlanet());

        var messagePlanet = "Ok, exploration in progress and everything under control....";
        given(converter.toProbeUpdateResponseDto(probe, messagePlanet)).willReturn(probeUpdateResponseDto);

        var probeUpdateDto = new ProbeUpdateDto("MMRR", probe.getPlanet().getId());
        var commands = probeUpdateDto.commands().toCharArray();
        willDoNothing().given(commandService).moveProbe(probe, commands);

        var probeUpdate = service.updateProbe(probe.getId(), probeUpdateDto);

        Assertions.assertThat(probeUpdate.getStatus()).isEqualTo(StatusProbe.ACTIVE);
        Assertions.assertThat(probeUpdate.getDirection()).isEqualTo(DirectionProbe.NORTH);
    }

    @DisplayName("Try update probe, but not associated with a planet and not in active status")
    @Test
    public void updateProbebutItsWanderingThroughSpace() {

        probe.setPlanet(null);
        probe.setStatus(StatusProbe.WALKING_IN_SPACE);

        given(repository.save(probe)).willReturn(probe);
        given(repository.findById(probe.getId())).willReturn(Optional.of(probe));

        var probeUpdateDto = new ProbeUpdateDto("RRMM", null);

        Assertions.assertThatCode(() -> service.updateProbe(probe.getId(), probeUpdateDto))
                .isInstanceOf(ProbeWanderingInSpaceException.class)
                .hasMessageContaining("PRB-0004");
    }

    @DisplayName("Update probe that is wandering through space to a planet")
    @Test
    public void updateProbeAddAssociationWithPlanet() {

        var planet = probe.getPlanet();
        probe.setPlanet(null);
        probe.setStatus(StatusProbe.WALKING_IN_SPACE);

        given(repository.save(probe)).willReturn(probe);
        given(repository.findById(probe.getId())).willReturn(Optional.of(probe));
        given(planetService.getPlanetById(planet.getId())).willReturn(planet);

        var messagePlanet = "Ok, exploration in progress and everything under control....";
        given(converter.toProbeUpdateResponseDto(probe, messagePlanet)).willReturn(probeUpdateResponseDto);

        var probeUpdateDto = new ProbeUpdateDto("RRMM", planet.getId());

        var probeUpdateResponse = service.updateProbe(probe.getId(), probeUpdateDto);

        Assertions.assertThat(probeUpdateResponse.getStatus()).isEqualTo(StatusProbe.ACTIVE);
        Assertions.assertThat(probeUpdateResponse.getDirection()).isEqualTo(DirectionProbe.NORTH);
    }

    @DisplayName("Update probe that is wandering through space to a planet")
    @Test
    public void updateProbeExistingCollisionWithAnotherProbe() {

        var probe2 = new Probe();
        probe2.setId("41829c9682e347290182e3480c30004124");
        probe2.setName("probe8");
        probe2.setStatus(StatusProbe.ACTIVE);
        probe2.setEditedAt(LocalDateTime.now());
        probe2.setCreatedAt(LocalDateTime.now());
        probe2.setPlanet(probe.getPlanet());
        probe2.setPositionX(2);
        probe2.setPositionY(3);

        given(repository.save(probe)).willReturn(probe);
        given(repository.save(probe2)).willReturn(probe2);
        probe.getPlanet().setProbes(of(probe, probe2));

        given(repository.findById(probe.getId())).willReturn(Optional.of(probe));
        given(planetService.getPlanetById(probe.getPlanet().getId())).willReturn(probe.getPlanet());

        var messagePlanet = String.format("Houston we had a problem!...probe collided with the %s, and it left the planet %s and is wandering through space",
                probe2.getName(), probe.getPlanet().getName());
        this.probeUpdateResponseDto = new ProbeUpdateResponseDto(
                probe.getId(),
                probe.getName(),
                probe.getStatus(),
                probe.getDirection(),
                probe.getPositionX(),
                probe.getPositionY(),
                messagePlanet);
        given(converter.toProbeUpdateResponseDto(probe, messagePlanet)).willReturn(probeUpdateResponseDto);

        var probeUpdateDto = new ProbeUpdateDto("RRMM", probe.getPlanet().getId());

        var probeUpdateResponse = service.updateProbe(probe.getId(), probeUpdateDto);

        Assertions.assertThat(probeUpdateResponse.getStatusPlanet().contains("collided")).isEqualTo(Boolean.TRUE);
    }
}
