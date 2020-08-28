package com.test.springboottest.utils;

import java.util.function.Predicate;

public class PhoneNumberValidator implements Predicate<String> {

    @Override
    public boolean test(String phoneNumber) {
        if (phoneNumber != null){
            return phoneNumber.startsWith("+44") && phoneNumber.length() == 13;
        }
        return false;
    }
}
