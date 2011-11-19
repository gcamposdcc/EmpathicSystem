package cl.automind.empathy.data;

import util.NamedValuePair;

public abstract class AbstractQueryCriterion<T> implements IQueryCriterion<T> {
	private T target = null;
	private NamedValuePair<?>[] params = {};

	public AbstractQueryCriterion(){ }

	public AbstractQueryCriterion(T target, NamedValuePair<?>... params){
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
	@Override
	public boolean apply(T target, util.NamedValuePair<?>... params) {
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
