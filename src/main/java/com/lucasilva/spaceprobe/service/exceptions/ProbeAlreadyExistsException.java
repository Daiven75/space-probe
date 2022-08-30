package com.lucasilva.spaceprobe.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class ProbeAlreadyExistsException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = 93128312L;

	public ProbeAlreadyExistsException(String msg) {
		super(msg);
	}
}