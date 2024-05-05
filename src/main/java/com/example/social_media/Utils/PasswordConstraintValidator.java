package com.example.social_media.Utils;


import com.example.social_media.security.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<Rule> rules = new ArrayList<>();
        //Rule 1: Password length should be in between
        //8 and 16 characters
        rules.add(new LengthRule(8, 16));
        //Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());
        //Rule 3.a: At least one Upper-case character
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        //Rule 3.b: At least one Lower-case character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        //Rule 3.c: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        //Rule 3.d: At least one special character
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
        PasswordValidator validator = new PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(password));
        //If the password is valid
        if (result.isValid()) {
            return true;
        }
        //If the password is invalid
        context.disableDefaultConstraintViolation(); //disable the default message
        //build new constraint violation
        context.buildConstraintViolationWithTemplate(
                        String.join(",", validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}