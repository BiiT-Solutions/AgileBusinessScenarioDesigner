package com.biit.abcd.persistence.entity.expressions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class ExprFunction extends ExprWChilds {
	
	//Do not persist.
	private HashMap<String, ExprPort> portMap;

	public ExprFunction() {
		super();
		portMap = new LinkedHashMap<String, ExprPort>();
	}

	protected void addPort(String key, ExprPort exprPort) {
		exprPort.parent = this;
		childs.add(exprPort);
		portMap.put(key, exprPort);
	}

	protected ExprPort getPort(String key) {
		return portMap.get(key);
	}

	public Collection<ExprPort> getPorts() {
		return portMap.values();
	}
}
