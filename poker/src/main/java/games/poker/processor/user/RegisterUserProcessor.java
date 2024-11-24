package games.poker.processor.user;

import games.poker.dto.processor.RegisterUserProcessorDto;
import games.poker.dto.request.RegisterUserRequestDto;
import games.poker.service.OktaService;
import games.poker.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegisterUserProcessor implements Processor {

    @Autowired
    OktaService oktaService;

    @Autowired
    S3Service s3Service;

    @Override
    public void process(Exchange exchange) throws Exception {
        RegisterUserProcessorDto processorDto = exchange.getIn().getBody(RegisterUserProcessorDto.class);
        RegisterUserRequestDto request = processorDto.getRequest();

        log.info("Processing register user request: {}", request);
        String username = request.getProfile().getEmail();
        ResponseEntity<String> response = ResponseEntity.ok().body(username + " created using IdP");
        if (request.getCredentials() != null) {
            response = oktaService.registerUser(request);
        }
        if (request.getCredentials() == null ||  response.getStatusCode().is2xxSuccessful()) {
            s3Service.createNewUserFiles(username);
        }
        processorDto.setResponse(response);
    }
}
