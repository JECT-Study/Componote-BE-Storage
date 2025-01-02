package ject.componote.application;

import ject.componote.error.InvalidImageExtensionException;
import ject.componote.error.NotFoundImageException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface StorageService {
    List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    String uploadImage(final MultipartFile image, final String type);
    void moveImage(final String tempObjectKey);
    void deleteImage(final String objectKey);

    default void validate(final MultipartFile file) {
        validateImage(file);
        validateImageFileExtension(file);
    }

    private void validateImage(final MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new NotFoundImageException();
        }
    }

    private void validateImageFileExtension(final MultipartFile image) {
        final String filename = image.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(filename);
        if (extension == null || !ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new InvalidImageExtensionException(extension);
        }
    }
}
