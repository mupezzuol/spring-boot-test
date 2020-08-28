package com.test.springboottest.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp(){
        underTest = new PhoneNumberValidator();
    }

    @Test
    @DisplayName("It should validate phoneNumber")
    void itShouldValidatePhoneNumber() {
        // Given
        String phoneNumber = "+447000000000";

        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should fail when lenght is bigger than 13")
    void itShouldValidatePhoneNumberWhenIncorrectAndHasLenghtBiggenThan13() {
        // Given
        String phoneNumber = "+4470000000003";

        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should fail when does not start with +")
    void itShouldValidatePhoneNumberWhenDoesNotStartWithPlusSign() {
        // Given
        String phoneNumber = "447000000000";

        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should fail when is null")
    void itShouldValidatePhoneNumberWhenIsNull() {
        // Given
        // When
        boolean isValid = underTest.test(null);

        // Then
        assertThat(isValid).isFalse();
    }
}
