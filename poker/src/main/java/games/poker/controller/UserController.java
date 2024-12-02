package games.poker.controller;

import games.poker.dto.processor.RegisterUserProcessorDto;
import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.dto.request.RegisterUserRequestDto;
import games.poker.dto.response.S3FilesResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestHeader("X-Username") String username,
                                               @RequestBody RegisterUserRequestDto request) {
        request.getProfile().setEmail(username);
        log.info("Received register user request: {}", request);
        var processorDto = RegisterUserProcessorDto.builder().request(request).build();

        try {
            producerTemplate.sendBody("direct:registerUser", processorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return processorDto.getResponse();
    }
}
