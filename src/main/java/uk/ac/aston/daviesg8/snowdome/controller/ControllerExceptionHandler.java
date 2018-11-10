package uk.ac.aston.daviesg8.snowdome.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.ac.aston.daviesg8.snowdome.model.exception.*;

/**
 * This controller advice class will intercept exceptions thrown at controller level and convert them into useful
 * information for the client.
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = ClientAlreadyExistsException.class)
    public ResponseEntity<String> handleClientAlreadyExistsException(ClientAlreadyExistsException e) {
        return new ResponseEntity<>("A client with that username already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ClientNotFoundException.class)
    public ResponseEntity<String> handleClientNotFoundException(ClientNotFoundException e) {
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ClientNotLoggedInException.class)
    public ResponseEntity<String> handleClientNotLoggedInException(ClientNotLoggedInException e) {
        return new ResponseEntity<>("You must be logged in to access that resource", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = LessonNotFoundException.class)
    public ResponseEntity<String> handleClientNotFoundException(LessonNotFoundException e) {
        return new ResponseEntity<>("Lesson not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MaximumSelectedLessonsExceededException.class)
    public ResponseEntity<String> handleMaximumSelectedLessonsExceededException(MaximumSelectedLessonsExceededException e) {
        return new ResponseEntity<>("Maximum number of selected lessons exceeded", HttpStatus.CONFLICT);
    }

}
