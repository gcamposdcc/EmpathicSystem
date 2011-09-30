package cl.automind.empathy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.EmptyRule;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.RuleUsageData;

public abstract class AbstractArbiter{
	private IRule validRule;
	private ArbiterCriterion criterion;
	private EmpathicKernel empathicKernel;
	public AbstractArbiter(){
		
	}
	public AbstractArbiter(EmpathicKernel kernel, ArbiterCriterion criterion){
		this.setEmpathicKernel(kernel);
		setCriterion(criterion);
	}
	/**
	 * Gets a {@link Map} containing the {@link RuleUsageData} for all rules.
	 * The information for each rule is mapped using the rule name as key.
	 * @return a map with rule name as key and rule usage data as value.
	 */
	public abstract Map<String, RuleUsageData> getAllRuleUsageData();
	/**
	 * Gets the {@link RuleUsageData} associated with the rule with the name
	 * <i>rulename</i>
	 * @param rulename The name of the rule to be looked up
	 * @return The {@link RuleUsageData} associated to the rule. Returns
	 * <i>null</i> if the rule doesn't exist.
	 */
	public RuleUsageData getRuleUsageData(String rulename) {
		return getAllRuleUsageData().get(rulename);
	}

	/**
	 * Gets a {@link List} containing the list of the names of the rules marked
	 * as valid rules. This {@link List} <i>should</i> be ordered. It's
	 * encouraged to use a "first ocurrence elements go first" fashion.
	 * @return a collection with the name of the rules used
	 */
	public abstract List<String> getRuleUsageList();
	/**
	 * Gets the amount of different rules that have been marked as valid. As such,
	 * repeated uses of the same rule are <b>not</b> be counted.
	 * @return the number of different rules that have been marked as valid
	 */
	public abstract int getDifferentRulesUsedCount();
	/**
	 * Gets the total amount of rules marked as valid. As such, repeated uses of the same
	 * rule <b>are</b> counted.
	 * @return the number of times that a rule has been marked as 
	 */
	public int getRuleTriggeredCount(){
		return getRuleUsageList().size();
	}
	/**
	 * Gets the current valid rule, for doing this it first checks if the current
	 * valid rule is still valid using the {@code needsUpdate()} method.
	 * If the current rule is still valid it returns the same rule, otherwise
	 * it will update the rule by invoking the {@code updateValidRule()} method
	 * and then return the new valid {@link AbstractRule}
	 * @return the current valid rule
	 * @see AbstractRule
	 */
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
	/**
	 * Updates the current valid rule. It operates as a <i>Template Method</i>
	 * chaining three other methods: {@code preUpdateValidRule()}, 
	 * {@code updateValidRuleImpl()}, {@code postUpdateValidRule()}.
	 */
	public final void updateValidRule(){
		preUpdateValidRule();
		setValidRule(applyCriterion());
		postUpdateValidRule();
	}
	/**
	 * 
	 */
	public abstract void preUpdateValidRule();
	/**
	 * Performs the update of the valid rule. At this method a 
	 */
	private final IRule applyCriterion(){
		return accept(getCriterion());
	}
	/**
	 * This method is for additional handling after the new valid rule
	 * has been updated.
	 */
	public abstract void postUpdateValidRule();
	/**
	 * Determines if the current valid rule needs to be updated
	 * @return <b>true</b> if the current valid rule needs to be updated
	 */
	public abstract boolean needsUpdate();

	public IRule accept(ArbiterCriterion visitor){
		return visitor.visit(this);
	}
	public void setCriterion(ArbiterCriterion criterion){
		this.criterion = criterion;
	}
	public ArbiterCriterion getCriterion(){
		return criterion;
	}
	public IRule getRule(String rulename){
		return getEmpathicKernel().getRule(rulename);
	}
	public Set<String> getAllRuleNames(){
		return getEmpathicKernel().getAllRuleNames();
	}
	
	public List<String> getEvaluableRules(){
		List<String> list = new ArrayList<String>();
		for(String rulename: getAllRuleNames()){
			if (getRule(rulename).canEvaluate()){
				list.add(rulename);
				System.out.println("Rule::"+rulename+"::Evaluable?:YES");
			} else {
				System.out.println("Rule::"+rulename+"::Evaluable?:NOT");
			}
		}
		if(list.size() == 0) list.add(EmptyRule.instance.getName());
		return list;
	}
	public List<String> getSelectableRules(){
		List<String> list = new ArrayList<String>();
		IRule rule;
		for (String rulename: getEvaluableRules()){
			rule = getRule(rulename);
			rule.evaluate();
			if (rule.isSelectable()) list.add(rulename);
		}
		return list;
	}
	public int timesUsed(String rulename, int ifNotFound){
		RuleUsageData rud = getAllRuleUsageData().get(rulename);
		if (rud != null){
			return rud.getTimesUsed();
		}
		else return getAllRuleNames().contains(rulename) ? 0: ifNotFound;
	}
	public void setEmpathicKernel(EmpathicKernel empathicKernel) {
		this.empathicKernel = empathicKernel;
	}
	public EmpathicKernel getEmpathicKernel() {
		return empathicKernel;
	}
}
