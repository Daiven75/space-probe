package com.lucasilva.spaceprobe.enums;

public enum ErroType {

	VALIDATION_ERRORS("PRB-0000", "Validation errors"),

	PROBE_ALREADY_EXISTS("PRB-0001", "Already exists probe with this name!"),

	PLANET_FULLY_EXPLORED("PRB-0002", "wow amazing, planet is being fully explored right now! Try another planet :)"),

	PROBE_NOT_FOUND("PRB-0003", "Probe not found"),

	PROBE_WANDERING_IN_SPACE("PRB-0004", "Your commands will have no effect as the probe is not on any planets at the moment"),

	PLANET_NOT_FOUND("PRB-0005", "Planet not found"),

	PLANET_ALREADY_EXISTS("PRB-0006", "Already exists planet with this name!"),

	GALAXY_ALREADY_EXISTS("PRB-0007", "Already exists galaxy with this name!"),

	GALAXY_NOT_FOUND("PRB-0008", "Galaxy not found"),
	;

	private final String code;
	private final String description;

	ErroType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return String.format("%s - %s", this.code, this.description);
	}
}