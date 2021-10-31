package br.com.alura.bookstore.infra;

import br.com.alura.bookstore.dto.Http400Dto;
import br.com.alura.bookstore.dto.Http500Dto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<Http400Dto> handleStatus400(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(error ->
                new Http400Dto(error.getField(), error.getDefaultMessage())).collect(Collectors.toList());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Http500Dto handleStatus500(Exception ex, HttpServletRequest req) {
        return new Http500Dto(LocalDate.now(), ex.getClass().getName(), ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleStatus404() {
    }
}
