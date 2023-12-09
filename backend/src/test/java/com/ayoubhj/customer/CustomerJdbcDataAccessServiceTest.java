package com.ayoubhj.customer;

import com.ayoubhj.AbstractUnitTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerJdbcDataAccessServiceTest extends AbstractUnitTestContainer {

    private CustomerJdbcDataAccessService customerJdbcDataAccessService;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        customerJdbcDataAccessService = new CustomerJdbcDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID()
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        // WHEN
        List<Customer> actual = customerJdbcDataAccessService.selectAllCustomers();

        //THEN
        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void willReturnEmptySelectCustomerById() {

        Long id = -1L ;

        var actual = customerJdbcDataAccessService.selectCustomerById(id);

        assertThat(actual).isEmpty();

    }

    @Test
    void insertCustomer() {

        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void deleteCustomerById() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN

        customerJdbcDataAccessService.deleteCustomerById(id);

        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isEmpty();
        
    }



    @Test
    void updateCustomerName() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        var name = "foo";

        Customer update = new Customer();
        update.setId(id);
        update.setName(name);
        update.setEmail(customer.getEmail());
        update.setAge(customer.getAge());

        customerJdbcDataAccessService.updateCustomer(update);

        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(update.getId());
            assertThat(c.getName()).isEqualTo(update.getName()); // changed
            assertThat(c.getEmail()).isEqualTo(update.getEmail());
            assertThat(c.getAge()).isEqualTo(update.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN

        var email2 = "kale@gail.com";

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(email2);
        update.setAge(customer.getAge());
        update.setName(customer.getName());

        customerJdbcDataAccessService.updateCustomer(update);

        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(update.getId());
            assertThat(c.getName()).isEqualTo(update.getName());
            assertThat(c.getEmail()).isEqualTo(update.getEmail()); // changed
            assertThat(c.getAge()).isEqualTo(update.getAge());
        });
    }


    @Test
    void updateCustomerAge() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN


        Customer update = new Customer();
        update.setId(id);
        update.setAge(55);
        update.setEmail(customer.getEmail());
        update.setName(customer.getName());

        customerJdbcDataAccessService.updateCustomer(update);

        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(update.getId());
            assertThat(c.getName()).isEqualTo(update.getName());
            assertThat(c.getEmail()).isEqualTo(update.getEmail());
            assertThat(c.getAge()).isEqualTo(update.getAge()) ; // changed
        });
    }

    @Test
    void updateCustomerAllFields() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN


        Customer update = new Customer();
        update.setId(id);
        update.setAge(55);
        update.setName("karl");
        update.setEmail("karl@gmail.com");
        customerJdbcDataAccessService.updateCustomer(update);

        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(update.getId());
            assertThat(c.getName()).isEqualTo(update.getName()); // changed
            assertThat(c.getEmail()).isEqualTo(update.getEmail()); // changed
            assertThat(c.getAge()).isEqualTo(update.getAge()) ; // changed
        });
    }

    @Test
    void updateCustomerAllFieldsDoesntChange() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                .stream()
                .filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN

        customerJdbcDataAccessService.updateCustomer(customer);

        customer.setId(id);

        Optional<Customer> actual = customerJdbcDataAccessService.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customer.getId());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge()) ;
        });
    }

    @Test
    void existPersonWithEmail() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);


        // WHEN
        boolean actual = customerJdbcDataAccessService.existPersonWithEmail(customer.getEmail());


        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void willReturnFalseExistPersonWithEmail() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();


        // WHEN
        boolean actual = customerJdbcDataAccessService.existPersonWithEmail(email);


        //THEN
        assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithId() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName()
                ,email
                ,20);

        customerJdbcDataAccessService.insertCustomer(customer);

        Long id = customerJdbcDataAccessService.selectAllCustomers()
                  .stream()
                  .filter(c -> c.getEmail().equals(email))
                  .map(Customer::getId)
                  .findFirst()
                  .orElseThrow();


        // WHEN
        boolean actual = customerJdbcDataAccessService.existPersonWithId(id);


        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void willReturnFalseExistPersonWithId() {
        // GIVEN
        Long id = -1L;

        // WHEN
        boolean actual = customerJdbcDataAccessService.existPersonWithId(id);


        //THEN
        assertThat(actual).isFalse();
    }
}