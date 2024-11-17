package games.poker.processor.hand;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.dto.request.HandRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static games.poker.constants.PokerConstants.INVALID_HAND;

@Slf4j
@Component
public class PrimaryAnalysisProcessor implements Processor {

    @Override
    // Checks if the hand has no duplicates, groups cards of the same value, and picks the high card
    public void process(Exchange exchange) {
        HandRequestDto handRequestDto = exchange.getIn().getBody(HandRequestDto.class);
        Hand hand = handRequestDto.getRequest();
        log.info("Processing hand: {}", hand);

        log.debug("Checking if the hand is valid");
        int numberOfUniqueCards = new HashSet<>(hand).size();
        if (numberOfUniqueCards != hand.size() || hand.isEmpty()) {
            handRequestDto.setResponse(INVALID_HAND);
            return;
        }

        log.debug("Grouping cards by value and picking the high card");
        for (Card card : hand) {
            List<Card> listOfCardsWithThisValue = hand.getCardsByValue().computeIfAbsent(card.getValue(), k -> new ArrayList<>());
            listOfCardsWithThisValue.add(card);
            if (card.isHigherThan(hand.getHighCard())) {
                hand.setHighCard(card);
            }
        }
    }
}
