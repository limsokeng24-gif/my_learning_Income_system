package com.school_management.overseas_language_centre.dto.pagination;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

// lombok
@Data
@NoArgsConstructor
public class PageDTO {
    private List<?> items;
    private PaginationDTO pagination;

    public PageDTO(Page<?> page) {
        this.items = page.getContent();

        int pageSize;
        int pageNumber;

        try {
            pageSize = page.getPageable().getPageSize();
            pageNumber = page.getPageable().getPageNumber();
        }catch (UnsupportedOperationException e) {
            pageSize = page.getNumberOfElements();
            pageNumber = 1;
        }

        this.pagination = PaginationDTO.builder()
                .empty(page.isEmpty())
                .first(page.isFirst())
                .last(page.isLast())
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }
}
