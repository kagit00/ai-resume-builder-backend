package com.ai.resume.builder.validation;

import com.ai.resume.builder.models.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PasswordValidator implements ConstraintValidator<ValidPassword, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (!user.isAuthTypeJwt()) return true;
        else {
            String password = user.getPassword();
            return !StringUtils.isEmpty(password) && password.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d).+$");
        }
    }
}
