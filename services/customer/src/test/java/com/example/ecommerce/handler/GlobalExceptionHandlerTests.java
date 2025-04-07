package com.example.ecommerce.handler;

import com.example.ecommerce.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

public class GlobalExceptionHandlerTests {

    @Test
    void handleCustomerNotFoundExceptionShouldReturnNotFoundStatus() {
        // Given
        CustomerNotFoundException exception = new CustomerNotFoundException("Customer not found");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // When
        ResponseEntity<String> response = handler.handleCustomerNotFoundException(exception);

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleCustomerNotFoundExceptionShouldReturnExceptionMessage() {
        // Given
        CustomerNotFoundException exception = new CustomerNotFoundException("Customer not found");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // When
        ResponseEntity<String> response = handler.handleCustomerNotFoundException(exception);

        // Then
        Assertions.assertEquals(exception.getMessage(), response.getBody());
    }

    @Test
    void handleMethodArgumentNotValidExceptionShouldReturnBadRequestStatus() {
        // Given
        MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        FieldError fieldError = new FieldError("customerRequest", "email", "Email is required");

        Mockito.when(exception.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        // When
        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(exception);

        // Then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleMethodArgumentNotValidExceptionShouldReturnDetailedErrorInformation() {
        // Given
        MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        FieldError fieldError = new FieldError("customerRequest", "email", "Email is required");

        Mockito.when(exception.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        // When
        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(exception);

        // Then
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().errors().containsKey(fieldError.getField()));
        Assertions.assertTrue(response.getBody().errors().containsValue(fieldError.getDefaultMessage()));
    }
}
