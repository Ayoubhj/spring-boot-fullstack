package com.ayoubhj.customer;

import com.ayoubhj.exception.DuplicateResourceException;
import com.ayoubhj.exception.NoDataChangeException;
import com.ayoubhj.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // WHEN
        underTest.getAllCustomers();
        //THEN
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // WHEN
        Customer actual = underTest.getCustomerById(id);

        //THEN
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void WillThrowWhenGetCustomerById() {
        // GIVEN
        Long id = 10L;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

         assertThatThrownBy( () -> underTest.getCustomerById(id) )
                 .isInstanceOf(ResourceNotFoundException.class
                         ).hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void insertCustomer() {
        // GIVEN
        String email  = "ayoub@gmail.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(false);
        // WHEN
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("ayoub", email, 22);

        underTest.insertCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capture = customerArgumentCaptor.getValue();

        //THEN
        assertThat(capture.getId()).isEqualTo(null);
        assertThat(capture.getName()).isEqualTo(request.name());
        assertThat(capture.getEmail()).isEqualTo(request.email());
        assertThat(capture.getAge()).isEqualTo(request.age());

    }

    @Test
    void existEmailInsertCustomer() {
        // GIVEN
        String email  = "ayoub@gmail.com";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("ayoub", email, 22);

        when(customerDao.existPersonWithEmail(email)).thenReturn(true);
        // WHEN

        assertThatThrownBy( () -> underTest.insertCustomer(request) )
                .isInstanceOf(DuplicateResourceException.class
                ).hasMessage("email already exists");

        verify(customerDao,never()).insertCustomer(any());
    }
    
    @Test
    void deleteCustomer() {

        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomer(id);


        verify(customerDao).deleteCustomerById(id);

    }

    @Test
    void WillNotFoundDeleteCustomer() {

        Long id = 1L;

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        assertThatThrownBy( () -> underTest.deleteCustomer(id) )
                .isInstanceOf(ResourceNotFoundException.class
                ).hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDao,never()).deleteCustomerById(id);

    }

    @Test
    void canUpdateAllCustomersProperties() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alsks@gmail.com";

        UpdateRequest updateRequest = new UpdateRequest("alsks", newEmail , 23);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);


        // WHEN

        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void canUpdateOnlyCustomerName() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        UpdateRequest updateRequest = new UpdateRequest("azer", null , null );
        // WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alsks@gmail.com";

        UpdateRequest updateRequest = new UpdateRequest(null, newEmail , null);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);
        // WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        UpdateRequest updateRequest = new UpdateRequest(null, null , 25);

        // WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void UpdateNotFoundCustomer() {
        // GIVEN
        Long id = 1L;

        UpdateRequest updateRequest = new UpdateRequest(null, null , 25);

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        assertThatThrownBy( () -> underTest.updateCustomer(id,updateRequest) )
                .isInstanceOf(ResourceNotFoundException.class
                ).hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDao,never()).updateCustomer(any());

    }

    @Test
    void UpdateCustomerNotFoundId() {
        // GIVEN
        Long id = 1L;

        UpdateRequest updateRequest = new UpdateRequest(null, null , 25);

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        assertThatThrownBy( () -> underTest.updateCustomer(id,updateRequest) )
                .isInstanceOf(ResourceNotFoundException.class
                ).hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDao,never()).updateCustomer(any());

    }

    @Test
    void canUpdateOnlyCustomerEmailExists() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alsks@gmail.com";

        UpdateRequest updateRequest = new UpdateRequest(null, newEmail , null);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);
        // WHEN
        assertThatThrownBy( () -> underTest.updateCustomer(id,updateRequest) )
                .isInstanceOf(DuplicateResourceException.class
                ).hasMessage("email already exists");

        verify(customerDao,never()).updateCustomer(any());

    }

    @Test
    void canUpdateOnlyCustomerEmailExistsNoDataChange() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,"ayoub","ayoub@gmail.com",22
        );

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));



        UpdateRequest updateRequest = new UpdateRequest(customer.getName(), customer.getEmail() , customer.getAge());

        // WHEN
        assertThatThrownBy( () -> underTest.updateCustomer(id,updateRequest) )
                .isInstanceOf(NoDataChangeException.class
                ).hasMessage("no data changes found");

        verify(customerDao,never()).updateCustomer(any());

    }
}