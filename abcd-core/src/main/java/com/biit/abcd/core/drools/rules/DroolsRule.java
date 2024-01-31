package com.biit.abcd.core.drools.rules;

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
