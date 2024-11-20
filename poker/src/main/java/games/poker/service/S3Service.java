package games.poker.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    List<String> getFileNames(String prefix);

    InputStreamResource getFile(String key);

    void postFile(String prefix, MultipartFile file);

    void deleteFile(String key);

    public void copyFile(String oldKey, String newKey);

}
