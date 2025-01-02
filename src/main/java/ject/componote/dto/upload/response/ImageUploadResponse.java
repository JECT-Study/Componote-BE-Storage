package ject.componote.dto.upload.response;

public record ImageUploadResponse(String tempObjectKey) {
    public static ImageUploadResponse from(final String tempObjectKey) {
        return new ImageUploadResponse(tempObjectKey);
    }
}
