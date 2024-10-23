package games.poker.processor;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.model.PokerDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

import static games.poker.constants.PokerConstants.*;

@Component
public class AnalysisResultsProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        PokerDto pokerDto = exchange.getIn().getBody(PokerDto.class);
        Hand hand = pokerDto.getHand();
        StringBuilder handDesc = new StringBuilder();

        if (!hand.isSecondaryAnalysisNeeded()) handDesc.append(getPrimaryResultsDescription(hand));
        else if (hand.isStraight() && hand.isFlush() && hand.getHighCard().getValue().equals("A"))
            handDesc.append(ROYAL_FLUSH.formatted(hand.get(0).getSuit()));
        else {
            if (hand.isStraight()) handDesc.append(STRAIGHT);
            if (hand.isFlush()) handDesc.append(FLUSH);
        }

        if (handDesc.toString().endsWith(" ")) handDesc.append(WITH_HIGH_CARD.formatted(hand.getHighCard()));
        if (handDesc.isEmpty()) handDesc.append(NOTHING.formatted(hand.getHighCard()));



        pokerDto.setResponse(handDesc.toString());
    }

    private String getPrimaryResultsDescription(Hand hand) {
        String pair = null;
        String secondPair = null;
        String threeOfAKind = null;
        for (var entry : hand.getCardsByValue().entrySet()) {
            List<Card> cardsOfTheSameValue = entry.getValue();
            int size = cardsOfTheSameValue.size();

            switch (size) {
                case 2 -> {
                    if (pair == null) pair = cardsOfTheSameValue.get(0).getValue();
                    else secondPair = cardsOfTheSameValue.get(0).getValue();
                }
                case 3 -> threeOfAKind = cardsOfTheSameValue.get(0).getValue();
                case 4 -> {
                    return FOUR_OF_A_KIND.formatted(cardsOfTheSameValue.get(0).getValue());
                }
                default -> { /*empty*/ }
            }
        }

        if (secondPair != null) return TWO_PAIRS.formatted(pair, secondPair);
        if (threeOfAKind != null) return pair != null ?
                FULL_HOUSE.formatted(threeOfAKind, pair) :
                THREE_OF_A_KIND.formatted(threeOfAKind);
        return PAIR.formatted(pair);
    }
}
