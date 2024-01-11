package com.biit.abcd.persistence.entity;

public enum CustomVariableType {
    STRING("class.String", " "),

    NUMBER("class.Number", null),

    DATE("class.Date", null);

    private final String translation;

    private final String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    private CustomVariableType(String translation, String defaultValue) {
        this.translation = translation;
        this.defaultValue = defaultValue;
    }

    public String getTranslationCode() {
        return translation;
    }

    public static CustomVariableType get(String type) {
        for (CustomVariableType customVariableType : CustomVariableType.values()) {
            if (customVariableType.name().equalsIgnoreCase(type)) {
                return customVariableType;
            }
        }
        return null;
    }
}
