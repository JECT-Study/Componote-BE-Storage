package ject.componote.application.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import ject.componote.application.StorageService;
import ject.componote.util.ObjectKeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class S3StorageService implements StorageService {
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final ObjectKeyGenerator objectKeyGenerator;

    public S3StorageService(final AmazonS3 s3Client,
                            @Value("${cloud.aws.s3.bucket}") final String bucketName, final ObjectKeyGenerator objectKeyGenerator) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.objectKeyGenerator = objectKeyGenerator;
    }

    @Override
    public String getByObjectKey(final Long memberId, final String objectKey) {
        try {
            s3Client.getObjectMetadata(bucketName, objectKey);  // URL이 유효한지만 판단하면 되므로 metadata만 가져온다.
            return getFileUrl(bucketName, objectKey);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new IllegalArgumentException("Object not found: " + objectKey, e);
            }
            throw new RuntimeException("S3 error occurred: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve object: " + objectKey, e);
        }
    }

    @Override
    public String uploadImage(final MultipartFile image, final Long memberId) {
        validate(image);

        final String objectKey = objectKeyGenerator.generateTempObjectKey(image.getOriginalFilename(), memberId);
        final PutObjectRequest putObjectRequest = createPutObjectRequest(image, objectKey);

        s3Client.putObject(putObjectRequest);
        return objectKey;
    }

    @Override
    public List<String> uploadImages(final List<MultipartFile> images, final Long memberId) {
        // 병렬 처리 고민 => 일부 이미지 업로드 실패 시 어떻게 할건지?
        return images.stream()
                .map(image -> uploadImage(image, memberId))
                .toList();
    }

    @Override
    public void moveToPermanentFolder(final String tempObjectKey) {
        final String permanentObjectKey = objectKeyGenerator.generatePermanentObjectKey(tempObjectKey);
        s3Client.copyObject(bucketName, tempObjectKey, bucketName, permanentObjectKey);
    }

    private PutObjectRequest createPutObjectRequest(final MultipartFile file, final String objectKey) {
        final ObjectMetadata objectMetadata = createObjectMetadata(file);
        try {
            return new PutObjectRequest(
                    bucketName,
                    objectKey,
                    file.getInputStream(),
                    objectMetadata
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectMetadata createObjectMetadata(final MultipartFile file) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }

    private String getFileUrl(final String bucketName, final String key) {
        final String region = s3Client.getRegionName();
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }
}
