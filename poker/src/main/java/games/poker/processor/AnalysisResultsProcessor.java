package games.poker.processor;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.model.PokerDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalysisResultsProcessor implements Processor {
    @Override
    public void process(Exchange exchange) {
        PokerDto pokerDto = exchange.getIn().getBody(PokerDto.class);
        Hand hand = pokerDto.getHand();

        StringBuilder sb = new StringBuilder();

        if (hand.isStraight()) sb.append("Straight ");
        if (hand.isFlush()) sb.append("Flush ");

        if (!hand.isStraight() && !hand.isFlush()) {
            String pair = null;
            String secondPair = null;
            String tripple = null;
            for (var entry : hand.getCardsByValue().entrySet()) {
                List<Card> cardsOfTheSameValue = entry.getValue();
                int size = cardsOfTheSameValue.size();

                switch (size) {
                    case 2 -> {
                        if (pair == null) pair = cardsOfTheSameValue.get(0).getValue();
                        else secondPair = cardsOfTheSameValue.get(0).getValue();
                    }
                    case 3 -> tripple = cardsOfTheSameValue.get(0).getValue();
                    case 4 -> sb.append("Four %ss ".formatted(cardsOfTheSameValue.get(0).getValue()));
                    default -> {/*empty*/}
                }

            }

            if (secondPair != null) sb.append("Two Pairs: %ss and %ss ".formatted(pair, secondPair));
            else if (tripple != null) sb.append(pair != null ?
                    "Full House: %ss over %ss".formatted(tripple, pair) :
                    "Three of a kind: %ss".formatted(tripple));
            else sb.append("Pair of %ss ".formatted(pair));
        }

        if (sb.toString().endsWith(" ")) sb.append("with %s high".formatted(hand.getHighCard()));
        if (sb.isEmpty()) sb.append("%s high".formatted(hand.getHighCard()));

        pokerDto.setResponse(sb.toString());
    }
}
