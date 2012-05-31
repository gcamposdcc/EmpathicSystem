package cl.automind.empathy.rule;

@RuleMetadata(name = "EMPATHIC_FRAMEWORK_EMPTY_RULE")
public final class EmptyRule extends AbstractRule {
	public final static AbstractRule INSTANCE = new EmptyRule();
	private final Object[] params = {};
	private EmptyRule(){ 
		super(); 
	}
	@Override
	public double evaluateImpl(Object... params) {
		return 0;
	}

	@Override
	public boolean canEvaluate(Object... params) {
		return false;
	}
	@Override
	public void setParams(Object... params) {
		// do nothing
	}
	@Override
	public Object[] getParams() {
		return params;
	}

}
