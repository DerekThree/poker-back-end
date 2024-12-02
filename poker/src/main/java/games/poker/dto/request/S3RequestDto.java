package games.poker.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3RequestDto {

    private String method;

    private String username;
    private Boolean isAdmin;

    private String filename;

    private MultipartFile file;
}