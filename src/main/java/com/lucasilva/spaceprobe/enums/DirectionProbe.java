package com.lucasilva.spaceprobe.enums;

import java.util.Arrays;
import java.util.Objects;

public enum DirectionProbe {

    NORTH(0, "North"),

    EAST(1, "East"),

    SOUTH(2, "South"),

    WEST(3, "West");

    private final int cod;
    private final String description;

    private DirectionProbe(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public int getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static DirectionProbe toEnum(Integer cod) {

        if(Objects.isNull(cod)) return null;

        return Arrays.stream(DirectionProbe.values())
                .filter(direction -> cod.equals(direction.getCod()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + cod));
    }
}
