package com.tst.models.enums;

public enum EInputStatus {
    NEW("NEW"),
    LATER_PROCESSING("LATER_PROCESSING"),
    IMPORTED("IMPORTED"),
    RE_IMPORTED("RE_IMPORTED"),
    MATCHING("MATCHING"),
    NOT_MATCHING("NOT_MATCHING"),
    CHECKED_MATCHING("CHECKED_MATCHING"),
    CHECKED_NOT_MATCHING("CHECKED_NOT_MATCHING"),
    FINAL_MATCHING("FINAL_MATCHING"),
    FINAL_NOT_MATCHING("FINAL_NOT_MATCHING"),
    RELEASED("RELEASED");

    private final String value;

    EInputStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
