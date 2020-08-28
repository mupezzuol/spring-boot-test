package com.test.springboottest.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp(){
        underTest = new PhoneNumberValidator();
    }

    //In this way, we passed several parameters and received the method for running the test.
    @DisplayName("It should validate list of phoneNumber")
    @ParameterizedTest
    @CsvSource({
            "+447000000000,true",
            "+4470000000003,false",
            "447000000000,false",
            "null,false"
    })
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expected) {
        // When
        boolean isValid = underTest.validate(phoneNumber);

        // Then
        assertThat(isValid).isEqualTo(expected);
    }

    @Test
    @DisplayName("It should validate phoneNumber")
    void itShouldValidatePhoneNumber() {
        // Given
        String phoneNumber = "+447000000000";

        // When
        boolean isValid = underTest.validate(phoneNumber);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should fail when lenght is bigger than 13")
    void itShouldValidatePhoneNumberWhenIncorrectAndHasLenghtBiggenThan13() {
        // Given
        String phoneNumber = "+4470000000003";

        // When
        boolean isValid = underTest.validate(phoneNumber);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should fail when does not start with +")
    void itShouldValidatePhoneNumberWhenDoesNotStartWithPlusSign() {
        // Given
        String phoneNumber = "447000000000";

        // When
        boolean isValid = underTest.validate(phoneNumber);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should fail when is null")
    void itShouldValidatePhoneNumberWhenIsNull() {
        // Given
        // When
        boolean isValid = underTest.validate(null);

        // Then
        assertThat(isValid).isFalse();
    }
}
