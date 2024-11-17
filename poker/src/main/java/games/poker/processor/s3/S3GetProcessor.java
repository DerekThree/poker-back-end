package games.poker.processor.s3;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3GetProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {

    }
}
