package cl.automind.empathy.fw.arbiter;

import gcampos.dev.patterns.creational.FlyweightNamedFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cl.automind.empathy.IArbiter;
import cl.automind.empathy.IArbiterCriterion;
import cl.automind.empathy.rule.IRule;

public class WeightedCriterion implements IArbiterCriterion {
	private Collection<WFunction> weightFunctions;
	
	public WeightedCriterion(){
		setWeightFunctions(new ArrayList<WFunction>());
	}
	
	public WeightedCriterion(Collection<WFunction> functions){
		setWeightFunctions(functions != null ? functions : new ArrayList<WFunction>());
	}
	
	@Override
	public IRule visit(IArbiter arbiter) {
		// TODO Auto-generated method stub
		return null;
	}
	public Collection<WFunction> getWeightFunctions() {
		return weightFunctions;
	}
	public void setWeightFunctions(Collection<WFunction> weightFunctions) {
		this.weightFunctions = weightFunctions;
	}
	
	
	public static class WFunction {
		
		private final FlyweightNamedFactory<Double> values;
		public WFunction(){
			values = new FlyweightNamedFactory<Double>() {
				@Override
				public Map<String, Double> initializeMap() {
					return new HashMap<String, Double>();
				}
			};
		}
		
		public double getWeight(String rulename){
			Double d = getValues().createElement(rulename);
			return d != null ? d : 1.0;
		}
		
		public double getWeight(IRule rule){
			return getWeight(rule.getName());
		}
		public Map<IRule,Double> applyOnRules(Collection<IRule> rules, Map<IRule, Double> values){
			Map<IRule, Double> result = new HashMap<IRule, Double>();
			Double value = null;
			for (IRule rule: rules){
				value = values.get(rule);
				result.put(rule, apply(rule, value != null ? value : 1.0));
			}
			return result;
		}
		public Map<String,Double> applyOnRulenames(Collection<String> rules, Map<String, Double> values){
			Map<String, Double> result = new HashMap<String, Double>();
			Double value = null;
			for (String rule: rules){
				value = values.get(rule);
				result.put(rule, apply(rule, value != null ? value : 1.0));
			}
			return result;
		}
		public double apply(IRule rule, double value){
			return getWeight(rule) * value;
		}

		public double apply(String rule, double value){
			return getWeight(rule) * value;
		}

		private FlyweightNamedFactory<Double> getValues() {
			return values;
		}
	}
}
