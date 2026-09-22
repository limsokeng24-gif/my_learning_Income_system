package com.school_management.overseas_language_centre.feature.intergration.fileStorage.impl;

import com.school_management.overseas_language_centre.feature.intergration.fileStorage.FileStorageService;
import com.school_management.overseas_language_centre.property.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private static final Map<String, String> EXTENSION_TYPES = Map.of(
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "webp", "image/webp",
            "gif", "image/gif");

    @Override
    public String storeImage(MultipartFile file, String subDir) {
        // check have file or not
        if (file == null || file.isEmpty()){
            throw  new ValidationException("Image file is required");
        }
        // get extension
        String extension = extensionOf(file.getOriginalFilename());
        String contentType = extension == null ? null : EXTENSION_TYPES.get(extension);

        // check type
        String declaredType = file.getContentType() == null
                ? ""
                : file.getContentType().toLowerCase(Locale.ROOT).trim();
        if (contentType == null && EXTENSION_TYPES.containsValue(declaredType)) {
            contentType = declaredType;
            // "image/jpeg" -> "jpeg", which is itself a key of the map above.
            extension = declaredType.substring(declaredType.indexOf('/') + 1);
        }

        if (contentType == null) {
            throw new ValidationException(
                    "Unsupported image type: " + (declaredType.isBlank() ? "unknown" : declaredType));
        }
        // check file size
        long maxBytes = (long) minioProperties.getMaxSizeMb() * 1024 * 1024;

        if (file.getSize() > maxBytes) {
            throw new ValidationException(
                    "Image is larger than " + minioProperties.getMaxSizeMb() + "MB");
        }
        // rename to UUID
        String objectKey = subDir + "/" + UUID.randomUUID() + "." + extension;
        try (InputStream in = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(objectKey)
                    .stream(in, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
          //   log.error("MinIO upload failed for key {}: {}", objectKey, e.getMessage());
            throw new ValidationException("Could not upload the image. Please try again.");
        }
        // IMPORTANT:
        // Return MinIO object key
        return objectKey;
    }

    // PNG -> png
    private String extensionOf(String filename) {
        if (filename == null) {
            return null;
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return null;
        }
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    @Override
    public void deleteObject(String objectKeyOrUrl) {
        if (objectKeyOrUrl == null || objectKeyOrUrl.isBlank()) {
            return;
        }

        String objectKey = extractObjectKey(objectKeyOrUrl);

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectKey)
                            .build()
            );

        } catch (Exception e) {
            throw new ValidationException(
                    "Could not delete the image."
            );
        }

    }

    @Override
    public String getFileUrl(String objectKeyOrUrl) {
        if (objectKeyOrUrl == null || objectKeyOrUrl.isBlank()) {
            return null;
        }

        String objectKey = extractObjectKey(objectKeyOrUrl);

        try {

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProperties.getBucket())
                            .object(objectKey)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );

        } catch (Exception e) {

            throw new ValidationException(
                    "Could not generate image URL."
            );
        }

    }

    private String extractObjectKey(String objectKeyOrUrl) {

        // If PostgreSQL contains:
        // users/15/abc.jpg
        //
        // just return it.

        if (!objectKeyOrUrl.startsWith("http://")
                && !objectKeyOrUrl.startsWith("https://")) {
            return objectKeyOrUrl;
        }

        // If database contains a full MinIO URL,
        // remove the endpoint and bucket.
        String prefix =
                minioProperties.getEndpoint()
                        + "/"
                        + minioProperties.getBucket()
                        + "/";

        if (objectKeyOrUrl.startsWith(prefix)) {
            return objectKeyOrUrl.substring(prefix.length());
        }

        return objectKeyOrUrl;
    }
}