package cl.automind.empathy.rule;

import java.util.Set;

public interface IRuleManager {

	void registerRule(String ruleName, IRule rule);

	IRule getRule(String ruleName);

	Set<String> getAllRulenames();

	void printElements();

}