package com.test.springboottest.payment.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.test.springboottest.payment.CardPaymentCharge;
import com.test.springboottest.payment.CardPaymentCharger;
import com.test.springboottest.payment.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService implements CardPaymentCharger {

    RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
            .build();

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);

        try {
            Charge charge = Charge.create(params, requestOptions);
            return new CardPaymentCharge(charge.getPaid());
        } catch (StripeException e) {
            throw new IllegalStateException("Cannot make strip chage", e);
        }
    }
}
