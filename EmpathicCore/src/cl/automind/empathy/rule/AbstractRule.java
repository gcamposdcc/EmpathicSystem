package cl.automind.empathy.rule;

import gcampos.dev.interfaces.structural.INamed;
import gcampos.dev.util.ArrayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;
import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.feedback.EmptyMessage;


/**
 * The base class for any empathic rule.
 * @author Guillermo
 */
public abstract class AbstractRule implements INamed, IRule{
	private AbstractMessage message = new EmptyMessage();
	private final Map<String, Object> messageValueMap = new HashMap<String, Object>();
	private DataRuleMediator dataMediator;
	private Object[] params = {};
	private final AbstractRule.ValueHolder ruleValues;

	// <metadata-fields>
	private final String name;
	private final String[] strategies;
	// </metadata-fields>

	private double value;

	public AbstractRule(){
		Class<?> thisClass = getClass();
		ruleValues = new AbstractRule.ValueHolder();
		if (thisClass.isAnonymousClass()){
			thisClass = thisClass.getSuperclass();
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("AnonymousClassDetected::Parent::" + thisClass.getSimpleName());
		}
		RuleMetadata metadata = thisClass.getAnnotation(RuleMetadata.class);
		// <metadata-fields-init>
		//TODO: Implement metadata corrections
		if (metadata != null){
			name = metadata.name().trim().equals("") ? thisClass.getSimpleName().replaceAll("Rule", "") : metadata.name().trim();
		} else {
			name = thisClass.getSimpleName().replaceAll("Rule", "");
		}
		strategies = metadata != null ? metadata.strategies() : RuleMetadata.STRATEGIES;
		getRuleValues().setMinValue(metadata != null ? metadata.minVal() : RuleMetadata.MIN_VALUE);
		getRuleValues().setMaxValue(metadata != null ? metadata.maxVal() : RuleMetadata.MAX_VALUE);
		getRuleValues().setThreshold(metadata != null ? metadata.threshold() : RuleMetadata.THRESHOLD);
		// </metadata-fields-init>
	}
	public AbstractRule(DataRuleMediator dataMediator){
		this();
		setDataMediator(dataMediator);
	}

	// <metadata-fields-getters>
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getName()
	 */
	@Override
	public String getName(){
		return name;
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getStrategies()
	 */
	@Override
	public String[] getStrategies(){
		return strategies;
	}
	protected double getMinValue(){
		return getRuleValues().getMinValue();
	}
	protected double getMaxValue(){
		return getRuleValues().getMaxValue();
	}
	protected double getThreshold(){
		return getRuleValues().getThreshold();
	}
	protected double getValueRange(){
		return getRuleValues().getValueRange();
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#hasStrategy(java.lang.String)
	 */
	@Override
	public boolean hasStrategy(String strategyName){
		if (strategyName.equals(StrategyMetadata.DEFAULT)) {
			return true;
		} else {
			return ArrayUtils.contains(getStrategies(), strategyName);
		}
	}
	// </metadata-fields-getters>
	protected abstract double evaluateImpl(Object... params);
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#evaluate(java.lang.Object)
	 */
	@Override
	public final double evaluate(Object... params){
		double eval = 0;
//		try{
			eval = evaluateImpl(params);
//		} catch (Exception e){
//
//		}
		setValue(eval);
		pushValues();
		return getLastEvaluationValue();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#canEvaluate(java.lang.Object)
	 */
	@Override
	public abstract boolean canEvaluate(Object... params);
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#isSelectable()
	 */
	@Override
	public final boolean isSelectable(){
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Rule::" + getName() +
				"Value:" + normalize(getLastEvaluationValue()) +
				"::Threshold:" + normalize(getThreshold()));
		return normalize(getLastEvaluationValue()) >= normalize(getThreshold());
	}
	protected final double normalize(double value){
		if (value < getMinValue()) {
			return 0.0;
		} else if (value > getMaxValue()) {
			return 1.0;
		} else {
			return (value - getMinValue())/getValueRange();
		}
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getMessage()
	 */
	@Override
	public AbstractMessage getMessage(){
		if (message != null) message.getContext().setCallingRuleName(getName());
		return message;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#setMessage(cl.automind.empathy.feedback.AbstractMessage)
	 */
	@Override
	public void setMessage(AbstractMessage message){
		this.message = message;
		getMessage().getContext().setCallingRuleName(getName());
	}

	protected <T> List<T> getInSource(String dataSourceName, IQueryOption option, IQueryCriterion<T>... criteria){
		return getDataMediator().getValue(dataSourceName, option, criteria);
	}
	protected <T> List<T> getAllInSource(String dataSourceName, T template){
		return getDataMediator().getAllInSource(dataSourceName, template);
	}
	protected int countAllInSource(String dataSourceName){
		return getDataMediator().countElements(dataSourceName);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getValuesMap()
	 */
	@Override
	public Map<String, Object> getMessageValuesMap() {
		return messageValueMap;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#clearValues()
	 */
	@Override
	public void clearMessageValues(){
		getMessageValuesMap().clear();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#putValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putMessageValue(String key, Object value){
		getMessageValuesMap().put(key, value);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#removeValue(java.lang.String)
	 */
	@Override
	public void removeMessageValue(String key){
		getMessageValuesMap().remove(key);
	}
	private void pushValues(){
		getMessage().setDefaultValues(getMessageValuesMap());
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#setDataMediator(cl.automind.empathy.rule.DataRuleMediator)
	 */
	@Override
	public final void setDataMediator(DataRuleMediator dataMediator) {
		this.dataMediator = dataMediator;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getDataMediator()
	 */
	@Override
	public DataRuleMediator getDataMediator() {
		return dataMediator;
	}
	protected void setValue(double value) {
//		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Rule::"+getName()+"::SetValue::"+value);
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getValue()
	 */
	@Override
	public double getLastEvaluationValue() {
//		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Rule::"+getName()+"::GetValue::"+value);
		return value;
	}
	@Override
	public void setParams(Object... params) {
		this.params = params;
	}
	@Override
	public Object[] getParams() {
		return params;
	}

	protected AbstractRule.ValueHolder getRuleValues() {
		return ruleValues;
	}

	public static class ValueHolder{
		private double minValue;
		private double maxValue;
		private double threshold;
		public void setMinValue(double minValue) {
			this.minValue = minValue;
		}
		public double getMinValue() {
			return minValue;
		}
		public void setMaxValue(double maxValue) {
			this.maxValue = maxValue;
		}
		public double getMaxValue() {
			return maxValue;
		}
		public void setThreshold(double threshold) {
			this.threshold = threshold;
		}
		public double getThreshold() {
			return threshold;
		}
		public double getValueRange() {
			return maxValue - minValue;
		}


	}

}
