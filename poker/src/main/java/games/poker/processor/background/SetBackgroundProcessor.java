package games.poker.processor.background;

import games.poker.dto.processor.BackgroundProcessorDto;
import games.poker.dto.request.BackgroundRequestDto;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SetBackgroundProcessor implements Processor {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) throws Exception {
        BackgroundRequestDto request = exchange.getIn().getBody(BackgroundProcessorDto.class).getRequest();
        log.info("Processing set background request: {}", request);

        String username = request.getUsername();
        String activeDir = username + "-active/";

        List<String> oldBackgrounds = s3ServiceImpl.getFileNames(activeDir);
        if (oldBackgrounds.size() > 1) {
            log.warn("Multiple files found in active background directory: {}", oldBackgrounds);
        }

        for (String oldBackground : oldBackgrounds) {
            s3ServiceImpl.deleteFile(activeDir + oldBackground);
        }

        String newBackground = request.getFilename();
        String oldKey = username + "/" + newBackground;
        String newKey = activeDir + newBackground;
        s3ServiceImpl.copyFile(oldKey, newKey);
    }
}
