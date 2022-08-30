package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.annotations.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record ProbeDto(
        @NotEmpty(message = "Mandatory filling")
        @Size(max = 100, message = "Maximum 100 characters")
        String name,

        @UUID
        String planetId) { }
