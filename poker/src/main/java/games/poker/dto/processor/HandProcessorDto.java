package games.poker.dto.processor;

import games.poker.model.Hand;
import lombok.Data;

@Data
public class HandProcessorDto {
    private Hand request;
    private String response;

}
