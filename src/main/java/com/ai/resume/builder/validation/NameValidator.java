package com.ai.resume.builder.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class NameValidator implements ConstraintValidator<ValidName, String>  {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(value) && value.matches("^[A-Za-zÀ-ÖØ-öø-ÿ'’\\- ]+$");
    }
}
