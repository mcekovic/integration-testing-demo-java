package com.igt.demo.betting.api;

import javax.persistence.*;

import com.igt.demo.betting.domain.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class BettingApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BettingApiExceptionHandler.class);

	@Override protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOGGER.error("Error processing request: " + request, ex);
		return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<String> handleUnknownException(Throwable th, WebRequest request) {
		LOGGER.error("Error processing request: " + request, th);
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(th.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		LOGGER.error("Entity not found: " + request, ex);
		return ResponseEntity.status(NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(BetPlacementException.class)
	public ResponseEntity<String> handleBetPlacementException(BetPlacementException ex, WebRequest request) {
		LOGGER.warn("Bet placement exception: " + request, ex);
		return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
	}
}
