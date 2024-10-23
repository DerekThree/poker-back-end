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

import static games.poker.constants.PokerConstants.INVALID_HAND;

@Component
public class PrimaryAnalysisProcessor implements Processor {

    @Override
    // Checks if the hand has no duplicates, groups cards of the same value, and picks the high card
    public void process(Exchange exchange) {
        PokerDto pokerDto = exchange.getIn().getBody(PokerDto.class);
        Hand hand = pokerDto.getHand();

        // Check if the hand is valid
        int numberOfUniqueCards = new HashSet<>(hand).size();
        if (numberOfUniqueCards != hand.size() || hand.isEmpty()) {
            pokerDto.setResponse(INVALID_HAND);
            return;
        }

        // Group cards by value and pick the high card
        for (Card card : hand) {
            List<Card> listOfCardsWithThisValue = hand.getCardsByValue().computeIfAbsent(card.getValue(), k -> new ArrayList<>());
            listOfCardsWithThisValue.add(card);
            if (card.isHigherThan(hand.getHighCard())) {
                hand.setHighCard(card);
            }
        }
    }
}
