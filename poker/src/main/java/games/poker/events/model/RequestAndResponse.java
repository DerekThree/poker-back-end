package games.poker.events.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestAndResponse {
    private String eventTimeStamp;
    private Long serviceExecutionTime;
    private Object requestData;
    private Object responseData;

}
