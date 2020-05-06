package no.cardwallet.card.AppUser;

import no.cardwallet.card.GiftCard.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AppUserController {

    @Autowired
    private JavaMailSender javaMailSender;

    final
    PasswordEncoder passwordEncoder;

    final
    GiftCardRepository giftCardRepository;

    final
    AppUserRepository appUserRepository;

    public AppUserController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, GiftCardRepository giftCardRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.giftCardRepository = giftCardRepository;
    }


    @GetMapping("/sign-up")
    public String signUp(@ModelAttribute AppUser appUser) {
        return "signUp";
    }


    @PostMapping("/save-user")
    public String validateUserByEmail(@ModelAttribute AppUser appUser, BindingResult bindingResult) throws MessagingException {
        AppUserValidator appUserValidator = new AppUserValidator();
        if (appUserValidator.supports(appUser.getClass())) {
            appUserValidator.validate(appUser, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "signUp";
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        generateLoginToken(appUser);

        appUserRepository.save(appUser);

        sendActivationLink(appUser.getEmail(), appUser.getLoginToken());

        return "registration";
    }

    private void generateLoginToken(@ModelAttribute AppUser appUser) {
        StringBuilder stringBuilder = new StringBuilder();
        int[] loginTokenArray = new int[20];
        for (int i = 0; i < loginTokenArray.length; i++) {
            loginTokenArray[i] = (int) (Math.random() * 10);
            stringBuilder.append(loginTokenArray[i]);
        }
        String loginToken = stringBuilder.toString();
        appUser.setLoginToken(loginToken);
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
        AppUser appUser = appUserRepository.findByLoginToken(loginToken);
        appUser.setIsActive(true);
        appUserRepository.save(appUser);
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
        if (appUserRepository.findAppUserByEmail(email) != null) {
            AppUser appUser = appUserRepository.findAppUserByEmail(email);
            generateLoginToken(appUser);
            appUserRepository.save(appUser);
            sendForgotPasswordLink(email, appUser.getLoginToken());
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
    public String setNewPassword(@PathVariable String loginToken, @ModelAttribute AppUser appUser) {
        return "resetPassword";
    }



    @PostMapping("/save-new-password/{loginToken}")
    public String saveNewPassword(Model model, @PathVariable String loginToken, @ModelAttribute AppUser appUser, BindingResult bindingResult) {
        AppUser dataBaseAppUser = appUserRepository.findByLoginToken(loginToken); //use email??
        AppUserValidator appUserValidator = new AppUserValidator();
        appUserValidator.validate(appUser, bindingResult);

        dataBaseAppUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(dataBaseAppUser);
        model.addAttribute("loginToken", loginToken);
        return "login";
    }


    @GetMapping("/settings")
    public String userSettings(@ModelAttribute AppUser appUser) {
        return "userSettings";
    }


    @GetMapping("/change-email")
    public String changeEmail(Model model, Principal principal) {
        getAppUserByEmailAddModelAttribute(model, principal);
        return "changeEmail";
    }

    private void getAppUserByEmailAddModelAttribute(Model model, Principal principal) {
        String email = principal.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        model.addAttribute(appUser);
    }


    @PostMapping("/save-changed-email")
    public String saveChangedEmail(Model model, Principal principal, @ModelAttribute AppUser appUserPosting, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String email = principal.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        model.addAttribute(appUser);
        appUser.setEmail(appUserPosting.getEmail());
        appUserRepository.save(appUser);
        new SecurityContextLogoutHandler().logout(httpRequest, httpResponse, SecurityContextHolder.getContext().getAuthentication());
        return "successfullyChangedEmail";
    }


    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        getAppUserByEmailAddModelAttribute(model, principal);
        return "changePassword";
    }


    @PostMapping("/save-changed-password")
    public String saveChangedPassword(Model model, Principal principal, @ModelAttribute AppUser appUserPosting, BindingResult bindingResult) {
        String email = principal.getName();
        AppUser appUser = appUserRepository.findByEmail(email);

        AppUserValidator appUserValidator = new AppUserValidator();
        if (appUserValidator.supports(appUser.getClass())) {
            appUserValidator.validateRepeatPassword(appUserPosting.getPassword(), appUserPosting.getRepeatPassword(), bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "changePassword";
        }
        appUser.setPassword(passwordEncoder.encode(appUserPosting.getPassword()));
        appUserRepository.save(appUser);
        model.addAttribute(appUser);
        return "successfullyChangedPassword";
    }


    @GetMapping("/want-to-delete-account")
    public String sureYouWantToDeleteAccount() {
        return "deleteAccount";
    }


    @Transactional
    @GetMapping("/delete-app-user")
    public String deleteAppUser(Principal principal) {
        String email = principal.getName();
        Long appUserId = appUserRepository.findByEmail(email).getId();
        giftCardRepository.deleteByAppUserId(appUserId);
        appUserRepository.deleteAppUserByEmail(email);
        return "redirect:/sign-up";
    }

}
