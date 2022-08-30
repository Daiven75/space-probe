package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.enums.DirectionProbe;
import com.lucasilva.spaceprobe.enums.StatusProbe;

public record ProbeUpdateResponseDto(
        String id,
        String name,
        StatusProbe status,
        DirectionProbe direction,
        Integer positionX,
        Integer positionY,
        String statusPlanet) { }
