//package com.example.social_media.Utils;
//
//import jakarta.validation.ConstraintValidatorContext;
//
//public class PasswordValidatorImpl implements ConstraintValidator<ValidPassword, String> {
//
//    private static final String UPPERCASE_REGEX = "(.*[A-Z].*)";
//    private static final String LOWERCASE_REGEX = "(.*[a-z].*)";
//    private static final String DIGIT_REGEX = "(.*[0-9].*)";
//    private static final String SPECIAL_REGEX = "(.*[!@#$%^&*()_+-=|:<>?.{}\\/].*)";
//
//    @Override
//    public void initialize(ValidPassword constraintAnnotation) {
//    }
//
//    @Override
//    public boolean isValid(String password, ConstraintValidatorContext context) {
//        if (password == null) {
//            return false;
//        }
//        int length = password.length();
//        return length >= 8 && password.matches(UPPERCASE_REGEX) && password.matches(LOWERCASE_REGEX)
//                && password.matches(DIGIT_REGEX) && password.matches(SPECIAL_REGEX);
//    }
//}