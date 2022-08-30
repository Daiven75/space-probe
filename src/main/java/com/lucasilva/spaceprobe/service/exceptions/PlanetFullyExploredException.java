package com.lucasilva.spaceprobe.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class PlanetFullyExploredException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = 903129312L;

	public PlanetFullyExploredException(String msg) {
		super(msg);
	}
}