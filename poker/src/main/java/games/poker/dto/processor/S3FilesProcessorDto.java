package games.poker.dto.processor;

import games.poker.dto.request.S3RequestDto;
import games.poker.dto.response.S3FilesResponseDto;
import games.poker.model.FileData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3FilesProcessorDto {

    private S3RequestDto request;

    private List<FileData> files;

    private String uploadUrl;
}
