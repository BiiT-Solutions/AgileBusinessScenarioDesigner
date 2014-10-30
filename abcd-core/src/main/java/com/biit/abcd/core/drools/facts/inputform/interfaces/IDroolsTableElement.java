package com.biit.abcd.core.drools.facts.inputform.interfaces;

import java.util.List;

public interface IDroolsTableElement {

	public String getName();
	public String getOriginalValue();
	public List<IDroolsTableElement> getChildren();
}
