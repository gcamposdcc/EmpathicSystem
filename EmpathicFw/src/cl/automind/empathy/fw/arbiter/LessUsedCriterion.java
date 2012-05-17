package cl.automind.empathy.fw.arbiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import cl.automind.empathy.IArbiter;
import cl.automind.empathy.IArbiterCriterion;
import cl.automind.empathy.rule.EmptyRule;
import cl.automind.empathy.rule.IRule;

public class LessUsedCriterion implements IArbiterCriterion{

	@Override
	public IRule visit(IArbiter arbiter) {
		return arbiter.getRule(getLessUsedRuleName(arbiter, arbiter.getSelectableRules()));
	}
	public String getLessUsedRuleName(IArbiter arbiter, Collection<String> rulenames){
		int min = Integer.MAX_VALUE;
		int current = 0;
		List<String> l_rulenames = new ArrayList<String>();
		for (String rulename: rulenames){
			if (rulename.equals(EmptyRule.INSTANCE.getName())) continue;
			current = arbiter.timesSelected(rulename, Integer.MAX_VALUE);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Rule::"+rulename+"::Used::"+current);
			if (current < min){
				min = current;
				l_rulenames.clear();
				l_rulenames.add(rulename);
			} else if (current == min){
				l_rulenames.add(rulename);
			}
		}
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("LessUsedRules");
		for (String rulename: rulenames){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("-- "+rulename);
		}
		switch(l_rulenames.size()){
		case 0:
			return EmptyRule.INSTANCE.getName();
		case 1:
			return l_rulenames.get(0);
		default:
			return l_rulenames.get((new Random()).nextInt(l_rulenames.size()));
		}
	}

}
