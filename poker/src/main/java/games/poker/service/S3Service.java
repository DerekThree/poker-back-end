package games.poker.service;

import java.util.List;

public interface S3Service {

    public List<String> getFileNames(String prefix);
}
