package com.biit.abcd.persistence.entity.expressions;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
