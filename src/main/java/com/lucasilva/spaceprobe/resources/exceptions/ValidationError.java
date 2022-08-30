package com.lucasilva.spaceprobe.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 64123019L;

	private final List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError(Integer status, String code, String details, Long timeStamp) {
		super(status, code, details, timeStamp);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
}