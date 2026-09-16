package com.school_management.overseas_language_centre.feature.intergration.fileStorage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    // store object // save image
    // delete object // delete image
    // get object url //  get image
    String storeImage(MultipartFile file, String subDir);
    void deleteObject(String objectKeyOrUrl);
    String getFileUrl(String objectKeyOrUrl);
}
