package com.lucasilva.spaceprobe.service;

import static java.util.List.of;

import com.lucasilva.spaceprobe.dto.PlanetDto;
import com.lucasilva.spaceprobe.dto.PlanetResponseDto;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.repository.PlanetRepository;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
import com.lucasilva.spaceprobe.service.exceptions.PlanetAlreadyExistsException;
import com.lucasilva.spaceprobe.utils.CustomerConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PlanetServiceTest {

    @Mock
    private PlanetRepository repository;

    @InjectMocks
    private PlanetService service;

    @Mock
    private CustomerConverter converter;

    @Mock
    private GalaxyService galaxyService;

    private Planet planet;

    private PlanetDto planetDto;

    private PlanetResponseDto planetResponseDto;

    @BeforeEach
    void init() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId("09f9e805-24dd-11ed-8f60-0242ac110002");
        galaxy.setName("GalaxyA");

        this.planet = new Planet();
        planet.setId("41829c9682e5d9530182e5d96bf10000");
        planet.setName("PlanetA");
        planet.setSizeAreaPlanet(5);
        planet.setGalaxy(galaxy);

        this.planetDto = new PlanetDto("PlanetA", galaxy.getId());

        this.planetResponseDto = new PlanetResponseDto(
                planet.getId(),
                planet.getName(),
                planet.getSizeAreaPlanet(),
                planet.getGalaxy().getName());
    }

    @DisplayName("Discovered planet and its galaxy save works properly")
    @Test
    public void discoveryPlanetWithSucess() {

        when(converter.toPlanet(planetDto)).thenReturn(planet);

        when(galaxyService.listAllGalaxy()).thenReturn(of(planet.getGalaxy()));

        when(repository.findByName(planet.getId())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.discoveryPlanet(planetDto)).doesNotThrowAnyException();
    }

    @DisplayName("Discovered only the planet and its galaxy was randomly defined")
    @Test
    public void discoveryPlanetWithoutGalaxyDefinedButWorksProperly() {

        var planetDto = new PlanetDto("PlanetA", null);
        when(converter.toPlanet(planetDto)).thenReturn(planet);

        when(galaxyService.listAllGalaxy()).thenReturn(of(planet.getGalaxy()));

        when(repository.findByName(planet.getId())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.discoveryPlanet(planetDto)).doesNotThrowAnyException();
    }

    @DisplayName("Try create planet, but already exists planet with same name")
    @Test
    public void createPlanetButThrowExceptionBecauseAlreadyExists() {

        when(repository.save(planet)).thenReturn(any());

        when(repository.findByName("planetA")).thenReturn(Optional.of(planet));

        Assertions.assertThatCode(() -> service.discoveryPlanet(planetDto))
                .isInstanceOf(PlanetAlreadyExistsException.class)
                .hasMessageContaining("PRB-0006");
    }

    @DisplayName("getAllPlanets method")
    @Test
    public void findAllPlanetWithSucess() {

        var planet2 = new Planet();
        planet2.setId("41829c9682e5d9530182e5d96bf104234");
        planet2.setName("PlanetB");
        planet2.setSizeAreaPlanet(5);
        planet2.setGalaxy(planet.getGalaxy());

        given(repository.findAll()).willReturn(of(planet2, this.planet));

        var planetList = service.getAllPlanets();

        Assertions.assertThat(planetList).isNotNull();
        Assertions.assertThat(planetList.size()).isEqualTo(2);
    }

    @DisplayName("Get planet by id works properly")
    @Test
    public void getPlanetByIdThenReturnPlanetObject(){
        given(repository.findById(planet.getId())).willReturn(Optional.of(planet));

        var savedPlanet = service.getPlanetById(planet.getId());

        Assertions.assertThat(savedPlanet).isNotNull();
    }

    @DisplayName("Get planet by id but not found")
    @Test
    public void getPlanetByIdButThrowException(){
        given(repository.findById("fsad3421432")).willReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.getPlanetById("fsad3421432"))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("PRB-0005");
    }
}
