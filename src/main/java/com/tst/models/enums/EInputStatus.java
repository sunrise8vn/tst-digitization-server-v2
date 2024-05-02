package com.tst.models.enums;

public enum EInputStatus {
    NEW("NEW"),
    LATER_PROCESSING("LATER_PROCESSING"),
    IMPORTED("IMPORTED"),
    MATCHING("MATCHING"),
    CHECKED("CHECKED");

    private final String value;

    EInputStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
