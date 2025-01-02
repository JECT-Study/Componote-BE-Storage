package ject.componote.error;

import org.springframework.http.HttpStatus;

public class NotFoundImageException extends StorageException {
    public NotFoundImageException() {
        super("이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
