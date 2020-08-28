package com.test.springboottest.payment.stripe;

import com.test.springboottest.payment.CardPaymentCharge;
import com.test.springboottest.payment.CardPaymentCharger;
import com.test.springboottest.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger  {

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        return new CardPaymentCharge(Boolean.TRUE);
    }

}
