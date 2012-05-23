package cl.automind.empathy.fw.arbiter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.automind.empathy.AbstractArbiter;
import cl.automind.empathy.IArbiterCriterion;
import cl.automind.empathy.EmpathicKernel;
import cl.automind.empathy.rule.RuleUsageData;

public class DefaultArbiter extends AbstractArbiter {
	private final Map<String, RuleUsageData> ruleUsageData;
	private final List<String> ruleUsageList;
	private int differentRulesUsedCount;
	public DefaultArbiter(){
		super();
		ruleUsageData = new HashMap<String, RuleUsageData>();
		ruleUsageList = new ArrayList<String>();
	}
	public DefaultArbiter(EmpathicKernel kernel) {
		super(kernel, null);
		ruleUsageData = new HashMap<String, RuleUsageData>();
		ruleUsageList = new ArrayList<String>();
	}
	public DefaultArbiter(EmpathicKernel kernel, IArbiterCriterion criterion) {
		super(kernel, criterion);
		ruleUsageData = new HashMap<String, RuleUsageData>();
		ruleUsageList = new ArrayList<String>();
	}

	@Override
	public Map<String, RuleUsageData> getAllRuleUsageData() {
		return ruleUsageData;
	}

	@Override
	public List<String> getRuleUsageList() {
		return ruleUsageList;
	}

	@Override
	public int getDifferentRulesUsedCount() {
		return differentRulesUsedCount;
	}

	@Override
	public void preUpdateValidRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postUpdateValidRule() {
		String rulename = getCurrentValidRule().getName();
		if (getRuleUsageList().contains(rulename)){
			ruleUsageData.get(rulename).newUse();
		} else {
			ruleUsageList.add(rulename);
			ruleUsageData.put(rulename, new RuleUsageData(true));
			differentRulesUsedCount++;
		}
	}
	/**
	 * Returns true if the current valid rule needs to be updated.
	 * The implementation in {@link DefaultArbiter} ALWAYS returns true.
	 * @return true
	 */
	@Override
	public boolean needsUpdate() {
		return true;
	}

}
