package no.cardwallet.card;

import no.cardwallet.card.Card.CardRepository;
import no.cardwallet.card.User.UserDetailService;
import no.cardwallet.card.User.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CardWalletApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;


    @Test
    void contextLoads() {
    }


    @Test
    public void testFindUserIdByEmail() {
        Assert.assertEquals("geir karlsen's userId number is 1.", 1, userRepository.findByEmail("geir.karlsen@hotmail.com").getId().intValue());
        Assert.assertEquals("elise norvik's userId number is 2.", 2, userRepository.findByEmail("elise.norvik@hotmail.com").getId().intValue());
        Assert.assertEquals("nils nordmann's userId number is 3.", 3, userRepository.findByEmail("nils.nordmann@hotmail.com").getId().intValue());
    }

    @Test
    public void testFindEmailByLoginToken() {
        Assert.assertEquals("Login token 12345678901234567890 belongs to 'geir.karlsen@hotmail.com'' account", "geir.karlsen@hotmail.com", userRepository.findByLoginToken("12345678901234567890").getEmail());
        Assert.assertEquals("Login token 12345678901234567890 belongs to 'elise.norvik@hotmail.com'' account", "elise.norvik@hotmail.com", userRepository.findByLoginToken("09876543210897654321").getEmail());
        Assert.assertEquals("Login token 12345678901234567890 belongs to 'nils.nordmann@hotmail.com'' account", "nils.nordmann@hotmail.com", userRepository.findByLoginToken("11111222223333344444").getEmail());
    }

    @Test
    public void testFindCardById() {
        Assert.assertEquals("The card with the id number '1' is from Jernia.", "Jernia", cardRepository.findCardById(1L).getStoreName());
        Assert.assertEquals("The card with the id number '1' is from Clas Ohlsen.", "Clas Ohlson", cardRepository.findCardById(2L).getStoreName());
        Assert.assertEquals("The card with the id number '1' is from Intersport.", "Intersport", cardRepository.findCardById(5L).getStoreName());
    }

    @Test
    public void testFindAllCardsByUserId() {
        Assert.assertEquals("The user with id 1 has 3 cards.", 3, cardRepository.findAllCardsByUserId(1L).size());
        Assert.assertEquals("The user with id 2 has 3 cards.", 3, cardRepository.findAllCardsByUserId(2L).size());
        Assert.assertEquals("The user with id 3 has 5 cards.", 5, cardRepository.findAllCardsByUserId(3L).size());
    }

    @Autowired
    UserDetailService userDetailService;

    @Test
    public void test() {

    }


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSignUp() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/sign-up")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testActivateUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/activate-user/09876543210897654321")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void saveUserShouldNotAcceptInvalidPassword() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/save-user")
                .param("password", "password1")
        )
                .andExpect(status().is4xxClientError());
    }
}

