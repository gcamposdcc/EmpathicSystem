package cl.automind.empathy.data;


public class QueryCriterion<T> implements IQueryCriterion<T> {
	private T target = null;
	private NamedValuePair<?>[] params = {};

	public QueryCriterion(){ }

	public QueryCriterion(T target, NamedValuePair<?>... params){
		this.target = target;
		this.params = params;
	}
	@Override
	public boolean apply() {
		return apply(getTarget(), getParams());
	}

	@Override
	public boolean apply(T target) {
		return apply(target, getParams());
	}
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
