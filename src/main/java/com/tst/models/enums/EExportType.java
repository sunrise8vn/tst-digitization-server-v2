package com.tst.models.enums;

public enum EExportType {
    EXCEL("EXCEL"),
    ZIP_PDF("ZIP_PDF"),
    BOTH("BOTH");

    private final String value;

    EExportType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
