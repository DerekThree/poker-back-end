package games.poker.model;

import lombok.Data;

import java.util.Map;

@Data
public class Card {

    private String suit;
    private String value;
    private int strength = 0;
    private static final Map<String, Integer> suitStrengths = Map.of(
            "♣", 1,
            "♦", 2,
            "♠", 3,
            "♥", 4);

    private static final Map<String, Integer> valueStrengths = Map.of(
            "J", 11,
            "Q", 12,
            "K", 13,
            "A", 14);


    public String toString() {
        return suit + value;
    }

    public boolean isHigherThan(Card other) {
        if (other == null || getStrength() > other.getStrength()) {
            return true;
        }
        else if (getStrength() < other.getStrength()) {
            return false;
        }
        else {
            return suitStrengths.get(this.suit) > suitStrengths.get(other.suit);
        }
    }

    public int getStrength() {
        try {
            strength = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return valueStrengths.get(value);
        }
        return strength;
    }
}
