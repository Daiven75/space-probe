package com.lucasilva.spaceprobe.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class ObjectNotFoundException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = 1983123L;

	public ObjectNotFoundException(String msg) {
		super(msg);
	}
}