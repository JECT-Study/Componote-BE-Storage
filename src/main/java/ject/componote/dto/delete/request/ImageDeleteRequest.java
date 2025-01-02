package ject.componote.dto.delete.request;

import jakarta.validation.constraints.NotBlank;

public record ImageDeleteRequest(@NotBlank String objectKey) {
}
