package com.tst.models.enums;

public enum EPaperSize {
    A0("A0"),
    A1("A1"),
    A2("A2"),
    A3("A3"),
    A4("A4");

    private final String value;

    EPaperSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static boolean checkValue(String name) {
        for (EPaperSize ePaperSize : values()) {
            if (ePaperSize.getValue().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
