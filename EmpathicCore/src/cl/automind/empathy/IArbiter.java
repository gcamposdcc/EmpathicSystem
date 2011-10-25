package cl.automind.empathy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.automind.empathy.rule.AbstractRule;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.RuleUsageData;

public interface IArbiter {

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
	public abstract RuleUsageData getRuleUsageData(String rulename);

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
	public abstract int getRuleTriggeredCount();

	/**
	 * Gets the current valid rule, for doing this it first checks if the current
	 * valid rule is still valid using the {@code needsUpdate()} method.
	 * If the current rule is still valid it returns the same rule, otherwise
	 * it will update the rule by invoking the {@code updateValidRule()} method
	 * and then return the new valid {@link AbstractRule}
	 * @return the current valid rule
	 * @see AbstractRule
	 */
	public abstract IRule getValidRule();

	/**
	 * Updates the current valid rule. It operates as a <i>Template Method</i>
	 * chaining three other methods: {@code preUpdateValidRule()},
	 * {@code updateValidRuleImpl()}, {@code postUpdateValidRule()}.
	 */
	public abstract void updateValidRule();

	/**
	 *
	 */
	public abstract void preUpdateValidRule();

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

	public abstract IRule accept(ArbiterCriterion visitor);

	public abstract void setCriterion(ArbiterCriterion criterion);

	public abstract ArbiterCriterion getCriterion();

	public abstract IRule getRule(String rulename);

	public abstract Set<String> getAllRuleNames();

	public abstract List<String> getEvaluableRules();

	public abstract List<String> getSelectableRules();

	public abstract int timesUsed(String rulename, int ifNotFound);

	public abstract void setEmpathicKernel(EmpathicKernel empathicKernel);

	abstract EmpathicKernel getEmpathicKernel();

}