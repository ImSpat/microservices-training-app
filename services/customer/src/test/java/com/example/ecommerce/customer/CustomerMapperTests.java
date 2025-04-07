package com.example.ecommerce.customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerMapperTests {

    private CustomerMapper mapper;
    private CustomerRequest request;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        this.mapper = new CustomerMapper();
        Address address = new Address("Street", "12", "12-345");
        this.request = new CustomerRequest(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        this.customer = new Customer(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
    }

    @Test
    public void mapCustomerRequestToCustomerShouldMapCorrectly() {
        // When
        Customer result = mapper.mapCustomerRequestToCustomer(request);

        // Then
        Assertions.assertEquals(request.id(), result.getId());
        Assertions.assertEquals(request.firstName(), result.getFirstName());
        Assertions.assertEquals(request.lastName(), result.getLastName());
        Assertions.assertEquals(request.email(), result.getEmail());
        Assertions.assertEquals(request.address(), result.getAddress());
    }

    @Test
    public void mapCustomerRequestToCustomerShouldReturnNullWhenCustomerRequestIsNull() {
        // When
        Customer result = mapper.mapCustomerRequestToCustomer(null);

        // Then
        Assertions.assertNull(result);
    }

    @Test
    public void mapCustomerToCustomerResponseShouldMapCorrectly() {
        // When
        CustomerResponse result = mapper.mapCustomerToCustomerResponse(customer);

        // Then
        Assertions.assertEquals(customer.getId(), result.id());
        Assertions.assertEquals(customer.getFirstName(), result.firstname());
        Assertions.assertEquals(customer.getLastName(), result.lastname());
        Assertions.assertEquals(customer.getEmail(), result.email());
        Assertions.assertEquals(customer.getAddress(), result.address());
    }

    @Test
    public void mapCustomerToCustomerResponseShouldReturnNullWhenCustomerIsNull() {
        // When
        CustomerResponse result = mapper.mapCustomerToCustomerResponse(null);

        // Then
        Assertions.assertNull(result);
    }
}
