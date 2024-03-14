package com.example.chichakchessapi.app.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaginationInfo<T> {
    private Long totalElements;
    private Integer totalPages;
    private Integer pageSize;
    private Integer pageNumber;

    private List<T> page;

    public PaginationInfo(Long totalElements, Integer totalPages) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}