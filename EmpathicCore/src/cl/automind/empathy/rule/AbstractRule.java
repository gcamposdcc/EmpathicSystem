package cl.automind.empathy.rule;

import interfaces.structural.INamed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ArrayUtils;
import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.feedback.EmptyMessage;


/**
 * The base class for any empathic rule.
 * @author Guillermo
 */
public abstract class AbstractRule implements INamed, IRule{
	private AbstractMessage message = new EmptyMessage();
	private final Map<String, Object> valuesMap = new HashMap<String, Object>();
	private DataRuleMediator dataMediator;

	// <metadata-fields>
	private final String name;
	private final String[] strategies;
	private final double minValue;
	private final double maxValue;
	private final double threshold;
	private final double valueRange;
	// </metadata-fields>

	private double value;

	public AbstractRule(){
		RuleMetadata metadata = getClass().getAnnotation(RuleMetadata.class);
		// <metadata-fields-init>
		//TODO: Implement metadata corrections
		if (metadata != null){
			name = metadata.name().trim().equals("") ? getClass().getSimpleName().replaceAll("Rule", "") : metadata.name().trim();
		} else {
			name = getClass().getSimpleName().replaceAll("Rule", "");
		}
		strategies = metadata != null ? metadata.strategies() : RuleMetadata.STRATEGIES;
		minValue = metadata != null ? metadata.minVal() : RuleMetadata.MIN_VALUE;
		maxValue = metadata != null ? metadata.maxVal() : RuleMetadata.MAX_VALUE;
		threshold = metadata != null ? metadata.threshold() : RuleMetadata.THRESHOLD;
		valueRange = maxValue - minValue;
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
		return minValue;
	}
	protected double getMaxValue(){
		return maxValue;
	}
	protected double getThreshold(){
		return threshold;
	}
	protected double getValueRange(){
		return valueRange;
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#hasStrategy(java.lang.String)
	 */
	@Override
	public boolean hasStrategy(String strategyName){
		if (strategyName.equals(StrategyMetadata.DEFAULT)) return true;
		return ArrayUtils.contains(getStrategies(), strategyName);
	}
	// </metadata-fields-getters>
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#evaluateImpl()
	 */
	@Override
	public abstract double evaluateImpl();
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#evaluate(java.lang.Object)
	 */
	@Override
	public final double evaluate(Object... params){
		setValue(evaluateImpl());
		pushValues();
		return getValue();
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
//		System.out.println("Rule::"+getName()+"::Selectable?::"+normalize(value)+">="+normalize(threshold)+"?");
		return normalize(getValue()) >= normalize(getThreshold());
	}
	protected final double normalize(double d){
		if (d < getMinValue()) return 0.0;
		if (d > getMaxValue()) return 1.0;
		return (d - getMinValue())/getValueRange();
	}

	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getMessage()
	 */
	@Override
	public AbstractMessage getMessage(){
		return message;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#setMessage(cl.automind.empathy.feedback.AbstractMessage)
	 */
	@Override
	public void setMessage(AbstractMessage message){
		this.message = message;
	}

	protected Object getValueByIdInSource(String dataSourceName, int id){
		return getDataMediator().getValueById(dataSourceName, id);
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
	public Map<String, Object> getValuesMap() {
		return valuesMap;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#clearValues()
	 */
	@Override
	public void clearValues(){
		getValuesMap().clear();
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#putValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putValue(String key, Object value){
		getValuesMap().put(key, value);
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#removeValue(java.lang.String)
	 */
	@Override
	public void removeValue(String key){
		getValuesMap().remove(key);
	}
	private void pushValues(){
//		System.out.println("Rule::"+getName()+"::PushValues::Start");
//		for (Map.Entry<String, Object> entry: getValuesMap().entrySet()){
//			System.out.println("Rule::"+getName()+"::PushValues::Key::"+entry.getKey()+"::Value::"+entry.getValue());
//		}
//		System.out.println("Rule::"+getName()+"::PushValues::End");
		getMessage().setDefaultValues(getValuesMap());
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#setDataMediator(cl.automind.empathy.rule.DataRuleMediator)
	 */
	@Override
	public void setDataMediator(DataRuleMediator dataMediator) {
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
//		System.out.println("Rule::"+getName()+"::SetValue::"+value);
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see cl.automind.empathy.rule.IRule#getValue()
	 */
	@Override
	public double getValue() {
//		System.out.println("Rule::"+getName()+"::GetValue::"+value);
		return value;
	}

}
