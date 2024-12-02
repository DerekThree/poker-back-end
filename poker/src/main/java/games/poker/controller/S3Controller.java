package games.poker.controller;

import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.dto.request.S3RequestDto;
import games.poker.dto.response.S3FilesResponseDto;
import games.poker.model.FileData;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/s3")
public class S3Controller {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping(value = "/getFiles")
    public ResponseEntity<List<FileData>> getFiles(@RequestHeader("X-Username") String username,
                                                   @RequestHeader("X-Is-Admin") String isAdmin,
                                                   @RequestBody S3RequestDto request) {
        log.info("Received get files request: {}", request);
        request.setMethod("get");
        request.setUsername(username);
        request.setIsAdmin(isAdmin != null && isAdmin.equals("true"));
        return process(request);
    }

    // Legacy method
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileData>> postFile(@RequestHeader("X-Username") String username,
                                                   @RequestParam MultipartFile file) {
        S3RequestDto request = S3RequestDto.builder()
                .username(username)
                .file(file)
                .build();
        log.info("Received upload file request: {}", request);
        request.setMethod("post");
        return process(request);
    }

    @PostMapping(value = "/getUploadUrl")
    public ResponseEntity<String> getUploadUrl(@RequestHeader("X-Username") String username,
                                               @RequestHeader("X-Is-Admin") String isAdmin,
                                               @RequestBody S3RequestDto request) {
        log.info("Received get upload url request: {}", request);
        request.setMethod("put");
        request.setUsername(username);
        request.setIsAdmin(isAdmin != null && isAdmin.equals("true"));

        var s3FilesProcessorDto = S3FilesProcessorDto.builder().request(request).build();
        try {
            producerTemplate.sendBody("direct:s3Files", s3FilesProcessorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(s3FilesProcessorDto.getUploadUrl());
    }

    @PostMapping(value = "/deleteFile")
    public ResponseEntity<List<FileData>> deleteFile(@RequestHeader("X-Username") String username,
                                                     @RequestHeader("X-Is-Admin") String isAdmin,
                                                     @RequestBody S3RequestDto request) {
        log.info("Received delete file request: {}", request);
        request.setMethod("delete");
        request.setUsername(username);
        request.setIsAdmin(isAdmin != null && isAdmin.equals("true"));
        return process(request);
    }

    private ResponseEntity<List<FileData>> process(S3RequestDto request) {
        var s3FilesProcessorDto = S3FilesProcessorDto.builder()
                .request(request)
                .build();

        try {
            producerTemplate.sendBody("direct:s3Files", s3FilesProcessorDto);
        } catch (Exception ex) {
            log.error("Camel route exception: {}", ex.getMessage());
        }

        return ResponseEntity.ok(s3FilesProcessorDto.getFiles());
    }
}
