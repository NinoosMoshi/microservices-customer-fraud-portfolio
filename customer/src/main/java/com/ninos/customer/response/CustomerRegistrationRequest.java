package com.ninos.customer.response;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
