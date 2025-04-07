package com.example.ecommerce.customer;

import com.example.ecommerce.exception.CustomerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    public static final String BASE_URI = "/api/v1/customer";
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Address address;
    private CustomerRequest request;
    private CustomerResponse response;

    @BeforeEach
    void setUp() {
        address = new Address("Street", "12", "12-345");
        request = new CustomerRequest(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        response = new CustomerResponse(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
    }

    @Test
    void createCustomerWithValidRequestShouldReturnStatusOk() throws Exception {
        // Given
        when(service.createCustomer(any(CustomerRequest.class))).thenReturn("1");

        // When & Then
        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(service).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void createCustomerWithInvalidRequestShouldReturnBadRequest() throws Exception {
        // Given
        CustomerRequest invalidRequest = new CustomerRequest(
                "1",
                null,
                null,
                "invalid-email",
                address
        );

        // When & Then
        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void updateCustomerWithValidRequestShouldReturnAccepted() throws Exception {
        // Given
        doNothing().when(service).updateCustomer(any(CustomerRequest.class));

        // When & Then
        mockMvc.perform(put(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(service).updateCustomer(any(CustomerRequest.class));
    }

    @Test
    void updateCustomerWhenCustomerNotFoundShouldReturnNotFound() throws Exception {
        // Given
        doThrow(new CustomerNotFoundException("Customer not found"))
                .when(service).updateCustomer(any(CustomerRequest.class));

        // When & Then
        mockMvc.perform(put(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));

        verify(service).updateCustomer(any(CustomerRequest.class));
    }

    @Test
    void findAllShouldReturnAllCustomers() throws Exception {
        // Given
        List<CustomerResponse> customers = Arrays.asList(response);
        when(service.findAllCustomers()).thenReturn(customers);

        // When & Then
        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(response.id())))
                .andExpect(jsonPath("$[0].firstname", is(response.firstname())))
                .andExpect(jsonPath("$[0].lastname", is(response.lastname())))
                .andExpect(jsonPath("$[0].email", is(response.email())));

        verify(service).findAllCustomers();
    }

    @Test
    void findByIdIfCustomerExistsShouldReturnCustomer() throws Exception {
        // Given
        when(service.findById("1")).thenReturn(response);

        // When & Then
        mockMvc.perform(get(BASE_URI + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.firstname", is(response.firstname())))
                .andExpect(jsonPath("$.lastname", is(response.lastname())))
                .andExpect(jsonPath("$.email", is(response.email())));

        verify(service).findById("1");
    }
}
