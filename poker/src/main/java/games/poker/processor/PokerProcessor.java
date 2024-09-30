package games.poker.processor;

import games.poker.model.Card;
import games.poker.model.PokerDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PokerProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        PokerDto pokerDto =  exchange.getIn().getBody(PokerDto.class);
        Card[] request = pokerDto.getRequest();

        Map<String, List<Card>> cardsByValue = new HashMap<>();
        Card highestCard = null;
        for (Card card : request) {
            List<Card> listOfCardsWithThisValue = cardsByValue.computeIfAbsent(card.getValue(), k -> new ArrayList<>());
            listOfCardsWithThisValue.add(card);
            if (card.isHigherThan(highestCard)) {
                highestCard = card;
            }
        }

        pokerDto.setResponse("Highest card: " + highestCard);
    }
}
