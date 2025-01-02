package ject.componote.dto.move.request;

import jakarta.validation.constraints.NotNull;

public record ImageMoveRequest(@NotNull String tempObjectKey) {
}
