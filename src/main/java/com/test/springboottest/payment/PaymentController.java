package com.test.springboottest.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public void makePayment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.chargeCard(paymentRequest.getPayment().getCustomerId(), paymentRequest);
    }

    @GetMapping("/{id}")
    public Payment findById(@PathVariable Long id) {
        return paymentService.findById(id);
    }

}
