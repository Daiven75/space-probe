package com.lucasilva.spaceprobe.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record GalaxyDto(
        @NotEmpty(message = "Mandatory filling")
        @Size(max = 100, message = "Maximum 100 characters")
        String name) { }
