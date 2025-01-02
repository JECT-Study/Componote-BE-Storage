package ject.componote.error;

import org.springframework.http.HttpStatus;

public class InvalidImageExtensionException extends StorageException {
    public InvalidImageExtensionException(final String extension) {
        super("이미지 확장자가 잘못되었습니다. 확장자: " + extension, HttpStatus.BAD_REQUEST);
    }
}
