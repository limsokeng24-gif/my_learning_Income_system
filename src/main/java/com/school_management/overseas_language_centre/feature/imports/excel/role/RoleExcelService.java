package com.school_management.overseas_language_centre.feature.imports.excel.role;

import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleExportResult;
import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleImportResult;
import org.springframework.web.multipart.MultipartFile;

public interface RoleExcelService {
    RoleImportResult importFromXlsx(MultipartFile file);
    RoleExportResult exportToXlsx();
}
