package games.poker.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InputProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        HashMap<String, String> body =  exchange.getIn().getBody(HashMap.class);
        body.put("key", "success");
    }
}
