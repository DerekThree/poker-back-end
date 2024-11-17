package games.poker.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service{

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private final AmazonS3 s3;

    public S3ServiceImpl() {
        s3 = getAmazonS3();
    }

    @Override
    public List<String> getFileNames(String prefix) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix);
        ListObjectsV2Result result = s3.listObjectsV2(request);
        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .map(this::removeFolderName)
                .toList();
    }

    private String removeFolderName(String fileKey) {
        return fileKey.substring(fileKey.lastIndexOf('/') + 1);
    }

    private AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.US_EAST_2)
                .build();
    }
}
