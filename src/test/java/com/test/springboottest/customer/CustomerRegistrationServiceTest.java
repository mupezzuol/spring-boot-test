package com.test.springboottest.customer;

import com.test.springboottest.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository, phoneNumberValidator);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Murillo", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number passed
        // -> when this code is called in the service, we force the return of this method to be "empty",
        // so that it falls into the condition of saving the object and does not fall into the logic of the IF
        // otherwise it will not go to DB, so we are mocking the data
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // ... Valid phone number
        given(phoneNumberValidator.validate(phoneNumber)).willReturn(Boolean.TRUE);

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture()); // capture the same value passed in call method 'request'
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue(); // get Customer used in request (custumer)
        assertThat(customerArgumentCaptorValue).isEqualTo(customer); // compare Customer with Customer passed in request (are the same object)
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Murillo", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... an existing customer is retuned (same customer)
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // ... Valid phone number
        given(phoneNumberValidator.validate(phoneNumber)).willReturn(Boolean.TRUE);

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should(never()).save(any());

        // Could use that way
        //then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
        //then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsTaken() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Murillo", phoneNumber);
        Customer customerTwo = new Customer(UUID.randomUUID(), "Neymar", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... Customer Two (same phoneNumber) passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));

        // ... Valid phone number
        given(phoneNumberValidator.validate(phoneNumber)).willReturn(Boolean.TRUE);

        // When + Then = Together this case
        // In that case it will fall into Exception where there is already a Customer with that same phone.
        // I did return 'customerTwo' and send in request 'customer', both are the same phoneNumber
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken", phoneNumber));

        // Finally
        then(customerRepository).should(never()).save(any(Customer.class)); // Can be empty: any()
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(null, "Murillo", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // ... Valid phone number
        given(phoneNumberValidator.validate(phoneNumber)).willReturn(Boolean.TRUE);

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        //compare the sentumer and the retrieved custumer before saving, but ignore the "id" field as NULL was sent and a new ID was generated in the method.
        assertThat(customerArgumentCaptorValue).isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();//It cannot be NULL, as an ID was generated in the method
    }

    @Test
    void itShouldSaveNewCustomerWhenPhoneNumberIsInvalid() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Murillo", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... Valid phone number
        given(phoneNumberValidator.validate(phoneNumber)).willReturn(Boolean.FALSE);

        // When
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is not valid", phoneNumber));

        // Then
        then(customerRepository).shouldHaveNoMoreInteractions();
    }
}