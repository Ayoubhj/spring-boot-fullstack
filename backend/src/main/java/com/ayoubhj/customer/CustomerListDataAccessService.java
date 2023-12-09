package com.ayoubhj.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    private static  final List<Customer> customers ;

    static {
        customers = new ArrayList<>();
        Customer customer = new Customer(1L,"Ayoub","Ayoub@gmail.com",22);
        Customer customer1 = new Customer(2L,"Alya","alya@gmail.com",20);
        customers.add(customer);
        customers.add(customer1);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream()
                .filter( c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customer.setId(customers.get(customers.size() - 1).getId() + 1);
        customers.add(customer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.stream()
                .filter( c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(o -> customers.remove(o));
    }

    @Override
    public void updateCustomer(Customer customer) {
         customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch( c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existPersonWithId(Long id) {
        return customers.stream()
                .anyMatch( c -> c.getId().equals(id));
    }
}
