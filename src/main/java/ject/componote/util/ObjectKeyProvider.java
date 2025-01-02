package ject.componote.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectKeyProvider {
    public String generateTempPath(final String extension, final String type) {
        final String uniqueIdentifier = UUID.randomUUID().toString().substring(0, 16);
        return String.format(
                "temp/%s/%s.%s",
                type,
                uniqueIdentifier,
                extension
        );
    }

    public String toObjectKey(final String path) {
        return path.replaceFirst("temp/", "");
    }

    public String toTempPath(final String objectKey) {
        return "temp/" + objectKey;
    }

    public String toPermanentPath(final String tempPath) {
        return tempPath.replaceFirst("temp", "data");
    }
}
