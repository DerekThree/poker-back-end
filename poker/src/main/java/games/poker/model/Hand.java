package games.poker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class Hand extends ArrayList<Card> {
    private transient Map<String, List<Card>> cardsByValue = new HashMap<>();
    private transient Card highCard;
    private boolean straight;
    private boolean flush;
    private boolean valid;

    public boolean isSecondaryAnalysisNeeded() {
        return valid && cardsByValue.size() == this.size() ;
    }

    public Hand(List<Card> cards) {
        super(cards);
    }
}
