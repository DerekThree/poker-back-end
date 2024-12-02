package games.poker.processor.s3files;

import games.poker.dto.processor.S3FilesProcessorDto;
import games.poker.dto.request.S3RequestDto;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteS3FilesProcessor implements Processor {
    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) {
        S3RequestDto request = exchange.getIn().getBody(S3FilesProcessorDto.class).getRequest();

        log.info("Processing delete file request: {}", request);
        boolean isAdmin = request.getIsAdmin();
        String key = isAdmin ? request.getFilename() : request.getUsername() + "/" + request.getFilename();
        s3ServiceImpl.deleteFile(key);
    }
}
