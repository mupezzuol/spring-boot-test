package com.test.springboottest.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.springboottest.customer.Customer;
import com.test.springboottest.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

//it starts the application instead of testing separately
//To test endpoints, it is necessary to use this annotation, as it will go up the spring service to perform this test.
@SpringBootTest
@AutoConfigureMockMvc //Without this "auto configuration" it will not work
class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given a customer
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "Murillo", "+447000000000");

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // Customer Register
        ResultActions customerRegResultActions = mockMvc.perform(put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))));

        // ... Payment
        long paymentId = 1L;
        Payment payment = new Payment(
                paymentId,
                customerId,
                new BigDecimal("10.00"),
                Currency.BRL,
                "x0x0x0x0",
                "Zakat"
        );

        // ... Payment request
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        // ... When payment is sent
        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));

        // Then both customer registration and payment requests are 200 status code
        customerRegResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(status().isOk());

        // Payment is stored in db
        ResultActions paymentByIdResultActions = mockMvc.perform(get("/api/v1/payment/"+String.valueOf(paymentId)));
        paymentByIdResultActions.andExpect(status().isOk());

        // TODO: Ensure sms is delivered
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
