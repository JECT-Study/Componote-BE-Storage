package ject.componote.dto.upload.request;

import org.springframework.web.multipart.MultipartFile;

public record SingleUploadRequest(Long memberId, MultipartFile image) {
}
