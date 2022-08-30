package com.lucasilva.spaceprobe.enums;

import java.util.Arrays;
import java.util.Objects;

public enum StatusProbe {

    ACTIVE(0, "Active"),
    WALKING_IN_SPACE(1, "Walking in space"),
    OFF(2, "Off");

    private final int cod;
    private final String description;

    private StatusProbe(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public int getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static StatusProbe toEnum(Integer cod) {

        if(Objects.isNull(cod)) return null;

        return Arrays.stream(StatusProbe.values())
                .filter(status -> cod.equals(status.getCod()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + cod));
    }
}
