package games.poker.processor.background;

import games.poker.dto.processor.BackgroundProcessorDto;
import games.poker.dto.request.BackgroundRequestDto;
import games.poker.model.FileData;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetBackgroundProcessor implements Processor {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) throws Exception {
        BackgroundProcessorDto processorDto = exchange.getIn().getBody(BackgroundProcessorDto.class);
        BackgroundRequestDto request = processorDto.getRequest();
        log.info("Processing get background request: {}", request);

        String prefix = request.getUsername() + "-active/";
        List<FileData> files = s3ServiceImpl.getFiles(prefix);
        if (files.size() > 1) {
            log.warn("Multiple files found in active background directory: {}", files);
        }

        String fileKey = prefix + files.get(0).getName();
        processorDto.setResponse(s3ServiceImpl.getFile(fileKey));
    }
}
