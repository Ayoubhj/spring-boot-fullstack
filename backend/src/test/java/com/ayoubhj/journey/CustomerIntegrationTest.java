package com.ayoubhj.journey;

import com.ayoubhj.customer.Customer;
import com.ayoubhj.customer.CustomerRegistrationRequest;
import com.ayoubhj.customer.UpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    public static final Random RANDOM = new Random();
    private static final String URI = "/api/v1/customers";
    @Test
    void canRegisterACustomer(){
        // create registration
        Faker faker = new Faker();
        Name fackername = faker.name();
        String name = fackername.fullName();
        String email = fackername.lastName() + UUID.randomUUID() + "@course.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                        name,email,age
                );
        // send post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(name,email,age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        Long id = allCustomers.stream().
                filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        expected.setId(id);

       Customer actual = webTestClient.get()
                .uri(URI + "/{id}" , id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();

       assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void canDeleteCustomer() {
        // create registration
        Faker faker = new Faker();
        Name fackername = faker.name();
        String name = fackername.fullName();
        String email = fackername.lastName() + UUID.randomUUID() + "@course.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                        name,email,age
                );
        // send post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        Long id = allCustomers.stream().
                filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(URI + "/{id}" , id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted();

         webTestClient.get()
                .uri(URI + "/{id}" , id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();


    }
    
    @Test
    void canUpdateCustomerAllFields() {
        // create registration
        Faker faker = new Faker();
        Name fackername = faker.name();
        String name = fackername.fullName();
        String email = fackername.lastName() + UUID.randomUUID() + "@course.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                        name,email,age
                );
        // send post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        Long id = allCustomers.stream().
                filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();


        UpdateRequest updateRequest = new UpdateRequest("ayoub","ayoub@gmail.com" + RANDOM.nextInt(16,100), 29);
        // delete customer
        webTestClient.put()
                .uri(URI + "/{id}" , id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),UpdateRequest.class)
                .exchange()
                .expectStatus()
                .isAccepted();

        Customer expected = new Customer(id,updateRequest.name(),updateRequest.email(),updateRequest.age());

        Customer actual = webTestClient.get()
                .uri(URI + "/{id}" , id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();

        assertThat(actual).isEqualToComparingFieldByField(expected);

    }

    @Test
    void canUpdateCustomerNoChange() {
        // create registration
        Faker faker = new Faker();
        Name fackername = faker.name();
        String name = fackername.fullName();
        String email = fackername.lastName() + UUID.randomUUID() + "@course.com";
        int age = RANDOM.nextInt(1,100);
        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest(
                        name,email,age
                );
        // send post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        Long id = allCustomers.stream().
                filter( c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();


        UpdateRequest updateRequest = new UpdateRequest(name,email, age);
        // delete customer
        webTestClient.put()
                .uri(URI + "/{id}" , id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),UpdateRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();


    }
}
