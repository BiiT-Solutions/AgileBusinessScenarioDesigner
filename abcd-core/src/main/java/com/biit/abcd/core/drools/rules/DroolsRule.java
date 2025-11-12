package com.biit.abcd.core.drools.rules;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Basic drools rule class.
 */
public class DroolsRule extends Rule {
    private static final long serialVersionUID = 387304400306278717L;

    private Long salience;

    public DroolsRule() {
        super();
    }

    public DroolsRule(Rule rule) {
        this();
        setName(rule.getName());
        setConditions(rule.getConditions());
        setActions(rule.getActions());
    }

    public DroolsRule(Rule rule, Long salience) {
        this(rule);
        setSalience(salience);
    }

    public Long getSalience() {
        return salience;
    }

    public void setSalience(Long salience) {
        this.salience = salience;
    }

    public boolean isLockOnActive() {
        return getConditions() == null || getConditions().getExpression() == null || getConditions().getExpression().isEmpty();
    }


    @Override
    public Rule generateCopy() {
        DroolsRule copy = null;
        try {
            copy = this.getClass().newInstance();
            copy.copyData(this);
            copy.setSalience(getSalience());
        } catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
        return copy;
    }
}
