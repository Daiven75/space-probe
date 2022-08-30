package com.lucasilva.spaceprobe.service;

import com.lucasilva.spaceprobe.dto.GalaxyDto;
import com.lucasilva.spaceprobe.dto.GalaxyResponseDto;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.repository.GalaxyRepository;
import com.lucasilva.spaceprobe.service.exceptions.GalaxyAlreadyExistsException;
import com.lucasilva.spaceprobe.service.exceptions.ObjectNotFoundException;
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

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class GalaxyServiceTest {

    @Mock
    private GalaxyRepository repository;

    @InjectMocks
    private GalaxyService service;

    @Mock
    private CustomerConverter converter;

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

    @DisplayName("Discovered galaxy and its galaxy save works properly")
    @Test
    public void discoveryGalaxyWithSucess() {

        when(converter.toGalaxy(galaxyDto)).thenReturn(galaxy);

        when(repository.findByName(galaxy.getId())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.discoveryGalaxy(galaxyDto)).doesNotThrowAnyException();
    }

    @DisplayName("Try create galaxy, but already exists galaxy with same name")
    @Test
    public void createGalaxyButThrowExceptionBecauseAlreadyExists() {

        when(repository.save(galaxy)).thenReturn(any());

        when(repository.findByName("GalaxyA")).thenReturn(Optional.of(galaxy));

        Assertions.assertThatCode(() -> service.discoveryGalaxy(galaxyDto))
                .isInstanceOf(GalaxyAlreadyExistsException.class)
                .hasMessageContaining("PRB-0007");
    }

    @DisplayName("listAllGalaxy method")
    @Test
    public void findAllGalaxyWithSucess() {

        var galaxy2 = new Galaxy();
        galaxy2.setId("09f9e805-24dd-11ed-8f60-0242ac110002");
        galaxy2.setName("GalaxyB");

        given(repository.findAll()).willReturn(of(galaxy2, this.galaxy));

        var galaxyList = service.listAllGalaxy();

        Assertions.assertThat(galaxyList).isNotNull();
        Assertions.assertThat(galaxyList.size()).isEqualTo(2);
    }

    @DisplayName("listAllGalaxySummarized method")
    @Test
    public void findAllGalaxySummarizedWithSucess() {

        var galaxy2 = new Galaxy();
        galaxy2.setId("09f9e805-24dd-11ed-8f60-0242ac110002");
        galaxy2.setName("GalaxyB");

        given(repository.findAll()).willReturn(of(galaxy2, this.galaxy));

        var galaxyList = service.listAllGalaxySummarized();

        Assertions.assertThat(galaxyList).isNotNull();
        Assertions.assertThat(galaxyList.size()).isEqualTo(2);
    }

    @DisplayName("Get galaxy by id works properly")
    @Test
    public void getGalaxyByIdThenReturnGalaxyObject(){
        given(repository.findById(galaxy.getId())).willReturn(Optional.of(galaxy));

        var savedGalaxy = service.getGalaxyById(galaxy.getId());

        Assertions.assertThat(savedGalaxy).isNotNull();
    }

    @DisplayName("Get galaxy by id but not found")
    @Test
    public void getGalaxyByIdButThrowException(){
        given(repository.findById("fsad3421432")).willReturn(Optional.empty());

        Assertions.assertThatCode(() -> service.getGalaxyById("fsad3421432"))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("PRB-0008");
    }
}
