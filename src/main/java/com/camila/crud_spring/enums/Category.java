package com.camila.crud_spring.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    FRONTEND("Front-end"),
    BACKEND("Back-end"),
    DATA_SCIENCE("Data Science"),
    DEVOPS("DevOps"),
    DATABASE("Banco de Dados");

    private final String value;

    Category(String value) {
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
