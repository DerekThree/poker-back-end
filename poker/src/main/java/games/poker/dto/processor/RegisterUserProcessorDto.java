package games.poker.dto.processor;

import games.poker.dto.request.RegisterUserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserProcessorDto {

    RegisterUserRequestDto request;

    ResponseEntity<String> response;
}
