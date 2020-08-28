package com.test.springboottest.utils;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class PhoneNumberValidator implements Predicate<String> {

    @Override
    public boolean test(String phoneNumber) {
        if (phoneNumber != null){
            return phoneNumber.startsWith("+44") && phoneNumber.length() == 13;
        }
        return false;
    }

}
