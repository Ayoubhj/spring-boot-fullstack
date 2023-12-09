package com.ayoubhj;

import com.ayoubhj.customer.Customer;
import com.ayoubhj.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class SpringBootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
		return  args -> {
			Faker faker = new Faker();
			Random random = new Random();
			String firstname = faker.name().firstName().toLowerCase();
			String lastName = faker.name().lastName().toLowerCase();
			Customer customer = new Customer( firstname + " " + lastName ,
					firstname + "." + lastName +"@course.com",
					random.nextInt(16,99));
			customerRepository.save(customer);
		};
	}
}
