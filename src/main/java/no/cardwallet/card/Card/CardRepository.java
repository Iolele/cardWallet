package no.cardwallet.card.Card;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long>{

    List<Card> findAllCardsByUserId(Long userId);

    Card findCardById(Long cardId);

    void deleteByUserId(Long userId);
}
