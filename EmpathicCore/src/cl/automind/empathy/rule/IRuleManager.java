package cl.automind.empathy.rule;

import java.util.Set;

public interface IRuleManager {

	public abstract void registerRule(String ruleName, IRule rule);

	public abstract IRule getRule(String ruleName);

	public abstract Set<String> getAllRulenames();

	public abstract void printElements();

}