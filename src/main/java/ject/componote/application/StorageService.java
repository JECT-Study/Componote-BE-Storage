package ject.componote.application;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface StorageService {
    List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    String getByObjectKey(final Long memberId, final String objectKey);

    String uploadImage(final MultipartFile image, final Long memberId);

    List<String> uploadImages(final List<MultipartFile> images, final Long memberId);

    void moveToPermanentFolder(final String tempPath);

    default void validate(final MultipartFile file) {
        validateImage(file);
        validateImageFileExtension(file);
    }

    private void validateImage(final MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new IllegalArgumentException("image is empty");
        }
    }

    private void validateImageFileExtension(final MultipartFile image) {
        final String filename = image.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(filename);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("invalid image extension");
        }
    }
}
