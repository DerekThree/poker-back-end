package games.poker.processor.s3;

import games.poker.dto.processor.S3ProcessorDto;
import games.poker.dto.response.S3ResponseDto;
import games.poker.service.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3GetProcessor implements Processor {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @Override
    public void process(Exchange exchange) {
        S3ProcessorDto processorDto = exchange.getIn().getBody(S3ProcessorDto.class);
        String username = processorDto.getRequest().getUsername();
        S3ResponseDto response = processorDto.getResponse();
        response.setFilesNames(s3ServiceImpl.getFileNames(username));
    }
}
