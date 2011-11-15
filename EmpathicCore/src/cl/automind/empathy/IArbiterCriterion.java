package cl.automind.empathy;

import cl.automind.empathy.rule.IRule;


public interface IArbiterCriterion{
	public abstract IRule visit(IArbiter arbiter);

}
