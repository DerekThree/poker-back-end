package games.poker.processor;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.model.PokerDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecondaryAnalysisProcessor implements Processor {

    @Override
    // checks for straight and flush
    public void process(Exchange exchange) {
        Hand hand = exchange.getIn().getBody(PokerDto.class).getHand();

        hand.setStraight(true);
        int lowestStrengthForStraight = hand.getHighCard().getStrength() - 4;
        for (Card card : hand) {
            if (card.getStrength() < lowestStrengthForStraight) {
                hand.setStraight(false);
                break;
            }
        }

        hand.setFlush(true);
        for (int i = 0; i < hand.size() - 1; i++) {
            if (!hand.get(i).getSuit().equals(hand.get(i+1).getSuit())) {
                hand.setFlush(false);
                break;
            }
        }
    }
}
