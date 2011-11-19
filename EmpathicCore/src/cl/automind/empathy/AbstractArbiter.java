package cl.automind.empathy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.automind.empathy.rule.EmptyRule;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.RuleUsageData;

public abstract class AbstractArbiter implements IArbiter{
	private IRule validRule;
	private IArbiterCriterion criterion;
	private EmpathicKernel empathicKernel;
	public AbstractArbiter(){

	}
	public AbstractArbiter(EmpathicKernel kernel, IArbiterCriterion criterion){
		this.setEmpathicKernel(kernel);
		setCriterion(criterion);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getAllRuleUsageData()
	 */
	@Override
	public abstract Map<String, RuleUsageData> getAllRuleUsageData();
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getRuleUsageData(java.lang.String)
	 */
	@Override
	public RuleUsageData getRuleUsageData(String rulename) {
		return getAllRuleUsageData().get(rulename);
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getRuleUsageList()
	 */
	@Override
	public abstract List<String> getRuleUsageList();
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getDifferentRulesUsedCount()
	 */
	@Override
	public abstract int getDifferentRulesUsedCount();
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getRuleTriggeredCount()
	 */
	@Override
	public int getRuleTriggeredCount(){
		return getRuleUsageList().size();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getValidRule()
	 */
	@Override
	public final IRule getValidRule(){
		if (needsUpdate()){
			updateValidRule();
		}
		return getCurrentValidRule();
	}
	protected IRule getCurrentValidRule(){
		return validRule;
	}
	/**
	 * Sets the current valid rule
	 * @param rule
	 */
	protected void setValidRule(IRule rule){
		validRule = rule;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#updateValidRule()
	 */
	@Override
	public final void updateValidRule(){
		preUpdateValidRule();
		setValidRule(applyCriterion());
		postUpdateValidRule();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#preUpdateValidRule()
	 */
	@Override
	public abstract void preUpdateValidRule();
	/**
	 * Performs the update of the valid rule. At this method a
	 */
	private final IRule applyCriterion(){
		return accept(getCriterion());
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#postUpdateValidRule()
	 */
	@Override
	public abstract void postUpdateValidRule();
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#needsUpdate()
	 */
	@Override
	public abstract boolean needsUpdate();

	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#accept(cl.automind.empathy.ArbiterCriterion)
	 */
	@Override
	public IRule accept(IArbiterCriterion visitor){
		return visitor.visit(this);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#setCriterion(cl.automind.empathy.ArbiterCriterion)
	 */
	@Override
	public void setCriterion(IArbiterCriterion criterion){
		this.criterion = criterion;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getCriterion()
	 */
	@Override
	public IArbiterCriterion getCriterion(){
		return criterion;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getRule(java.lang.String)
	 */
	@Override
	public IRule getRule(String rulename){
		return getEmpathicKernel().getRule(rulename);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getAllRuleNames()
	 */
	@Override
	public Set<String> getAllRuleNames(){
		return getEmpathicKernel().getAllRuleNames();
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getEvaluableRules()
	 */
	@Override
	public List<String> getEvaluableRules(){
		List<String> list = new ArrayList<String>();
		IRule rule;
		for(String rulename: getAllRuleNames()){
			rule = getRule(rulename);
			if (rule.canEvaluate(rule.getParams())){
				list.add(rulename);
				System.out.println("Rule::"+rulename+"::Evaluable?:YES");
			} else {
				System.out.println("Rule::"+rulename+"::Evaluable?:NOT");
			}
		}
		if(list.size() == 0) list.add(EmptyRule.instance.getName());
		return list;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getSelectableRules()
	 */
	@Override
	public List<String> getSelectableRules(){
		List<String> list = new ArrayList<String>();
		IRule rule;
		for (String rulename: getEvaluableRules()){
			rule = getRule(rulename);
			rule.evaluate(rule.getParams());
			if (rule.isSelectable()) list.add(rulename);
		}
		return list;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#timesUsed(java.lang.String, int)
	 */
	@Override
	public int timesUsed(String rulename, int ifNotFound){
		RuleUsageData rud = getAllRuleUsageData().get(rulename);
		if (rud != null){
			return rud.getTimesUsed();
		}
		else return getAllRuleNames().contains(rulename) ? 0: ifNotFound;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#setEmpathicKernel(cl.automind.empathy.EmpathicKernel)
	 */
	@Override
	public void setEmpathicKernel(EmpathicKernel empathicKernel) {
		this.empathicKernel = empathicKernel;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.IArbiter#getEmpathicKernel()
	 */
	@Override
	public	EmpathicKernel getEmpathicKernel() {
		return empathicKernel;
	}
}
