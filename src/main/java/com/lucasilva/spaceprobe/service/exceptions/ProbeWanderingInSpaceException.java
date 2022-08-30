package com.lucasilva.spaceprobe.service.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class ProbeWanderingInSpaceException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = 641274218;

	public ProbeWanderingInSpaceException(String msg) {
		super(msg);
	}
}