package games.poker.processor.s3files;

import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.dto.request.S3FilesRequestDto;
import games.poker.dto.response.S3FilesResponseDto;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetS3FilesProcessor implements Processor {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) {
        S3FilesProcessorDto processorDto = exchange.getIn().getBody(S3FilesProcessorDto.class);
        S3FilesRequestDto request = processorDto.getRequest();
        log.info("Processing get file names request: {}", request);

        String username = request.getUsername();
        S3FilesResponseDto response = processorDto.getResponse();
        response.setFileNames(s3ServiceImpl.getFileNames(username + "/"));
    }
}
