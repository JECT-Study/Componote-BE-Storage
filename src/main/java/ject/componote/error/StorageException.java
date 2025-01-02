package ject.componote.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StorageException extends RuntimeException {
    private final HttpStatus status;

    public StorageException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
