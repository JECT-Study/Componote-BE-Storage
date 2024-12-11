package ject.componote.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectKeyGenerator {
    public String generateTempObjectKey(final String originalFilename, final Long memberId) {
        final String uniqueIdentifier = UUID.randomUUID().toString().substring(0, 8);
        return String.format(
                "/temp/users/%d/%s_%s",
                memberId,
                uniqueIdentifier,
                originalFilename
        );
    }

    public String generatePermanentObjectKey(final String tempObjectKey) {
        return tempObjectKey.replaceFirst("temp", "permanent");
    }
}
