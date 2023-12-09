package com.ayoubhj.customer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerJPADataAccessServiceTest  {

    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJPADataAccessService(customerRepository);
    }


    @Test
    void selectAllCustomers() {
        // WHEN
         underTest.selectAllCustomers();
        //THEN
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // GIVEN
        Long id = 1l;
        // WHEN
        underTest.selectCustomerById(id);
        //THEN
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // GIVEN
        Customer customer = new Customer(
                1L,"ayoub","ayoub@gmail.com",22
        );
        // WHEN
        underTest.insertCustomer(customer);

        //THEN
        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomerById() {
        Long id = 1L;

        underTest.deleteCustomerById(id);
        //THEN
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(
                1L,"ayoub","ayoub@gmail.com",22
        );

        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithEmail() {
       String email = "ayoub@gmail.com";

       underTest.existPersonWithEmail(email);

       verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existPersonWithId() {
        Long id = 1L;

        underTest.existPersonWithId(id);

        verify(customerRepository).existsCustomerById(id);
    }
}