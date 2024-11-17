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

    @GetMapping
    public ResponseEntity<S3ResponseDto> getFiles(@RequestBody S3RequestDto request) {
        log.info("Received get files request: {}", request);
        return process(request);
    }

    @PostMapping
    public ResponseEntity<S3ResponseDto> postFile(@RequestBody S3RequestDto request) {
        log.info("Received post file request: {}", request);
        return process(request);
    }

    @DeleteMapping
    public ResponseEntity<S3ResponseDto> deleteFile(@RequestBody S3RequestDto request) {
        log.info("Received delete file request: {}", request);
        return process(request);
    }

    private ResponseEntity<S3ResponseDto> process(S3RequestDto request) {
        var s3ProcessorDto = new S3ProcessorDto(request, new S3ResponseDto());

        try {
            producerTemplate.sendBody("direct:s3Data", s3ProcessorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(s3ProcessorDto.getResponse());
    }
}
