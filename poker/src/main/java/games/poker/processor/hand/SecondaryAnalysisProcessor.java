package games.poker.processor.hand;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.dto.request.HandRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecondaryAnalysisProcessor implements Processor {

    @Override
    // Checks for straight and flush
    public void process(Exchange exchange) {
        Hand hand = exchange.getIn().getBody(HandRequestDto.class).getRequest();

        log.debug("Checking for a straight");
        hand.setStraight(true);
        int lowestStrengthForStraight = hand.getHighCard().getStrength() - 4;
        for (Card card : hand) {
            if (card.getStrength() < lowestStrengthForStraight) {
                hand.setStraight(false);
                break;
            }
        }

        log.debug("Checking for a flush");
        hand.setFlush(true);
        for (int i = 0; i < hand.size() - 1; i++) {
            if (!hand.get(i).getSuit().equals(hand.get(i+1).getSuit())) {
                hand.setFlush(false);
                break;
            }
        }
    }
}
