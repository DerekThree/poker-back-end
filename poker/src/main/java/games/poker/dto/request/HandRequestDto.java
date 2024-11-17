package games.poker.dto.request;

import games.poker.model.Hand;
import lombok.Data;

@Data
public class HandRequestDto {
    private Hand request;
    private String response;

}
