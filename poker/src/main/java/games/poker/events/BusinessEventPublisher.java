package games.poker.events;

import com.wu.era.library.event.config.BusinessEventHeader;
import com.wu.era.library.event.enums.BusinessEventType;
import com.wu.era.library.event.enums.ServiceAction;
import com.wu.era.library.event.utils.WUBusinessEventUtils;
import games.poker.events.model.RequestAndResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BusinessEventPublisher {

    private WUBusinessEventUtils eventUtils;

    public void publishEvent(String eventCode, String busEventTimeStamp, long apiResponseTime,
                             Object request, Object response, String apiName) {
        RequestAndResponse requestAndResponseEvent = RequestAndResponse.builder()
                .serviceExecutionTime(apiResponseTime)
                .eventTimeStamp(busEventTimeStamp)
                .requestData(request)
                .responseData(response).build();
        Map<String, Object> businessEvent = new HashMap<>();
        businessEvent.put(apiName, requestAndResponseEvent);
        BusinessEventHeader header = createHeader();
        long publishStart = System.currentTimeMillis();
        eventUtils.publishEvent(eventCode, BusinessEventType.BUSINESS, ServiceAction.INSERT, businessEvent, header);
        long publishEnd = System.currentTimeMillis() - publishStart;
        log.debug("Published event to Kafka successfully in: {} ms", publishEnd);
    }

    private BusinessEventHeader createHeader() {
        BusinessEventHeader header = new BusinessEventHeader();
        header.setBusinessArea("BUSINESS_AREA");
        header.setBusinessDomain("BUSINESS_DOMAIN");
        return header;
    }

}
