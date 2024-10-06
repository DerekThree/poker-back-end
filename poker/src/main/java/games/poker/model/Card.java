package games.poker.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Data
public class Card implements Serializable {

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
        if (other == null || this.getStrength() > other.getStrength()) return true;
        else if (this.getStrength() < other.getStrength()) return false;
        else return suitStrengths.get(this.suit) > suitStrengths.get(other.suit);
    }

    public int getStrength() {
        if (strength > 0) return strength;

        try {
            strength = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            strength = valueStrengths.get(value);
        }

        return strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit.equals(card.suit) && value.equals(card.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }
}
