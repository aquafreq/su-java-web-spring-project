package com.example.english.exceptions.handler;

import com.example.english.data.model.response.ErrorResponseModel;
import com.example.english.exceptions.NoContentFound;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NoContentFound.class,
            UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseModel> handleException(Exception exception) {
        ErrorResponseModel errorResponseModel =
                new ErrorResponseModel()
                        .setMessage(exception.getMessage())
                        .setStatusCode(400);

        return ResponseEntity.status(errorResponseModel.getStatusCode()).body(errorResponseModel);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseModel> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        ErrorResponseModel errorResponseModel = new ErrorResponseModel().setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponseModel);

//        return new ResponseEntity<ResponseEntity>("Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
// dasa oprai gre6kata such username already exists, da se metka i da hodi w resta s taq gre6ka,
    // da se doopraat valdiaciite i da se commitne koda sled koeto trqbwa da se zapo4ne i poslednata
    //funkcionalnost koqto 6te poakzva kategorite i dumite i nai-veroqtno ]e moje da se maha i dobavq
    // ne6to tako, move i da se vkara twa trendex api prevda4,
    // no predi tva trea da se vidi za pra6tane na imeil i da se naprai interceptor s logger
    // koito da narpai o6te 1 stranica i schedul i nekvi takia ne6ta // TODO
}
