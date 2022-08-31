package com.lucasilva.spaceprobe.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasilva.spaceprobe.dto.PlanetAllDataDto;
import com.lucasilva.spaceprobe.dto.PlanetDto;
import com.lucasilva.spaceprobe.dto.PlanetResponseDto;
import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.service.PlanetService;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlanetResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetService service;

    private Planet planet;

    private PlanetAllDataDto planetAllDataDto;

    private PlanetDto planetDto;

    private PlanetResponseDto planetResponseDto;

    @BeforeEach
    void init() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId("41829c9682f032bd0182f0330a540000");
        galaxy.setName("GalaxyA");

        this.planet = new Planet();
        planet.setId("41829c9682e5d9530182e5d96bf10000");
        planet.setName("PlanetA");
        planet.setSizeAreaPlanet(5);
        planet.setGalaxy(galaxy);

        this.planetAllDataDto = new PlanetAllDataDto(this.planet);

        this.planetDto = new PlanetDto("PlanetA", galaxy.getId());

        this.planetResponseDto = new PlanetResponseDto(
                planet.getId(),
                planet.getName(),
                planet.getSizeAreaPlanet(),
                planet.getGalaxy().getName());
    }

    @DisplayName("Discovery planet and save works properly")
    @Test
    public void savePlanetWithSucess() throws Exception {

        doReturn(planetResponseDto)
                .when(service)
                .discoveryPlanet(planetDto);

        this.mockMvc.perform(post("/planets")
                        .content(asJsonString(planetDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(planet.getId())))
                .andExpect(jsonPath("$.name", is(planet.getName())));
    }

    @Test
    public void getPlanetByIdWithSucess() throws Exception {
        doReturn(planetAllDataDto).when(service).getPlanetAllDataById(planet.getId());

        this.mockMvc.perform(get("/planets/41829c9682e5d9530182e5d96bf10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(planet.getName())));
    }

    @DisplayName("Get planet by id but not found")
    @Test
    public void getPlanetByIdThrowException() throws Exception {
        doThrow(new ObjectNotFoundException(ErroType.PLANET_NOT_FOUND.toString()))
                .when(service).getPlanetAllDataById("fsad3421432");

        this.mockMvc.perform(get("/planets/fsad3421432"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRB-0005")));
    }

    @DisplayName("getAllPlanets method")
    @Test
    public void getAllProbesWithSucess() throws Exception {

        var planetListResponse = of(planetResponseDto, planetResponseDto, planetResponseDto);

        doReturn(planetListResponse).when(service).getAllPlanets();

        this.mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
