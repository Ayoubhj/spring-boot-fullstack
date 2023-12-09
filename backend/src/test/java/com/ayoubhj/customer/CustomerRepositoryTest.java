package com.ayoubhj.customer;

import com.ayoubhj.AbstractUnitTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractUnitTestContainer {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        underTest.save(customer);


        // WHEN
        boolean actual = underTest.existsCustomerByEmail(email);

        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void ReturnFalseExistsCustomerByEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // WHEN
        boolean actual = underTest.existsCustomerByEmail(email);

        //THEN
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        underTest.save(customer);

        Long id = underTest.findAll()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        boolean actual = underTest.existsCustomerById(id);

        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void ReturnFalseExistsCustomerById() {
        // GIVEN

        Long id = -1L;

        // WHEN
        boolean actual = underTest.existsCustomerById(id);

        //THEN
        assertThat(actual).isFalse();
    }


}