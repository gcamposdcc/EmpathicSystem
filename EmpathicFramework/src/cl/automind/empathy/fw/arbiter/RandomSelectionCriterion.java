package cl.automind.empathy.fw.arbiter;

import java.util.Random;

import cl.automind.empathy.AbstractArbiter;
import cl.automind.empathy.ArbiterCriterion;
import cl.automind.empathy.rule.IRule;

public class RandomSelectionCriterion extends ArbiterCriterion {
	private Random random = new Random();
	@Override
	public IRule visit(AbstractArbiter arbiter) {
		String[] rulenames = arbiter.getAllRuleNames().toArray(new String[]{});
		return arbiter.getRule(rulenames[random.nextInt(rulenames.length)]);
	}

}
