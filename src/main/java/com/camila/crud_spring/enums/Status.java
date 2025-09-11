package com.camila.crud_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
