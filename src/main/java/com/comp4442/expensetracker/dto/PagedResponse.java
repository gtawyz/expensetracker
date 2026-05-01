package com.comp4442.expensetracker.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    // Copies Spring Page content and metadata into a simpler response object for the API.
    public PagedResponse(Page<T> pageData) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
        this.last = pageData.isLast();
    }

    // Returns the records contained in the current page.
    public List<T> getContent() {
        return content;
    }

    // Returns the zero-based page number for this result.
    public int getPage() {
        return page;
    }

    // Returns the requested page size.
    public int getSize() {
        return size;
    }

    // Returns how many matching records exist across all pages.
    public long getTotalElements() {
        return totalElements;
    }

    // Returns how many pages are available in total.
    public int getTotalPages() {
        return totalPages;
    }

    // Returns true when this page is the final page of results.
    public boolean isLast() {
        return last;
    }
}
