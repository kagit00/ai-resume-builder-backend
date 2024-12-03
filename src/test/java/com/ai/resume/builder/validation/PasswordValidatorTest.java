package com.ai.resume.builder.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validPassword_returnsTrue() {
        String validPassword = "Password123!";
        assertTrue(passwordValidator.isValid(validPassword, context));
    }

    @Test
    void isValid_passwordWithUppercaseSpecialCharacterDigit_returnsTrue() {
        String validPassword = "Valid@123";
        assertTrue(passwordValidator.isValid(validPassword, context));
    }

    @Test
    void isValid_passwordWithLowercaseOnly_returnsFalse() {
        String password = "password";
        assertFalse(passwordValidator.isValid(password, context));
    }

    @Test
    void isValid_passwordWithNoSpecialCharacter_returnsFalse() {
        String password = "Password123";
        assertFalse(passwordValidator.isValid(password, context));
    }

    @Test
    void isValid_passwordWithNoDigit_returnsFalse() {
        String password = "Password!";
        assertFalse(passwordValidator.isValid(password, context));
    }

    @Test
    void isValid_passwordWithNoUppercase_returnsFalse() {
        String password = "password123!";
        assertFalse(passwordValidator.isValid(password, context));
    }

    @Test
    void isValid_emptyPassword_returnsFalse() {
        String emptyPassword = "";
        assertFalse(passwordValidator.isValid(emptyPassword, context));
    }

    @Test
    void isValid_nullPassword_returnsFalse() {
        assertFalse(passwordValidator.isValid(null, context));
    }

    @Test
    void isValid_passwordWithMultipleSpecialCharacters_returnsTrue() {
        String validPassword = "P@ssw@rd123!";
        assertTrue(passwordValidator.isValid(validPassword, context));
    }

    @Test
    void isValid_passwordWithWhitespace_returnsFalse() {
        String passwordWithWhitespace = "Password 123!";
        assertFalse(passwordValidator.isValid(passwordWithWhitespace, context));
    }
}
