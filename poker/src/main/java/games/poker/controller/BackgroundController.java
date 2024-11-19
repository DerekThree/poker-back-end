package games.poker.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import games.poker.dto.processor.BackgroundProcessorDto;
import games.poker.dto.request.BackgroundRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/background")
public class BackgroundController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping(value = "/getActive")
    public ResponseEntity<InputStreamResource> getActiveBackground(@RequestBody BackgroundRequestDto request) {
        log.info("Received get background request: {}", request);
        request.setMethod("get");

        var processorDto = BackgroundProcessorDto.builder().request(request).build();

        try {
            producerTemplate.sendBody("direct:backgroundData", processorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(processorDto.getResponse());
    }

    @PostMapping(value = "/setActive")
    public ResponseEntity<InputStreamResource> setActiveBackground(@RequestBody BackgroundRequestDto request) {
        log.info("Received set background request: {}", request);
        request.setMethod("put");

        var backgroundProcessorDto = BackgroundProcessorDto.builder().request(request).build();

        try {
            producerTemplate.sendBody("direct:backgroundData", backgroundProcessorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(backgroundProcessorDto.getResponse());
    }
}
