package com.stratumtech.realtyauthuser.validation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.stratumtech.realtyauthuser.validation.validator.TelegramTagValidator;

@Documented
@Constraint(validatedBy = TelegramTagValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface TelegramTag {

    String message() default "Invalid Telegram tag format (5–32 characters; letters, digits, and underscores)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        TelegramTag[] value();
    }
}

