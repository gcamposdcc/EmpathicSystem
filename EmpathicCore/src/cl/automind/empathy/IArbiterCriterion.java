package cl.automind.empathy;

import cl.automind.empathy.rule.IRule;


public interface IArbiterCriterion{
	/**
	 * Selects the rule to be used for feedback. This criterion's
	 * way to select the rule should be independent of the IArbiter
	 * implementation and rely on the info that the {@link IArbiter}
	 * will provide with it's methods.
	 * Specific methods aimed for this task are the {@code isEvaluable()} and
	 * {@code isSelectable()} methods (to name a few) of {@link IArbiter}.
	 * @param arbiter the arbiter of the rules
	 * @return the {@link IRule} to be used for feedback.
	 */
	IRule visit(IArbiter arbiter);

}
