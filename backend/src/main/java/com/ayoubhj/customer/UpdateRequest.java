package com.ayoubhj.customer;

public record UpdateRequest(
        String name,
        String email,
        Integer age
) {
}
