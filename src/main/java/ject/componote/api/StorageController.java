package ject.componote.api;

import ject.componote.application.StorageService;
import ject.componote.dto.download.request.DownloadRequest;
import ject.componote.dto.download.response.DownloadResponse;
import ject.componote.dto.move.request.MoveRequest;
import ject.componote.dto.upload.request.MultiUploadRequest;
import ject.componote.dto.upload.request.SingleUploadRequest;
import ject.componote.dto.upload.response.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping("/upload/image")
    public ResponseEntity<UploadResponse> uploadImage(@ModelAttribute final SingleUploadRequest request) {
        final String eTag = storageService.uploadImage(request.image(), request.memberId());
        final UploadResponse response = UploadResponse.from(eTag);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/images")
    public ResponseEntity<List<UploadResponse>> uploadImages(@ModelAttribute final MultiUploadRequest request) {
        final List<String> eTags = storageService.uploadImages(request.images(), request.memberId());
        final List<UploadResponse> uploadResponses = eTags.stream()
                .map(UploadResponse::from)
                .toList();
        return ResponseEntity.ok(uploadResponses);
    }

    @GetMapping("/download")
    public ResponseEntity<DownloadResponse> downloadImage(@ModelAttribute final DownloadRequest downloadRequest) {
        final String fileUrl = storageService.getByObjectKey(downloadRequest.memberId(), downloadRequest.objectKey());
        final DownloadResponse downloadResponse = DownloadResponse.from(fileUrl);
        return ResponseEntity.ok(downloadResponse);
    }

    @PostMapping("/move/temp-to-permanent")
    public ResponseEntity<Void> moveImage(@RequestBody final MoveRequest moveRequest) {
        storageService.moveToPermanentFolder(moveRequest.tempObjectKey());
        return ResponseEntity.noContent()
                .build();
    }
}
