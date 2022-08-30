package com.lucasilva.spaceprobe.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasilva.spaceprobe.dto.GalaxyDto;
import com.lucasilva.spaceprobe.dto.GalaxyResponseDto;
import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.service.GalaxyService;
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
public class GalaxyResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GalaxyService service;

    private Galaxy galaxy;

    private GalaxyResponseDto galaxyResponseDto;

    private GalaxyDto galaxyDto;

    @BeforeEach
    void init() {
        this.galaxy = new Galaxy();
        galaxy.setId("09f9e805-24dd-11ed-8f60-0242ac110002");
        galaxy.setName("GalaxyA");

        this.galaxyResponseDto = new GalaxyResponseDto(galaxy.getId(), galaxy.getName());

        this.galaxyDto = new GalaxyDto(galaxy.getName());
    }

    @DisplayName("Discovery galaxy and save works properly")
    @Test
    public void saveGalaxyWithSucess() throws Exception {

        doReturn(galaxyResponseDto)
                .when(service)
                .discoveryGalaxy(galaxyDto);

        this.mockMvc.perform(post("/galaxies")
                        .content(asJsonString(galaxyDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(galaxy.getId())))
                .andExpect(jsonPath("$.name", is(galaxy.getName())));
    }

    @DisplayName("getGalaxyById method")
    @Test
    public void getGalaxyByIdWithSucess() throws Exception {
        doReturn(galaxy).when(service).getGalaxyById(galaxy.getId());

        this.mockMvc.perform(get("/galaxies/09f9e805-24dd-11ed-8f60-0242ac110002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(galaxy.getName())));
    }

    @DisplayName("Get galaxy by id but not found")
    @Test
    public void getGalaxyByIdThrowException() throws Exception {
        doThrow(new ObjectNotFoundException(ErroType.GALAXY_NOT_FOUND.toString()))
                .when(service).getGalaxyById("fsad3421432");

        this.mockMvc.perform(get("/galaxies/fsad3421432"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRB-0008")));
    }

    @DisplayName("getAllGalaxies method")
    @Test
    public void getAllGalaxiesWithSucess() throws Exception {

        var galaxyListResponse = of(galaxyResponseDto, galaxyResponseDto, galaxyResponseDto);

        doReturn(galaxyListResponse).when(service).listAllGalaxySummarized();

        this.mockMvc.perform(get("/galaxies"))
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
