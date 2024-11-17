package games.poker.dto.processor;

import games.poker.dto.request.S3RequestDto;
import games.poker.dto.response.S3ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3ProcessorDto {

    private S3RequestDto request;

    private S3ResponseDto response;
}
