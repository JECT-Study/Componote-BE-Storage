package ject.componote.application.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import ject.componote.application.StorageService;
import ject.componote.util.ObjectKeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3StorageService implements StorageService {
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final ObjectKeyProvider objectKeyProvider;

    public S3StorageService(final AmazonS3 s3Client,
                            @Value("${cloud.aws.s3.bucket}") final String bucketName,
                            final ObjectKeyProvider objectKeyProvider) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.objectKeyProvider = objectKeyProvider;
    }

    @Override
    public String uploadImage(final MultipartFile image, final String type) {
        validate(image);

        final String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        final String tempPath = objectKeyProvider.generateTempPath(extension, type);
        final PutObjectRequest putObjectRequest = createPutObjectRequest(image, tempPath);

        s3Client.putObject(putObjectRequest);
        return objectKeyProvider.toObjectKey(tempPath);
    }

    @Override
    public void moveImage(final String tempObjectKey) {
        // 실패 시 별도 처리 필요
        final String tempPath = objectKeyProvider.toTempPath(tempObjectKey);
        final String permanentPath = objectKeyProvider.toPermanentPath(tempPath);
        s3Client.copyObject(bucketName, tempPath, bucketName, permanentPath);
    }

    @Override
    public void deleteImage(final String objectKey) {
        s3Client.deleteObject(bucketName, objectKey);
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
}
