package Web.Player.SoundBar.Exceptions.Handlers;

import Web.Player.SoundBar.Exceptions.TokenMissingException;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Formats.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        StringBuilder errorMessage = new StringBuilder();

        for (FieldError error : result.getFieldErrors()) {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        }

        return new ResponseError(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectIsAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleConflictException(ObjectIsAlreadyExistException ex) {
        return new ResponseError(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TokenMissingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleMissingTokenException(TokenMissingException ex) {
        return new ResponseError(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleMissingObjectException(EntityNotFoundException ex) {
        return new ResponseError(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}