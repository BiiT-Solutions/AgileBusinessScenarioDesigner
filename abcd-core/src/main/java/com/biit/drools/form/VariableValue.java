package com.biit.drools.form;

import com.biit.drools.variables.FormVariableValue;
import org.kie.api.definition.type.Modifies;

public class VariableValue extends FormVariableValue {
    public VariableValue(String reference, String variable, Object value) {
        super(reference, variable, value);
    }

    @Modifies("value")
    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }

}
