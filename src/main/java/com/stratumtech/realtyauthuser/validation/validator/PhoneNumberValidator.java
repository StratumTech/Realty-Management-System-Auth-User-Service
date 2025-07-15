package com.stratumtech.realtyauthuser.validation.validator;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.stratumtech.realtyauthuser.validation.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private Pattern regex;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.regex = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return regex.matcher(value).matches();
    }
}

