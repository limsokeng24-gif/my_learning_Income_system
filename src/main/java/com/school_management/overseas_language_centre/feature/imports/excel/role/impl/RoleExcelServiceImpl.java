package com.school_management.overseas_language_centre.feature.imports.excel.role.impl;


import com.school_management.overseas_language_centre.entity.Role;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleExportResult;
import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleImportResult;
import com.school_management.overseas_language_centre.feature.core.role.mapper.RoleMapper;
import com.school_management.overseas_language_centre.feature.core.role.normalizer.RoleNormalizer;
import com.school_management.overseas_language_centre.feature.core.role.repository.RoleRepository;
import com.school_management.overseas_language_centre.feature.imports.excel.role.RoleExcelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleExcelServiceImpl implements RoleExcelService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleNormalizer roleNormalizer;
    private final DataFormatter dataFormatter = new DataFormatter();

    @Transactional
    @Override
    public RoleImportResult importFromXlsx(MultipartFile file) {
        // check file have or not
        if (file == null || file.isEmpty()){
            throw new IllegalArgumentException("XLSX file is required and must not be empty");
        }

        int total = 0;
        int imported = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();
        Set<String> seenInFile = new HashSet<>();

        //role.xlsx
        try (InputStream is = file.getInputStream()){
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);

            Row header = sheet.getRow(0);

            if (header == null ) {
                throw new IllegalArgumentException("XLSX is empty (no header row)");
            }

            int nameCol = -1;
            int descCol = -1;

            for (Cell cell : header) {
                String h = cellString(cell);
                if(h == null) {
                    continue;
                }
                if (h.equalsIgnoreCase("name")) {
                    nameCol = cell.getColumnIndex();
//                    nameCol = 1;
                } else if (h.equalsIgnoreCase("description")) {
                    descCol = cell.getColumnIndex();
                }
            }
            if (nameCol == -1) {
                throw new IllegalArgumentException("XLSX must contain a 'name' column"); // -> 400
            }

            for (int r = 1; r <= sheet.getLastRowNum(); r++){
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                total++;
                long line = r + 1;

                RoleRequest request = new RoleRequest();
                request.setName(cellString(row.getCell(nameCol)));
//                request.setName(cellString(row.getCell(nameCol)));
                request.setDescription(descCol >= 0 ? cellString(row.getCell(descCol)) : null);

                roleNormalizer.normalize(request);

                if (request.getName() == null) {
                    skipped++;
                    errors.add("row " + line + ": name is blank");
                    continue;
                }

                if (!seenInFile.add(request.getName())) {            // add() returns false if already in the file
                    skipped++;                                        // duplicate within this upload
                    errors.add("row " + line + ": '" + request.getName() + "' duplicated within the file"); // record why
                    continue;                                        // next row
                }
                if (roleRepository.existsByName(request.getName())) {
                    skipped++;
                    errors.add("row " + line + ": '" + request.getName() + "' already exists");
                    continue;
                }

                Role entity = roleMapper.toEntity(request);
                roleRepository.save(entity);
                imported++;

            }
        }catch (IOException e){
            throw new IllegalArgumentException("Failed to read XLSX file: " + e.getMessage()); // -> 400
        }


        return RoleImportResult.builder()
                .totalRows(total)
                .imported(imported)
                .skipped(skipped)
                .errors(errors)
                .build();
    }

    @Transactional
    @Override
    public RoleExportResult exportToXlsx() {

        List<Role> roles = roleRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Roles");

            // Header
            Row headerRow = sheet.createRow(0);


            headerRow.createCell(0).setCellValue("name");
            headerRow.createCell(1).setCellValue("description");

            // Data
            int rowNum = 1;

            for (Role role : roles) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        role.getName() != null
                                ? role.getName() : ""
                );

                row.createCell(1).setCellValue(
                        role.getDescription() != null
                                ? role.getDescription()
                                : ""
                );
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return RoleExportResult.builder()
                    .file(outputStream.toByteArray())
                    .fileName("roles.xlsx")
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to export roles to Excel", e);
        }
    }

    private String cellString(Cell cell) {
        if (cell == null) {
            return  null;
        }
        String value = dataFormatter.formatCellValue(cell);

        return (value == null || value.isBlank())
                ? null
                : value.trim();
    }
}