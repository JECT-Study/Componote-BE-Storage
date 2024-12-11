package ject.componote.dto.download.response;

public record DownloadResponse(String url) {
    public static DownloadResponse from(final String url) {
        return new DownloadResponse(url);
    }
}
