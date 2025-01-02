package ject.componote.dto.upload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequest(@NotBlank String type, @NotNull MultipartFile image) {
}
