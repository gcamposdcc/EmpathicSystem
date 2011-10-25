package cl.automind.empathy.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import patterns.creational.FlyweightNamedFactory;



public class AbstractRuleManager implements IRuleManager{

	private final EmpathicRuleFactory ruleFactory;
	protected AbstractRuleManager(){
		ruleFactory = initializeRuleFactory();
	}
	protected EmpathicRuleFactory initializeRuleFactory(){
		return new EmpathicRuleFactory();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRuleManager#registerRule(java.lang.String, cl.automind.empathy.rule.IRule)
	 */
	@Override
	public void registerRule(String ruleName, IRule rule){
		getRuleFactory().registerElement(ruleName, rule);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRuleManager#getRule(java.lang.String)
	 */
	@Override
	public IRule getRule(String ruleName){
		return getRuleFactory().createElement(ruleName);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRuleManager#getAllRulenames()
	 */
	@Override
	public Set<String> getAllRulenames(){
		return getRuleFactory().getAllRulenames();
	}
	protected EmpathicRuleFactory getRuleFactory(){
		return ruleFactory;
	}
	protected Collection<IRule> ruleByStrategy(String strategyName){
		return getRuleFactory().getRulesByStrategy(strategyName);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRuleManager#printElements()
	 */
	@Override
	public void printElements(){
		getRuleFactory().printElements();
	}

	protected class EmpathicRuleFactory extends FlyweightNamedFactory<IRule>{

		@Override
		public Map<String, IRule> initializeMap() {
			return new Hashtable<String, IRule>();
		}
		public Collection<IRule> getRulesByStrategy(String strategyName){
			Collection<IRule> rules = getRegistry().values();
			Collection<IRule> selectedRules = new ArrayList<IRule>();
			for (IRule rule : rules){
				if (rule.hasStrategy(strategyName)) {
					selectedRules.add(rule);
				}
			}
			return selectedRules;
		}
		public Set<String> getAllRulenames(){
			System.out.println("GettingAllRuleNames");
			return getRegistry().keySet();
		}
	}
}
