package no.cardwallet.card.GiftCard;

import org.springframework.data.repository.CrudRepository;

import java.security.Principal;
import java.util.List;

public interface GiftCardRepository extends CrudRepository<GiftCard, Long>{

    List<GiftCard> findGiftCardsByAppUserId(Long id);

    GiftCard findGiftCardById(Long id);

    void deleteByAppUserId(Principal principal);

    void findById(long cardId);
}
