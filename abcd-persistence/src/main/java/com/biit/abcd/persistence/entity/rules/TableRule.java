package com.biit.abcd.persistence.entity.rules;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.abcd.serialization.rules.TableRuleDeserializer;
import com.biit.abcd.serialization.rules.TableRuleSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@JsonDeserialize(using = TableRuleDeserializer.class)
@JsonSerialize(using = TableRuleSerializer.class)
@Table(name = "rule_decision_table")
@Cacheable(true)
public class TableRule extends StorableObject implements INameAttribute {
    private static final long serialVersionUID = 1112900840434494717L;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot
    // simultaneously fetch multiple bags
    // (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size = 20)
    // @OrderBy(clause = "creationTime ASC")
    @JoinTable(name = "rule_decision_table_row_rules", joinColumns = @JoinColumn(name = "rule_decision_table", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rule", referencedColumnName = "id"))
    private List<TableRuleRow> rules;

    public TableRule() {
        super();
        rules = new ArrayList<>();
    }

    public TableRule(String name) {
        super();
        rules = new ArrayList<>();
        setName(name);
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (rules != null) {
            for (TableRuleRow tableRuleRow : rules) {
                tableRuleRow.resetIds();
            }
        }
    }

    public List<TableRuleRow> getRules() {
        return rules;
    }

    public void setRules(List<TableRuleRow> rules) {
        rules.clear();
        rules.addAll(rules);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public TableRuleRow addRow() {
        TableRuleRow row = new TableRuleRow();
        return addRow(row);
    }

    /**
     * When you add a new row, the table or the row is resized to allow the
     * operation
     *
     * @param row the row to add.
     * @return the row added.
     */
    public TableRuleRow addRow(TableRuleRow row) {
        if (!getRules().isEmpty()) {
            while (row.getConditionNumber() < getConditionNumber()) {
                row.addEmptyExpressionPair();
            }
            while (row.getConditionNumber() > getConditionNumber()) {
                addEmptyExpressionPair();
            }

        }
        getRules().add(row);
        return row;
    }

    public void addEmptyExpressionPair() {
        for (TableRuleRow row : getRules()) {
            row.addCondition(new ExpressionValueTreeObjectReference());
            row.addCondition(new ExpressionChain());
        }
    }

    public void removeRule(TableRuleRow rule) {
        rules.remove(rule);
    }

    public void removeConditions(TableRuleRow row, List<Expression> values) {
        for (Expression value : values) {
            row.getConditions().removeExpression(value);
        }
    }

    public int getConditionNumber() {
        if (getRules().size() > 0) {
            return getRules().get(0).getConditionNumber();
        } else {
            return 0;
        }
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        for (TableRuleRow rule : rules) {
            innerStorableObjects.add(rule);
            innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof TableRule) {
            super.copyBasicInfo(object);
            TableRule tableRule = (TableRule) object;
            this.setName(tableRule.getName());
            rules.clear();
            for (TableRuleRow row : tableRule.getRules()) {
                TableRuleRow newRow = new TableRuleRow();
                newRow.copyData(row);
                rules.add(newRow);
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TableRule.");
        }
    }
}
