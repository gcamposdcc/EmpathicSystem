package cl.automind.empathy.data;


public class QueryCriterion<T> implements IQueryCriterion<T> {
	private T target = null;
	private NamedValuePair<?>[] params = {};

	public QueryCriterion(){ }

	public QueryCriterion(T target, NamedValuePair<?>... params){
		setTarget(target);
		setParams(params);
	}
	@Override
	public boolean apply() {
		return apply(getTarget(), getParams());
	}

	@Override
	public boolean apply(T target) {
		return apply(target, getParams());
	}
//	@Override
//	public boolean apply(T target, NamedValuePair<?> pair){
//		return true;
//	}
//	@Override
//	public boolean apply(T target, NamedValuePair<?> pair00, NamedValuePair<?> pair01){
//		return true;
//	}
//	@Override
//	public boolean apply(T target, NamedValuePair<?> pair00, NamedValuePair<?> pair01, NamedValuePair<?> pair02){
//		return true;
//	}
	@Override
	public boolean apply(T target, cl.automind.empathy.data.NamedValuePair<?>... params) {
		return true;
	}

	@Override
	public void setTarget(T target) {
		this.target = target;
	}

	@Override
	public T getTarget() {
		return target;
	}

	@Override
	public void setParams(NamedValuePair<?>... params) {
		this.params = params;
	}

	@Override
	public NamedValuePair<?>[] getParams() {
		return params;
	}

}
