package games.poker.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service{

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private AmazonS3 s3;

    @PostConstruct
    public void init() {
        log.debug("Instantiating Amazon S3 client");
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.fromName(region))
                .build();
    }

    @Override
    public List<String> getFileNames(String prefix) {
        log.info("Fetching file summaries from S3 for prefix {}", prefix);
        ListObjectsV2Result result;
        try {
            result = s3.listObjectsV2(bucketName, prefix);
        } catch (Exception e) {
            log.error("Error fetching file names from S3: {}", e.getMessage());
            throw new RuntimeException("Error fetching file names from S3", e);
        }

        List<String> filenames = result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .map(this::truncateFolderName)
                .toList();
        log.info("Returning S3 file names: {}", filenames);
        return filenames;
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

    private String truncateFolderName(String fileKey) {
        return fileKey.substring(fileKey.lastIndexOf('/') + 1);
    }

}
