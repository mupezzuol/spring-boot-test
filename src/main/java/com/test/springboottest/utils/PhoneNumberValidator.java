package com.test.springboottest.utils;

import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator {

    public boolean validate(String phoneNumber) {
        if (phoneNumber != null){
            return phoneNumber.startsWith("+44") && phoneNumber.length() == 13;
        }
        return false;
    }

}
