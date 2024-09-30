package games.poker.controller;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/poker")
public class PokerController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/health")
    public ResponseEntity<String> statusCheck() {

        Map<String, String> inputDto = new HashMap<String, String>();
        try {
            producerTemplate.sendBody("direct:inputData", inputDto);
        } catch (CamelExecutionException ex) {
            System.out.println(ex.getExchange().getException().getMessage());
        }

        return ResponseEntity.ok(inputDto.get("key"));
    }
}
