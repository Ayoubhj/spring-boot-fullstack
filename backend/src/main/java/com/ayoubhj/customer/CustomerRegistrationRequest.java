package com.ayoubhj.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
