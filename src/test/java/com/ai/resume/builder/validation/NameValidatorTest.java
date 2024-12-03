package com.ai.resume.builder.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class NameValidatorTest {

    private NameValidator nameValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        nameValidator = new NameValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validName_returnsTrue() {
        String validName = "John Doe";
        assertTrue(nameValidator.isValid(validName, context));
    }

    @Test
    void isValid_validNameWithAccents_returnsTrue() {
        String validNameWithAccents = "Élise Dupont";
        assertTrue(nameValidator.isValid(validNameWithAccents, context));
    }

    @Test
    void isValid_validNameWithHyphen_returnsTrue() {
        String validNameWithHyphen = "Anne-Marie";
        assertTrue(nameValidator.isValid(validNameWithHyphen, context));
    }

    @Test
    void isValid_validNameWithApostrophe_returnsTrue() {
        String validNameWithApostrophe = "O'Connor";
        assertTrue(nameValidator.isValid(validNameWithApostrophe, context));
    }

    @Test
    void isValid_emptyName_returnsFalse() {
        String emptyName = "";
        assertFalse(nameValidator.isValid(emptyName, context));
    }

    @Test
    void isValid_nullName_returnsFalse() {
        assertFalse(nameValidator.isValid(null, context));
    }

    @Test
    void isValid_nameWithSpecialCharacters_returnsFalse() {
        String invalidNameWithSpecialChars = "John@Doe!";
        assertFalse(nameValidator.isValid(invalidNameWithSpecialChars, context));
    }

    @Test
    void isValid_nameWithNumbers_returnsFalse() {
        String invalidNameWithNumbers = "John123";
        assertFalse(nameValidator.isValid(invalidNameWithNumbers, context));
    }

    @Test
    void isValid_nameWithLeadingTrailingSpaces_returnsTrue() {
        String validNameWithSpaces = "   John Doe   ";
        assertTrue(nameValidator.isValid(validNameWithSpaces.trim(), context));
    }

    @Test
    void isValid_nameWithMultipleSpaces_returnsTrue() {
        String validNameWithMultipleSpaces = "John   Doe";
        assertTrue(nameValidator.isValid(validNameWithMultipleSpaces, context));
    }
}
