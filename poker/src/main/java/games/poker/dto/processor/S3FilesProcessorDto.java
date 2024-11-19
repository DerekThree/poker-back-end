package games.poker.dto.processor;

import games.poker.dto.request.S3FilesRequestDto;
import games.poker.dto.response.S3FilesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3FilesProcessorDto {

    private S3FilesRequestDto request;

    private S3FilesResponseDto response;
}
