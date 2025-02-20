package com.tst.models.enums;

public enum EExportStatus {
    PROCESSING("PROCESSING"),
    FAILED("FAILED"),
    SUCCESS("SUCCESS");

    private final String value;

    EExportStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
