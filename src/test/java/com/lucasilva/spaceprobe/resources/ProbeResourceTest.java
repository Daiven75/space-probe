package com.lucasilva.spaceprobe.resources;

import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasilva.spaceprobe.dto.ProbeResponseDto;
import com.lucasilva.spaceprobe.dto.ProbeUpdateDto;
import com.lucasilva.spaceprobe.dto.ProbeUpdateResponseDto;
import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import com.lucasilva.spaceprobe.service.ProbeService;
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

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class ProbeResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProbeService service;

    private Probe probe;

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

    @DisplayName("Create probe and save works properly")
    @Test
    public void saveProbeWithSucess() throws Exception {
        var probeDto = new ProbeResponseDto(
                probe.getId(),
                probe.getName(),
                probe.getStatus(),
                probe.getPlanet().getName());

        doReturn(probeResponseDto)
                .when(service)
                .createProbe(any());

        this.mockMvc.perform(post("/probes")
                        .content(asJsonString(probeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(probe.getId())))
                .andExpect(jsonPath("$.name", is(probe.getName())));
    }

    @Test
    public void getProbeByIdWithSucess() throws Exception {
        doReturn(probe).when(service).getProbeById(probe.getId());

        this.mockMvc.perform(get("/probes/41829c9682e347290182e3480c300000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(probe.getName())));
    }

    @DisplayName("Get probe by id but not found")
    @Test
    public void getProbeByIdThrowException() throws Exception {
        doThrow(new ObjectNotFoundException(ErroType.PROBE_NOT_FOUND.toString()))
                .when(service).getProbeById("fsad3421432");

        this.mockMvc.perform(get("/probes/fsad3421432"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRB-0003")));
    }

    @DisplayName("getAllProbes method")
    @Test
    public void getAllProbesWithSucess() throws Exception {

        var probeListResponse = of(probeResponseDto, probeResponseDto, probeResponseDto);

        doReturn(probeListResponse).when(service).getAllProbes();

        this.mockMvc.perform(get("/probes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @DisplayName("updateProbe method")
    @Test
    public void updateProbeWithSucess() throws Exception {

        var probeUpdateDto = new ProbeUpdateDto("RRRM", null);

        doReturn(probeUpdateResponseDto).when(service).updateProbe(probe.getId(), probeUpdateDto);

        this.mockMvc.perform(get("/probes/41829c9682e347290182e3480c300000")
                        .content(asJsonString(probeUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
