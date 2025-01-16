package ject.componote.api;

import ject.componote.application.StorageService;
import ject.componote.dto.delete.request.ImageDeleteRequest;
import ject.componote.dto.move.request.ImageMoveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageConsumer {
    private final StorageService storageService;

    @RabbitListener(
            queues = "imageMoveQueue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumeImageMoveMessage(final ImageMoveRequest request) {
        log.info("이미지 이동 메시지 수신: {}", request);
        storageService.moveImage(request.tempObjectKey());
    }

    @RabbitListener(
            queues = "imageDeleteQueue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumeImageDeleteMessage(final ImageDeleteRequest request) {
        log.info("이미지 삭제 메시지 수신: {}", request);
        storageService.deleteImage(request.objectKey());
    }
}
