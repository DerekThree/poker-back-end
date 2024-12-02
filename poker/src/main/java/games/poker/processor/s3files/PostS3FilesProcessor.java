package games.poker.processor.s3files;

import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.dto.request.S3RequestDto;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
// Legacy processor
public class PostS3FilesProcessor implements Processor {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) {
        S3RequestDto request = exchange.getIn().getBody(S3FilesProcessorDto.class).getRequest();

        log.info("Processing upload file request: {}", request);
        String username = request.getUsername();
        MultipartFile file = request.getFile();
        s3ServiceImpl.postFile(username, file);
    }
}
