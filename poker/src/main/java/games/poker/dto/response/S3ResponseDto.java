package games.poker.dto.response;

import com.amazonaws.services.s3.model.S3Object;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3ResponseDto {

    private List<S3Object> files;

}
