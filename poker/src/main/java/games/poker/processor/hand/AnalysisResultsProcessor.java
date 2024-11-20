package games.poker.processor.hand;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.dto.processor.HandProcessorDto;
import games.poker.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static games.poker.constants.PokerConstants.*;

@Slf4j
@Component
public class AnalysisResultsProcessor implements Processor {

    @Autowired
    KafkaService kafkaService;

    // Creates a hand description
    @Override
    public void process(Exchange exchange) {
        HandProcessorDto handProcessorDto = exchange.getIn().getBody(HandProcessorDto.class);
        Hand hand = handProcessorDto.getRequest();
        StringBuilder handDesc = new StringBuilder();

        log.info("Creating the hand description");
        if (!hand.isSecondaryAnalysisNeeded()) {
            log.debug("Pair, 3, or 4 of a kind found");
            handDesc.append(getPrimaryResultsDescription(hand));
        }
        else if (hand.isStraight() && hand.isFlush() && hand.getHighCard().getValue().equals("A")) {
            log.debug("Royal flush found");
            handDesc.append(ROYAL_FLUSH.formatted(hand.get(0).getSuit()));
        }
        else {
            if (hand.isStraight()) {
                log.debug("Straight found");
                handDesc.append(STRAIGHT);
            }
            if (hand.isFlush()) {
                log.debug("Flush found");
                handDesc.append(FLUSH);
            }
        }

        if (handDesc.toString().endsWith(" ")) {
            log.debug("Appending high card");
            handDesc.append(WITH_HIGH_CARD.formatted(hand.getHighCard()));
        }
        if (handDesc.isEmpty()) {
            log.debug("Only high card");
            handDesc.append(NOTHING.formatted(hand.getHighCard()));
        }

        kafkaService.sendMessage(handDesc.toString());

        handProcessorDto.setResponse(handDesc.toString());
    }

    // Describes a pair, 3, or 4 of a kind
    private String getPrimaryResultsDescription(Hand hand) {
        String pair = null;
        String secondPair;
        String threeOfAKind = null;

        log.info("Describing grouped cards");
        for (var entry : hand.getCardsByValue().entrySet()) {
            List<Card> cardsOfTheSameValue = entry.getValue();
            int size = cardsOfTheSameValue.size();

            switch (size) {
                case 2 -> {
                    if (pair == null) {
                        log.debug("Pair found");
                        pair = cardsOfTheSameValue.get(0).getValue();
                    }
                    else {
                        log.debug("2 pairs found");
                        secondPair = cardsOfTheSameValue.get(0).getValue();
                        return TWO_PAIRS.formatted(pair, secondPair);
                    }
                }
                case 3 -> {
                    log.debug("3 of a kind found");
                    threeOfAKind = cardsOfTheSameValue.get(0).getValue();
                }
                case 4 -> {
                    log.debug("4 of a kind found");
                    return FOUR_OF_A_KIND.formatted(cardsOfTheSameValue.get(0).getValue());
                }
                default -> { /*empty*/ }
            }
        }

        if (threeOfAKind != null) {
            log.debug("Checking for full house");
            return pair != null ? FULL_HOUSE.formatted(threeOfAKind, pair) : THREE_OF_A_KIND.formatted(threeOfAKind);
        }

        log.debug("Only a pair found");
        return PAIR.formatted(pair);
    }
}
