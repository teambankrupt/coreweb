package com.example.coreweb.utils;

import com.example.coreweb.domains.base.models.enums.SortByFields;
import org.springframework.data.domain.Sort;

import java.util.Locale;

public class PageableParams {
    private final String query;
    private final int page;
    private int size;
    private final SortByFields sortBy;
    private final Sort.Direction direction;

    private PageableParams(String query, int page, int size, SortByFields sortBy, Sort.Direction direction) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.direction = direction;
    }

    public static PageableParams of(String query, int page, int size, SortByFields sortBy, Sort.Direction direction) {
        return new PageableParams(query, page, size, sortBy, direction);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuery() {
        if (this.query == null) return null;
        return this.query.toLowerCase(Locale.getDefault());
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public SortByFields getSortBy() {
        return sortBy;
    }

    public Sort.Direction getDirection() {
        return direction;
    }
}
