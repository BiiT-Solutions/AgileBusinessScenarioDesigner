package com.biit.abcd.persistence.entity.expressions;

public enum QuestionDateUnit {
    // Days from now.
    DAYS("D*", "Days", "DroolsDateUtils.returnDaysDistanceFromDate"),

    // Months from now.
    MONTHS("M*", "Months", "DroolsDateUtils.returnMonthsDistanceFromDate"),

    // Years from now.
    YEARS("Y*", "Years", "DroolsDateUtils.returnYearsDistanceFromDate"),

    // Pure data format.
    DATE("DT", "Date", " "),

    // Date from 1970
    ABSOLUTE_DAYS("D", "Days", "DroolsDateUtils.returnDaysDistanceFromOrigin"),

    // Date from 1970
    ABSOLUTE_MONTHS("M", "Months", "DroolsDateUtils.returnMonthsDistanceFromOrigin"),

    // Date from 1970
    ABSOLUTE_YEARS("Y", "Years", "DroolsDateUtils.returnYearsDistanceFromOrigin");

    private String abbreviature;
    private String unitName;
    private String droolsFunction;

    QuestionDateUnit(String abbreviature, String unitName, String droolsFunction) {
        this.abbreviature = abbreviature;
        this.unitName = unitName;
        this.droolsFunction = droolsFunction;
    }

    public String getAbbreviature() {
        return abbreviature;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getDroolsFunction() {
        return droolsFunction;
    }

    public static QuestionDateUnit get(String questionDateUnit) {
        for (QuestionDateUnit unit : QuestionDateUnit.values()) {
            if (unit.name().equalsIgnoreCase(questionDateUnit)) {
                return unit;
            }
        }
        return null;
    }

}