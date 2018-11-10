package uk.ac.aston.daviesg8.snowdome.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ClientAlreadyExistsException extends Exception {
}
