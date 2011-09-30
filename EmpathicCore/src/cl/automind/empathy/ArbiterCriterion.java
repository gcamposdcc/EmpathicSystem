package cl.automind.empathy;

import cl.automind.empathy.rule.IRule;


public abstract class ArbiterCriterion{
	
	public abstract IRule visit(AbstractArbiter arbiter);

}
