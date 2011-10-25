package cl.automind.empathy.fw.arbiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import cl.automind.empathy.ArbiterCriterion;
import cl.automind.empathy.IArbiter;
import cl.automind.empathy.rule.EmptyRule;
import cl.automind.empathy.rule.IRule;

public class LessUsedCriterion extends ArbiterCriterion{

	@Override
	public IRule visit(IArbiter arbiter) {
		return arbiter.getRule(getLessUsedRuleName(arbiter, arbiter.getSelectableRules()));
	}
	public String getLessUsedRuleName(IArbiter arbiter, Collection<String> rulenames){
		int min = Integer.MAX_VALUE;
		int current = 0;
		List<String> l_rulenames = new ArrayList<String>();
		for (String rulename: rulenames){
			if (rulename.equals(EmptyRule.instance.getName())) continue;
			current = arbiter.timesUsed(rulename, Integer.MAX_VALUE);
			System.out.println("Rule::"+rulename+"::Used::"+current);
			if (current < min){
				min = current;
				l_rulenames.clear();
				l_rulenames.add(rulename);
			} else if (current == min){
				l_rulenames.add(rulename);
			}
		}
		System.out.println("LessUsedRules");
		for (String rulename: rulenames){
			System.out.println("-- "+rulename);
		}
		switch(l_rulenames.size()){
		case 0:
			return EmptyRule.instance.getName();
		case 1:
			return l_rulenames.get(0);
		default:
			return l_rulenames.get((new Random()).nextInt(l_rulenames.size()));
		}
	}

}