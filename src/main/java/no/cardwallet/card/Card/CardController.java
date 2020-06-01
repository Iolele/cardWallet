package no.cardwallet.card.Card;

import no.cardwallet.card.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CardController {

    final
    CardRepository cardRepository;

    final
    UserRepository userRepository;

    public CardController(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    private Long getUserId(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email).getId();
    }

    private List<Card> getAllCards(Principal principal) {
        Long appUserId = getUserId(principal);
        return cardRepository.findAllCardsByUserId(appUserId);
    }

    private boolean checkPrincipalIsCardOwner( Long userId, Long cardId, Principal principal) {
        Long principalUserId = getUserId(principal);
        List<Card> cardList = cardRepository.findAllCardsByUserId(principalUserId);
        return !cardList.contains(cardRepository.findCardById(cardId)) || !principalUserId.equals(userId);
    }

    //  show all gift cards for logged in user by id
    @GetMapping("/my-cards")
    public String getAllCards(Model model, Principal principal) {
        List<Card> cardList = getAllCards(principal);
        boolean isExpired = false;
        for (Card card : cardList) {
            LocalDate expDate = (card.getExpiryDate().toLocalDate());
            if (LocalDate.now().isAfter(expDate)) {
                isExpired = true;
            }
            card.setExpired(isExpired);
            isExpired = false;
        }
        model.addAttribute("cardList", cardList);
        return "myCards";
    }


    @GetMapping("/show-card/{userId}/{cardId}")
    public String showCard(Model model, @PathVariable Long userId, @PathVariable Long cardId, Principal principal) {
        if (checkPrincipalIsCardOwner(userId, cardId, principal)) return "defaultView";
        Card card = cardRepository.findCardById(cardId);

        boolean isExpired = false;
        LocalDate expDate = (card.getExpiryDate().toLocalDate());
        if (LocalDate.now().isAfter(expDate)) {
            isExpired = true;
        }

        card.setExpired(isExpired);
        model.addAttribute("card", card);
        model.addAttribute("userId", userId);

        return "showCard";
    }


    @GetMapping("/add-card")
    public String addCard(Model model, Principal principal) {
        Long userId = getUserId(principal);
        model.addAttribute("userId", userId);
        model.addAttribute("card", new Card());

        return "addCard";
    }


    @PostMapping("/save-new-card")
    public String saveNewCard(@ModelAttribute Card card, Principal principal) {
        Long userId = getUserId(principal);
        card.setUserId(userId);
        cardRepository.save(card);

        return "redirect:/my-cards";
    }


    @GetMapping("/edit-card/{userId}/{cardId}")
    public String editCard(Model model, @PathVariable Long userId, @PathVariable Long cardId, Principal principal) {
        Card tempCard = new Card();
        model.addAttribute("tempCard", tempCard);

        if (checkPrincipalIsCardOwner(userId, cardId, principal)) return "defaultView";
        Card card = cardRepository.findById(cardId).get();

        model.addAttribute(card);

        return "editCard";
    }


    @PostMapping("/save-edited-card/{userId}/{cardId}")
    public String savEditedCard(@ModelAttribute Card card, @ModelAttribute Card tempCard, @PathVariable Long userId, @PathVariable Long cardId, Principal principal) {
        if (checkPrincipalIsCardOwner(userId, cardId, principal)) return "defaultView";
        if (cardId != null) {
            card.setId(cardId);
        }
        card.setUserId(getUserId(principal));
        card.setExpiryDate(cardRepository.findCardById(cardId).getExpiryDate());
        cardRepository.save(card);
        card.setBalanceInt(tempCard.getBalanceInt());

        return "redirect:/my-cards";
    }


    @GetMapping("/delete-card/{userId}/{cardId}")
    public String deleteCard(@PathVariable Long userId, @PathVariable Long cardId) {
        cardRepository.deleteById(cardId);
        return "redirect:/my-cards";
    }

}