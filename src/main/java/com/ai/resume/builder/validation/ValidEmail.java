package com.ai.resume.builder.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Valid email.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {
    /**
     * Message string.
     *
     * @return the string
     */
// The `String message() default "Invalid Email";` is defining the default error message that will
    // be displayed if the email validation fails. In this case, the default error message is "Invalid
    // Email".
    String message() default "Invalid Email Format.";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
// The `Class<?>[] groups() default {};` is defining an array of group classes that the validation
    // constraint belongs to.
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
// The `Class<? extends Payload>[] payload() default {};` is defining an array of payload classes
    // that can be used to provide additional metadata about the validation constraint. The payload
    // classes must implement the `Payload` interface. This allows for more advanced validation
    // scenarios where additional information can be attached to the constraint.
    Class<? extends Payload>[] payload() default {};
}