package games.poker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/poker")
public class PokerController {

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/health")
    public ResponseEntity<String> statusCheck() {
        return ResponseEntity.ok("Back end is UP");
    }
}
