package com.biit.abcd.core.drools.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * All the complex rules like 'A and (B or C)' are divided in sets of simple
 * rules like: 'BC = B or C', 'A and BC'.<br>
 * To allow this we create groups of rules so we can know when a rule is part of
 * a more complex one.<br>
 * This class defines the rule that combines all the simple rules generated.<br>
 * It has especial conditions to avoid the launch of the rule several times.
 */
public class DroolsRuleGroupEndRule extends DroolsRuleGroup {
	private static final long serialVersionUID = 5322960385494913547L;
	private String groupCondition = "";
	private String groupAction = "";
	private Map<String, String> rulesIdentifierMap;
	private ITreeElement parserResult;

	public DroolsRuleGroupEndRule() {
		super();
		rulesIdentifierMap = new HashMap<String, String>();
	}

	public DroolsRuleGroupEndRule(Rule rule) {
		super(rule);
	}

	public String getGroupCondition() {
		return groupCondition;
	}

	public void setGroupCondition(String groupCondition) {
		this.groupCondition = groupCondition;
	}

	public String getGroupAction() {
		return groupAction;
	}

	public void setGroupAction(String groupAction) {
		this.groupAction = groupAction;
	}

	public void putExpresionRuleIdentifiers(String expressionId, String ruleName) {
		rulesIdentifierMap.put(expressionId, ruleName);
	}

	public void getRuleNameWithId(String ruleId) {
		rulesIdentifierMap.get(ruleId);
	}

	public ITreeElement getParserResult() {
		return parserResult;
	}

	public void setParserResult(ITreeElement parserResult) {
		this.parserResult = parserResult;
	}

	public Set<Entry<String, String>> getMapEntry() {
		return rulesIdentifierMap.entrySet();
	}

	public Map<String, String> getRulesIdentifierMap() {
		return rulesIdentifierMap;
	}

	public void setRulesIdentifierMap(Map<String, String> rulesIdentifierMap) {
		this.rulesIdentifierMap = rulesIdentifierMap;
	}
}
