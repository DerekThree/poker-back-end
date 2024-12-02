package games.poker.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import games.poker.model.FileData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private AmazonS3 s3;

    private final String DEFAULT_BACKGROUND = "default.jpg";

    @PostConstruct
    public void init() {
        log.debug("Instantiating Amazon S3 client");
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.fromName(region))
                .build();
    }

    @Override
    public List<FileData> getFiles(String prefix) {
        log.info("Fetching file summaries from S3 for prefix {}", prefix);
        ListObjectsV2Result result;
        try {
            result = s3.listObjectsV2(bucketName, prefix);
        } catch (Exception e) {
            log.error("Error fetching files from S3: {}", e.getMessage());
            throw new RuntimeException("Error fetching file names from S3", e);
        }

        List<S3ObjectSummary> fileSummaries = result.getObjectSummaries();
        List<FileData> files = new ArrayList<>();
        for (S3ObjectSummary fileSummary: fileSummaries) {
            String key = fileSummary.getKey();
            String name = prefix.isBlank() ? key : truncateFolderName(key);
            String url = generatePresignedUrl(key, com.amazonaws.HttpMethod.GET);
            FileData fileData = new FileData(name, url);
            files.add(fileData);
        }

        log.info("Returning S3 files: {}", files);
        return files;
    }

    @Override
    public String getUploadUrl(String key) {
        return generatePresignedUrl(key, com.amazonaws.HttpMethod.PUT);
    }

    @Override
    public InputStreamResource getFile(String key) {
        log.info("Fetching file {} from S3", key);
        S3Object s3Object;
        try {
            s3Object = s3.getObject(bucketName, key);
        } catch (Exception e) {
            log.error("Error fetching file from S3: {}", e.getMessage());
            throw new RuntimeException("Error fetching file from S3", e);
        }

        return new InputStreamResource(s3Object.getObjectContent());
    }

    @Override
    // Legacy method
    public void postFile(String prefix, MultipartFile file) {
        String key = prefix + "/" + file.getOriginalFilename();
        log.info("Posting file {} to S3", key);
        try {
            s3.putObject(bucketName, key, file.getInputStream(), new ObjectMetadata());
        } catch (Exception e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    @Override
    public void deleteFile(String key) {
        log.info("Deleting file {} from S3", key);
        try {
            s3.deleteObject(bucketName, key);
        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            throw new RuntimeException("Error deleting file from S3", e);
        }
    }

    @Override
    public void copyFile(String oldKey, String newKey) {
        log.info("Copying file {} to {} in S3", oldKey, newKey);
        try {
            s3.copyObject(bucketName, oldKey, bucketName, newKey);
        } catch (Exception e) {
            log.error("Error copying file in S3: {}", e.getMessage());
            throw new RuntimeException("Error copying file in S3", e);
        }
    }

    @Override
    public void createNewUserFiles(String username) {
        log.debug("Creating new user files");
        copyFile(DEFAULT_BACKGROUND, username + "/" + DEFAULT_BACKGROUND);
        copyFile(DEFAULT_BACKGROUND, username + "-active/" + DEFAULT_BACKGROUND);
    }

    private String generatePresignedUrl(String objectKey, com.amazonaws.HttpMethod method) {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour expiration

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(method)
                .withExpiration(expiration);

        return s3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private String truncateFolderName(String fileKey) {
        return fileKey.substring(fileKey.lastIndexOf('/') + 1);
    }

}
