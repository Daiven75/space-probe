package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.enums.StatusProbe;

public record ProbeResponseDto(String id, String name, StatusProbe status, String planetName) { }