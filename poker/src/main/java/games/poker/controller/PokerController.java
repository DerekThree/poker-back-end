package games.poker.controller;

import games.poker.model.Card;
import games.poker.model.PokerDto;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/poker")
public class PokerController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/hand")
    public ResponseEntity<String> process(@RequestBody Card[] body) {

        PokerDto pokerDto = new PokerDto();
        pokerDto.setRequest(body);

        try {
            producerTemplate.sendBody("direct:pokerData", pokerDto);
        } catch (CamelExecutionException ex) {
            System.out.println(ex.getExchange().getException().getMessage());
        }

        return ResponseEntity.ok(pokerDto.getResponse());
    }
}
