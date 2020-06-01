package no.cardwallet.card.User;

import no.cardwallet.card.Card.CardRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.security.Principal;


@Controller
public class UserController {

    private final JavaMailSender javaMailSender;

    final
    PasswordEncoder passwordEncoder;

    final
    CardRepository cardRepository;

    final
    UserRepository userRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, CardRepository cardRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardRepository = cardRepository;
        this.javaMailSender = javaMailSender;
    }


    @GetMapping("/sign-up")
    public String signUp(@ModelAttribute User user) {
        return "signUp";
    }


    @PostMapping("/save-user")
    public String validateUserByEmail(@ModelAttribute User user, BindingResult bindingResult) throws MessagingException {
        UserValidator userValidator = new UserValidator();
        if (userValidator.supports(user.getClass())) {
            userValidator.validate(user, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "signUp";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        generateLoginToken(user);

        userRepository.save(user);

        sendActivationLink(user.getEmail(), user.getLoginToken());

        return "registration";
    }

    private void generateLoginToken(@ModelAttribute User user) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int temp = (int) (Math.random() * 10);
            stringBuilder.append(temp);
        }
        user.setLoginToken(stringBuilder.toString());
    }

    public void sendActivationLink(String email, String loginToken) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        String subject = "Complete your wallit registration!";
        mimeMessageHelper.setSubject(subject);
        String loginLink = "http://localhost:8080/activate-user/" + loginToken;
        mimeMessageHelper.setText("Welcome to wallit! Click this link to complete your registration.\n" + loginLink,
                "<html><a href=\"" + loginLink + "\">Welcome to wallit! Click here to complete your registration.</a></html>");
        javaMailSender.send(mimeMessage);
    }


    @GetMapping("/activate-user/{loginToken}")
    public String activateUser(@PathVariable String loginToken) {
        User user = userRepository.findByLoginToken(loginToken);
        user.setIsActive(true);
        userRepository.save(user);
        return "login";
    }


    @GetMapping("/terms-and-conditions")
    public String termsAndConditions() {
        return "termsAndConditions";
    }


    @GetMapping("/login")
    public String userLogin() {
        return "login";
    }


    @GetMapping("/logout")
    public String logout() {
        return "login";
    }


    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgotPassword";
    }


    @PostMapping("/reset-password")
    public String sendResetPasswordLink(@RequestParam String email) throws MessagingException {
        if (userRepository.findByEmail(email) != null) {
            User user = userRepository.findByEmail(email);
            generateLoginToken(user);
            userRepository.save(user);
            sendForgotPasswordLink(email, user.getLoginToken());
        } else {
            return "redirect:/sign-up";
        }
        return "registration";
    }

    public void sendForgotPasswordLink(String email, String loginToken) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        String subject = "Reset your wallit password";
        mimeMessageHelper.setSubject(subject);
        String loginLink = "http://localhost:8080/set-new-password/" + loginToken;
        mimeMessageHelper.setText("Reset your wallit password now. Follow this link:\n" + loginLink,
                "<html><a href=\"" + loginLink + "\">Reset your wallit password now.</a></html>");
        javaMailSender.send(mimeMessage);
    }

    @GetMapping("/set-new-password/{loginToken}")
    public String setNewPassword(@PathVariable String loginToken, @ModelAttribute User user) {
        return "resetPassword";
    }


    @PostMapping("/save-new-password/{loginToken}")
    public String saveNewPassword(Model model, @PathVariable String loginToken, @ModelAttribute User user, BindingResult bindingResult) {
        User dBUser = userRepository.findByLoginToken(loginToken);
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);

        dBUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(dBUser);
        model.addAttribute("loginToken", loginToken);
        return "login";
    }


    @GetMapping("/settings")
    public String userSettings(@ModelAttribute User user) {
        return "userSettings";
    }


    @GetMapping("/change-email")
    public String changeEmail(Model model, Principal principal) {
        getUserByEmailAddModelAttribute(model, principal);
        return "changeEmail";
    }

    private void getUserByEmailAddModelAttribute(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute(user);
    }


    @PostMapping("/save-changed-email")
    public String saveChangedEmail(Model model, Principal principal, @ModelAttribute User userPosting, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute(user);
        user.setEmail(userPosting.getEmail());
        userRepository.save(user);
        new SecurityContextLogoutHandler().logout(httpRequest, httpResponse, SecurityContextHolder.getContext().getAuthentication());
        return "successfullyChangedEmail";
    }


    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        getUserByEmailAddModelAttribute(model, principal);
        return "changePassword";
    }


    @PostMapping("/save-changed-password")
    public String saveChangedPassword(Model model, Principal principal, @ModelAttribute User userPosting, BindingResult bindingResult) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        UserValidator userValidator = new UserValidator();
        if (userValidator.supports(user.getClass())) {
            userValidator.validateRepeatPassword(userPosting.getPassword(), userPosting.getRepeatPassword(), bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "changePassword";
        }
        user.setPassword(passwordEncoder.encode(userPosting.getPassword()));
        userRepository.save(user);
        model.addAttribute(user);
        return "successfullyChangedPassword";
    }


    @GetMapping("/want-to-delete-account")
    public String sureYouWantToDeleteAccount() {
        return "deleteAccount";
    }


    @Transactional
    @GetMapping("/delete-user")
    public String deleteUser(Principal principal) {
        String email = principal.getName();
        Long id = userRepository.findByEmail(email).getId();
        cardRepository.deleteByUserId(id);
        userRepository.deleteUserByEmail(email);
        return "redirect:/sign-up";
    }

}
