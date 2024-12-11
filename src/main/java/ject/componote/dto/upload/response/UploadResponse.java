package ject.componote.dto.upload.response;

public record UploadResponse(String fileUrl) {
    public static UploadResponse from(final String fileUrl) {
        return new UploadResponse(fileUrl);
    }
}
