package games.poker.dto.processor;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import games.poker.dto.request.BackgroundRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackgroundProcessorDto {

    private BackgroundRequestDto request;

    private InputStreamResource response;

}
