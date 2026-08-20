package com.school_management.overseas_language_centre.feature.core.role.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleExportResult {
    private byte[] file;
    private String fileName;
}
