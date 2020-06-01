package no.cardwallet.card.User;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        User appUser = (User) object;
        // validate email address;also by sending email to newly registered users, see AUController.
        ValidationUtils.rejectIfEmpty(errors, "email", "email.empty", "Email address is required.");
        //validate password
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Password is required.");
        validateUpperLowerDigitPassword(appUser.getPassword(), errors);
        validateRepeatPassword(appUser.getPassword(), appUser.getRepeatPassword(), errors);
    }

    public void validateUpperLowerDigitPassword(String password, Errors errors) {
        if (checkPasswordLength(password, errors)) return;
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
        Matcher matcher = pattern.matcher(password);
        boolean isValidPassword = matcher.find();

        if (!isValidPassword) {
            errors.rejectValue("password", "password.invalid", "Password must contain at least 1 uppercase & lowercase letter, " +
                    "1 digit, no special characters.");
        }
    }

    private boolean checkPasswordLength(String password, Errors errors) {
        if(password.length() < 8) {
            errors.rejectValue("password", "password.invalid", "Password too short.");
            return true;
        }
        return false;
    }

    public void validateRepeatPassword(String password, String repeatPassword, Errors errors) {
        if (!password.equals(repeatPassword)) {
            errors.rejectValue("repeatPassword", "password.notEqual", "Passwords not a match.");
        }
    }
}