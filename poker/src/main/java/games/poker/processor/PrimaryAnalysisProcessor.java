package games.poker.processor;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.model.PokerDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class PrimaryAnalysisProcessor implements Processor {

    @Override
    // checks if the hand has no duplicates, groups cards of the same value, and picks the high card
    public void process(Exchange exchange) {
        Hand hand = exchange.getIn().getBody(PokerDto.class).getHand();

        int numberOfUniqueCards = new HashSet<>(hand).size();
        hand.setValid(hand.size() == numberOfUniqueCards);
        if (!hand.isValid()) {
            return;
        }

        for (Card card : hand) {
            List<Card> listOfCardsWithThisValue = hand.getCardsByValue().computeIfAbsent(card.getValue(), k -> new ArrayList<>());
            listOfCardsWithThisValue.add(card);
            if (card.isHigherThan(hand.getHighCard())) {
                hand.setHighCard(card);
            }
        }
    }
}
