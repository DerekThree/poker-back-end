package games.poker.processor.s3files;

import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3UploadUrlProcessor implements Processor {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) throws Exception {
        S3FilesProcessorDto processorDto = exchange.getIn().getBody(S3FilesProcessorDto.class);

        log.info("Processing upload URL request: {}", processorDto.getRequest());
        String username = processorDto.getRequest().getUsername();
        String filename = processorDto.getRequest().getFilename();
        processorDto.setUploadUrl(s3ServiceImpl.getUploadUrl(username + "/" + filename));
    }
}
