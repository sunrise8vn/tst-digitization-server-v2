package com.tst.models.enums;


public enum EUserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_MANAGER("MANAGER"),
    ROLE_USER("USER");

    private final String value;

    EUserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static EUserRole getEUserRoleByName(String name) {
        for (EUserRole eUserRole : values()) {
            if (eUserRole.getValue().equals(name)) {
                return eUserRole;
            }
        }
        throw new IllegalArgumentException("Please re-enter");
    }
}
