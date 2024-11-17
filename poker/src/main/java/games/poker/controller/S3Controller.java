package games.poker.controller;

import games.poker.dto.processor.S3ProcessorDto;
import games.poker.dto.request.S3RequestDto;
import games.poker.dto.response.S3ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/s3")
public class S3Controller {

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping("/{username}")
    public ResponseEntity<S3ResponseDto> getFiles(@PathVariable String username) {
        log.info("Received request for user: {}", username);

        S3ProcessorDto s3ProcessorDto = new S3ProcessorDto();
        s3ProcessorDto.setRequest(new S3RequestDto("get", username));

        try {
            producerTemplate.sendBody("direct:s3Data", s3ProcessorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(s3ProcessorDto.getResponse());
    }
}
