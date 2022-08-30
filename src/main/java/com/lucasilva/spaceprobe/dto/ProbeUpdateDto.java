package com.lucasilva.spaceprobe.dto;

import com.lucasilva.spaceprobe.annotations.CommandValidator;
import com.lucasilva.spaceprobe.annotations.UUID;

import javax.validation.constraints.NotEmpty;

public record ProbeUpdateDto(
        @NotEmpty(message = "Mandatory filling")
        @CommandValidator
        String commands,

        @UUID
        String planetId) { }
