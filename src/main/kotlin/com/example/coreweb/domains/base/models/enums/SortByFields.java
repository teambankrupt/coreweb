package com.example.coreweb.domains.base.models.enums;

public enum SortByFields {
    ID("id"),
    CREATED_AT("createdAt");

    private final String fieldName;

    SortByFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
