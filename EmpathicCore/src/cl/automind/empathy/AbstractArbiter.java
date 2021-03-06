package cl.automind.empathy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import cl.automind.empathy.rule.EmptyRule;
import cl.automind.empathy.rule.IRule;
import cl.automind.empathy.rule.RuleUsageData;

/**
 * The class 'responsible' for selecting the {@link IRule} that will generate
 * the {@link AbstractMessage} containing the feedback message for the user.
 * Due to the fact that almost all the job is done in this {@link IArbiter}'s 
 * {@link IArbiterCriterion}, this class is basically a <i>mediator</i>
 * between an {@link EmpathicKernel} and an {@link IArbiterCriterion}.
 * Besides that, this class keeps track of the different rules selected over time.
 * The communication between this arbiter and its criterions is done using the
 * <i>visitor</i> pattern.
 * @author Guillermo
 *
 */
public abstract class AbstractArbiter implements IArbiter{
	/**
	 * The current selected {@link IRule}
	 */
	private IRule validRule;
	/**
	 * The current preferred {@link IArbiterCriterion}
	 */
	private IArbiterCriterion criterion;
	/**
	 * The current responsible {@link EmpathicKernel}
	 */
	private EmpathicKernel empathicKernel;
	/**
	 * Creates an empty arbiter with no empathic kernel and no criterion.
	 */
	protected AbstractArbiter(){

	}
	/**
	 * Creates an arbiter using the given {@link EmpathicKernel} and 
	 * {@link IArbiterCriterion}.
	 * @param kernel The kernel from where the rules are extracted
	 * @param criterion The criterion for rule selection
	 */
	protected AbstractArbiter(EmpathicKernel kernel, IArbiterCriterion criterion){
		setEmpathicKernel(kernel);
		setCriterion(criterion);
	}

	@Override
	public abstract Map<String, RuleUsageData> getAllRuleUsageData();

	@Override
	public RuleUsageData getRuleUsageData(String rulename) {
		return getAllRuleUsageData().get(rulename);
	}

	@Override
	public abstract List<String> getRuleUsageList();

	@Override
	public abstract int getDifferentRulesUsedCount();

	@Override
	public int getRuleTriggeredCount(){
		return getRuleUsageList().size();
	}

	@Override
	public final IRule getValidRule(){
		if (needsUpdate()){
			updateValidRule();
		}
		return getCurrentValidRule();
	}
	/**
	 * Gets the current valid rule. This method does not check if the
	 * rule needs to be updated nor it updates the rule (and therefore
	 * it doesn't evaluate any rule).
	 * @return the current valid rule
	 */
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

	@Override
	public final void updateValidRule(){
		preUpdateValidRule();
		setValidRule(applyCriterion());
		postUpdateValidRule();
	}

	@Override
	public abstract void preUpdateValidRule();
	/**
	 * Calls the preferred {@link IArbiterCriterion}
	 * @return the rule selected by the criterion
	 */
	private final IRule applyCriterion(){
		return accept(getCriterion());
	}

	@Override
	public abstract void postUpdateValidRule();

	@Override
	public abstract boolean needsUpdate();

	@Override
	public IRule accept(IArbiterCriterion visitor){
		return visitor.visit(this);
	}

	@Override
	public final void setCriterion(IArbiterCriterion criterion){
		this.criterion = criterion;
	}

	@Override
	public IArbiterCriterion getCriterion(){
		return criterion;
	}

	@Override
	public IRule getRule(String rulename){
		return getEmpathicKernel().getRule(rulename);
	}

	@Override
	public Set<String> getAllRuleNames(){
		return getEmpathicKernel().getAllRuleNames();
	}

	@Override
	public List<String> getEvaluableRules(){
		List<String> list = new ArrayList<String>();
		IRule rule;
		for(String rulename: getAllRuleNames()){
			rule = getRule(rulename);
			try{
				if (rule.canEvaluate(rule.getParams())){
					list.add(rulename);
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Rule::"+rulename+"::Evaluable?:YES");
				} else {
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Rule::"+rulename+"::Evaluable?:NOT");
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		if(list.size() == 0) {
			list.add(EmptyRule.INSTANCE.getName());
		}
		return list;
	}

	@Override
	public List<String> getSelectableRules(){
		List<String> list = new ArrayList<String>();
		IRule rule;
		for (String rulename: getEvaluableRules()){
			try{
				rule = getRule(rulename);
				rule.evaluate(rule.getParams());
				if (rule.isSelectable()) {
					list.add(rulename);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int timesSelected(String rulename, int ifNotFound){
		RuleUsageData rud = getAllRuleUsageData().get(rulename);
		if (rud != null){
			return rud.getTimesUsed();
		} else {
			return getAllRuleNames().contains(rulename) ? 0: ifNotFound;
		}
	}

	@Override
	public final void setEmpathicKernel(EmpathicKernel empathicKernel) {
		this.empathicKernel = empathicKernel;
	}

	@Override
	public	EmpathicKernel getEmpathicKernel() {
		return empathicKernel;
	}
}
