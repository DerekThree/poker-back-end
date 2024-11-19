package games.poker.controller;

import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.dto.request.S3FilesRequestDto;
import games.poker.dto.response.S3FilesResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/v1/s3")
public class S3FilesController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping(value = "/getFiles")
    public ResponseEntity<S3FilesResponseDto> getFiles(@RequestBody S3FilesRequestDto request) {
        log.info("Received get files request: {}", request);
        request.setMethod("get");
        return process(request);
    }

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<S3FilesResponseDto> postFile(@RequestParam String username, @RequestParam MultipartFile file) {
        S3FilesRequestDto request = S3FilesRequestDto.builder()
                .username(username)
                .file(file)
                .build();
        log.info("Received upload file request: {}", request);
        request.setMethod("post");
        return process(request);
    }

    @PostMapping(value = "/deleteFile")
    public ResponseEntity<S3FilesResponseDto> deleteFile(@RequestBody S3FilesRequestDto request) {
        log.info("Received delete file request: {}", request);
        request.setMethod("delete");
        return process(request);
    }

    private ResponseEntity<S3FilesResponseDto> process(S3FilesRequestDto request) {
        var s3FilesProcessorDto = new S3FilesProcessorDto(request, new S3FilesResponseDto());

        try {
            producerTemplate.sendBody("direct:s3Files", s3FilesProcessorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(s3FilesProcessorDto.getResponse());
    }
}
