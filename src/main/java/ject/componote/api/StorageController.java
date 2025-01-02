package ject.componote.api;

import jakarta.validation.Valid;
import ject.componote.application.StorageService;
import ject.componote.dto.delete.request.ImageDeleteRequest;
import ject.componote.dto.move.request.ImageMoveRequest;
import ject.componote.dto.upload.request.ImageUploadRequest;
import ject.componote.dto.upload.response.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class StorageController {
    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImages(@ModelAttribute @Valid final ImageUploadRequest request) {
        final String tempObjectKey = storageService.uploadImage(request.image(), request.type());
        return ResponseEntity.ok(
                ImageUploadResponse.from(tempObjectKey)
        );
    }

    @PostMapping("/move")
    public ResponseEntity<Void> moveImage(@RequestBody @Valid final ImageMoveRequest request) {
        storageService.moveImage(request.tempObjectKey());
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@ModelAttribute @Valid final ImageDeleteRequest request) {
        storageService.deleteImage(request.objectKey());
        return ResponseEntity.noContent()
                .build();
    }
}
