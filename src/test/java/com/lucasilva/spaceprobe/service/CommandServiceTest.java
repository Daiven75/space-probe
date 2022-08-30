package com.lucasilva.spaceprobe.service;

import com.lucasilva.spaceprobe.dto.ProbeUpdateDto;
import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.StatusProbe;
import com.lucasilva.spaceprobe.model.Galaxy;
import com.lucasilva.spaceprobe.model.Planet;
import com.lucasilva.spaceprobe.model.Probe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class CommandServiceTest {

    @InjectMocks
    private CommandService service;

    private Probe probe;

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
        probe.setPositionX(2);
        probe.setPositionY(3);
        probe.setEditedAt(LocalDateTime.now());
        probe.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void moveProbeWorksProperly() {
        String commands = "MMRRMMLLLL";

        service.moveProbe(probe, commands.toCharArray());

        Assertions.assertThat(probe.getDirection()).isEqualTo(DirectionProbe.SOUTH);
        Assertions.assertThat(probe.getPositionY()).isEqualTo(3);
    }
}
