package games.poker.service;

import games.poker.dto.request.RegisterUserRequestDto;
import org.springframework.http.ResponseEntity;

public interface OktaService {

    public ResponseEntity<String> registerUser(RegisterUserRequestDto request);
}
