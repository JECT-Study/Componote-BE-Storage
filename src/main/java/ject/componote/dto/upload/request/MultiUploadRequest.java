package ject.componote.dto.upload.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record MultiUploadRequest(Long memberId, List<MultipartFile> images) {
}
