package com.example.ecommerce.customer;

import com.example.ecommerce.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository repository;
    @Mock
    private CustomerMapper mapper;

    @Test
    public void createCustomerShouldReturnCustomerId() {
        // Given
        Address address = new Address("Street", "12", "12-345");
        CustomerRequest request = new CustomerRequest(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        Customer customer = new Customer(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        Mockito.when(mapper.mapCustomerRequestToCustomer(request)).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);

        // When
        CustomerService service = new CustomerService(repository, mapper);
        String result = service.createCustomer(request);

        // Then
        Assertions.assertEquals(request.id(), result);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Customer.class));
        Mockito.verify(mapper, Mockito.times(1)).mapCustomerRequestToCustomer(request);
    }

    @Test
    void updateCustomerShouldUpdateOnlyProvidedFields() {
        // Given
        Address address = new Address("Street", "12", "12-345");
        CustomerRequest request = new CustomerRequest(
                "1",
                "NewJohn",
                "Doe",
                null,
                null
        );
        Customer customer = new Customer(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        Mockito.when(repository.findById(request.id())).thenReturn(Optional.of(customer));

        // When
        CustomerService service = new CustomerService(repository, mapper);
        service.updateCustomer(request);

        // Then
        Mockito.verify(repository).findById(request.id());
        Mockito.verify(repository).save(customer);

        Assertions.assertEquals(request.id(), customer.getId());
        Assertions.assertEquals(request.firstName(), customer.getFirstName());
        Assertions.assertEquals(request.lastName(), customer.getLastName());
        Assertions.assertNotEquals(request.email(), customer.getEmail());
        Assertions.assertNotEquals(request.address(), customer.getAddress());
    }

    @Test
    void updateCustomerProvidedWithNonExistingCustomerShouldThrowException() {
        // Given
        Address address = new Address("Street", "12", "12-345");
        CustomerRequest request = new CustomerRequest(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        CustomerService service = new CustomerService(repository, mapper);
        Mockito.when(repository.findById("1")).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(CustomerNotFoundException.class, () -> service.updateCustomer(request));
        Mockito.verify(repository).findById("1");
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Customer.class));
    }

    @Test
    void findAllCustomers_ShouldReturnAllCustomers() {
        // Given
        Address address = new Address("Street", "12", "12-345");
        Customer customer = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .address(address)
                .build();
        CustomerResponse response = new CustomerResponse(
                "1",
                "John",
                "Doe",
                "john.doe@test.com",
                address
        );
        List<Customer> customers = Arrays.asList(customer);
        Mockito.when(repository.findAll()).thenReturn(customers);
        Mockito.when(mapper.mapCustomerToCustomerResponse(customer)).thenReturn(response);

        // When
        CustomerService service = new CustomerService(repository, mapper);
        List<CustomerResponse> result = service.findAllCustomers();

        // Then
        Assertions.assertEquals(customers.size(), result.size());
        Assertions.assertEquals(response, result.get(0));
        Mockito.verify(repository).findAll();
        Mockito.verify(mapper).mapCustomerToCustomerResponse(customer);
    }
}
