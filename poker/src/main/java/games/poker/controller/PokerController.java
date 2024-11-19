package games.poker.controller;

import games.poker.model.Card;
import games.poker.model.Hand;
import games.poker.dto.processor.HandProcessorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/poker")
public class PokerController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping("/hand")
    public ResponseEntity<String> process(@RequestBody List<Card> body) {
        log.info("Received request: {}", body);

        HandProcessorDto handProcessorDto = new HandProcessorDto();
        handProcessorDto.setRequest(new Hand(body));

        try {
            producerTemplate.sendBody("direct:handData", handProcessorDto);
        } catch (CamelExecutionException ex) {
            log.error("Camel route exception: {}", ex.getExchange().getException().getMessage());
        }

        return ResponseEntity.ok(handProcessorDto.getResponse());
    }
}
