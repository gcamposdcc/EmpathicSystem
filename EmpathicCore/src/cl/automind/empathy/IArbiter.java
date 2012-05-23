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
    Map<String, RuleUsageData> getAllRuleUsageData();

    /**
     * Gets the {@link RuleUsageData} associated with the rule with the name
     * <i>rulename</i>
     * @param rulename The name of the rule to be looked up
     * @return The {@link RuleUsageData} associated to the rule. Returns
     * <i>null</i> if the rule doesn't exist.
     */
    RuleUsageData getRuleUsageData(String rulename);

    /**
     * Gets a {@link List} containing the list of the names of the rules marked
     * as valid rules. This {@link List} <i>should</i> be ordered. It's
     * encouraged to use a "first ocurrence elements go first" fashion.
     * @return a collection with the name of the rules used
     */
    List<String> getRuleUsageList();

    /**
     * Gets the amount of different rules that have been marked as valid. As such,
     * repeated uses of the same rule are <b>not</b> be counted.
     * @return the number of different rules that have been marked as valid
     */
    int getDifferentRulesUsedCount();

    /**
     * Gets the total amount of rules marked as valid. As such, repeated uses of the same
     * rule <b>are</b> counted.
     * @return the number of times that a rule has been marked as
     */
    int getRuleTriggeredCount();

    /**
     * Gets the current valid rule, for doing this it first checks if the current
     * valid rule is still valid using the {@code needsUpdate()} method.
     * If the current rule is still valid it returns the same rule, otherwise
     * it will update the rule by invoking the {@code updateValidRule()} method
     * and then return the new valid {@link AbstractRule}
     * @return the current valid rule
     * @see AbstractRule
     */
    IRule getValidRule();

    /**
     * Updates the current valid rule. It operates as a <i>Template Method</i>
     * chaining three other methods: {@code preUpdateValidRule()},
     * {@code updateValidRuleImpl()}, {@code postUpdateValidRule()}.
     */
    void updateValidRule();

    /**
     * This method is for handling before calling the {@link IArbiterCriterion}
     * and selecting the current valid rule.
     */
    void preUpdateValidRule();

    /**
     * This method is for additional handling after the new valid rule
     * has been updated.
     */
    void postUpdateValidRule();

    /**
     * Determines if the current valid rule needs to be updated
     * @return <b>true</b> if the current valid rule needs to be updated
     */
    boolean needsUpdate();

    /**
     * Accepts a criterion for selecting the next valid {@link IRule}
     * @param visitor
     * @return the rule to be used for the next Message
     */
    IRule accept(IArbiterCriterion visitor);
    /**
     * Sets the current preferred {@link IArbiterCriterion} to
     * evaluate and priorize the {@link IRule}
     * @param criterion
     */
    void setCriterion(IArbiterCriterion criterion);
    /**
     * Gets the current preferred {@link IArbiterCriterion}
     * @return The current preferred {@link IArbiterCriterion}
     */
    IArbiterCriterion getCriterion();

    /**
     * Gets the {@link IRule} of the specified name.
     * @param rulename The name of the {@link IRule} to be looked up.
     * @return
     */
    IRule getRule(String rulename);
    /**
     * Gets a {@link Set} of {@link String} containing the names of all the {@link IRule}
     * @return The names of all the rules
     */
    Set<String> getAllRuleNames();
    /**
     * Gets all the rules that match their evaluable criterion.
     * A rule is marked as evaluable when their {@code canEvaluate()} method returns true.
     * @return all rules that match their evaluable criterion
     */
    List<String> getEvaluableRules();
    /**
     * Gets all the rules that match their selectable criterion.
     * A rule is marked as selectable when their {@code isSelectable()} method returns true.
     * @return all rules that match their selectable criterion
     */
    List<String> getSelectableRules();
    /**
     * Get the number of times a rule has been selected. The rule is looked up depending
     * on the specified rulename. A rule is selected after being returned by the
     * {@code visit()} method of this {@link IArbiter}'s {@link IArbiterCriterion}.
     * @param rulename the name of the rule to be looked up
     * @param ifNotFound the value to be returned in case the rule isn't found
     * @return the number of times that the rule of name rulename has been selected
     */
    int timesSelected(String rulename, int ifNotFound);
    /**
     * Sets the {@link EmpathicKernel} responsible for this arbiter.
     * @param empathicKernel the kernel
     */
    void setEmpathicKernel(EmpathicKernel empathicKernel);
    /**
     * Gets the {@link EmpathicKernel} responsible for this arbiter.
     * @return
     */
    EmpathicKernel getEmpathicKernel();

}
