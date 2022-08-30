package com.lucasilva.spaceprobe.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class GalaxyAlreadyExistsException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = 5432513L;

	public GalaxyAlreadyExistsException(String msg) {
		super(msg);
	}
}