package com.lucasilva.spaceprobe.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class PlanetAlreadyExistsException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = 14322412L;

	public PlanetAlreadyExistsException(String msg) {
		super(msg);
	}
}