package com.lucasilva.spaceprobe.resources.exceptions;

import com.lucasilva.spaceprobe.enums.ErroType;
import com.lucasilva.spaceprobe.service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	private final static String CLASS_NAME = ResourceExceptionHandler.class.getName();

//	@Autowired
//	private LogUtils log;

	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public StandardError objectNotFound(HttpServletRequest request, ObjectNotFoundException ex) {
		var code = ex.getMessage().substring(0, 8);
		var details = ex.getMessage().substring(11);

//		log.error(
//				request.getMethod(),
//				request.getRequestURI(),
//				HttpStatus.NOT_FOUND.value(),
//				CLASS_NAME);

		return new StandardError(
				HttpStatus.NOT_FOUND.value(), 
				code,
				details,
				System.currentTimeMillis());
	}
	
	@ExceptionHandler({
			ProbeAlreadyExistsException.class,
			PlanetFullyExploredException.class,
			ProbeWanderingInSpaceException.class,
			PlanetAlreadyExistsException.class,
			GalaxyAlreadyExistsException.class
	})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public StandardError badRequestErrors(HttpServletRequest request, RuntimeException ex) {
		var code = ex.getMessage().substring(0, 8);
		var details = ex.getMessage().substring(11);

//		log.error(
//				request.getMethod(),
//				request.getRequestURI(),
//				HttpStatus.BAD_REQUEST.value(),
//				CLASS_NAME);

		return new StandardError(
				HttpStatus.BAD_REQUEST.value(),
				code,
				details,
				System.currentTimeMillis());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public StandardError validation(
			HttpServletRequest request,
			MethodArgumentNotValidException ex) {

		var err = new ValidationError(
				HttpStatus.BAD_REQUEST.value(),
				ErroType.VALIDATION_ERRORS.getCode(),
				ErroType.VALIDATION_ERRORS.getDescription(),
				System.currentTimeMillis());

//		log.error(
//				request.getMethod(),
//				request.getRequestURI(),
//				HttpStatus.BAD_REQUEST.value(),
//				CLASS_NAME);

		var errors = ex.getBindingResult().getFieldErrors();

		errors.forEach(e -> err.addError(
				e.getField(), e.getDefaultMessage()));

		return err;
	}
}