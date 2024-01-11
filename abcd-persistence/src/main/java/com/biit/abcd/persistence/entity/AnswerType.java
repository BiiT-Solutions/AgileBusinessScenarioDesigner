package com.biit.abcd.persistence.entity;

public enum AnswerType {
    RADIO(null, true, true, null, true, true),

    MULTI_CHECKBOX(null, true, true, null, true, true),

    // Uses answer format.
    INPUT(AnswerFormat.TEXT, false, false, null, true, false);

    public boolean isInputField() {
        return this.equals(AnswerType.INPUT);
    }

    private AnswerFormat defaultAnswerFormat;
    private boolean childrenAllowed;
    private boolean subChildrenAllowed;
    private Boolean defaultHorizontal;
    private Boolean defaultMandatory;
    private boolean nestedAnswerAllowed;

    AnswerType(AnswerFormat defaultAnswerType, boolean childrenAllowed, boolean subchildrenAllowed,
               Boolean defaultHorizontal, Boolean defaultMandatory, boolean nestedAnswerAllowed) {
        this.defaultAnswerFormat = defaultAnswerType;
        this.childrenAllowed = childrenAllowed;
        this.defaultHorizontal = defaultHorizontal;
        this.defaultMandatory = defaultMandatory;
        this.nestedAnswerAllowed = nestedAnswerAllowed;
        this.subChildrenAllowed = subchildrenAllowed;
    }

    public boolean isAnswerFormatEnabled() {
        return defaultAnswerFormat != null;
    }

    public boolean isHorizontalEnabled() {
        return defaultHorizontal != null;
    }

    public Boolean getDefaultHorizontal() {
        if (defaultHorizontal != null) {
            return defaultHorizontal;
        }
        return false;
    }

    public AnswerFormat getDefaultAnswerFormat() {
        return defaultAnswerFormat;
    }

    public boolean isChildrenAllowed() {
        return childrenAllowed;
    }

    public boolean isMandatoryEnabled() {
        return defaultMandatory != null;
    }

    public boolean getDefaultMandatory() {
        if (defaultMandatory != null) {
            return defaultMandatory;
        }
        return false;
    }

    public boolean isNestedAnswerAllowed() {
        return nestedAnswerAllowed;
    }

    public boolean isSubChildrenAllowed() {
        return subChildrenAllowed;
    }

    public static AnswerType get(String answerType) {
        for (AnswerType type : AnswerType.values()) {
            if (type.name().equalsIgnoreCase(answerType)) {
                return type;
            }
        }
        return null;
    }
}
