package games.poker.service;

import games.poker.model.FileData;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    void createNewUserFiles(String username);

    List<FileData> getFiles(String prefix);

    InputStreamResource getFile(String key);

    void postFile(String prefix, MultipartFile file);

    void deleteFile(String key);

    void copyFile(String oldKey, String newKey);

}
