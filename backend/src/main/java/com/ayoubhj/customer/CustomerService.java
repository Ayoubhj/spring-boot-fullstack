package com.ayoubhj.customer;

import com.ayoubhj.exception.DuplicateResourceException;
import com.ayoubhj.exception.NoDataChangeException;
import com.ayoubhj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
         return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Long id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
    }

    public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest){

            if(customerDao.existPersonWithEmail(customerRegistrationRequest.email())){
                  throw  new DuplicateResourceException("email already exists");
            }

            Customer customer =  new Customer(
                    customerRegistrationRequest.name(),
                    customerRegistrationRequest.email(),
                    customerRegistrationRequest.age()
            );

            customerDao.insertCustomer(customer);
    }


    public void deleteCustomer(Long id){

        if(!customerDao.existPersonWithId(id)){
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);

    }


    public void updateCustomer(Long id,UpdateRequest updateRequest){

        if(!customerDao.existPersonWithId(id)){
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }

        Customer customer = getCustomerById(id);

        boolean change = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            change = true;
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){

            if(customerDao.existPersonWithEmail(updateRequest.email())){
                throw  new DuplicateResourceException("email already exists");
            }

            customer.setEmail(updateRequest.email());
            change = true;

        }

        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            change = true;
        }


        if(!change){
            throw new NoDataChangeException("no data changes found");
        }

        customerDao.updateCustomer(customer);

    }

}
