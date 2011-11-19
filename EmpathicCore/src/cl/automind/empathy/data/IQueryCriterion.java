package cl.automind.empathy.data;

import util.NamedValuePair;

public interface IQueryCriterion<T> {
	public boolean apply();
	public boolean apply(T target);
	public boolean apply(T target, NamedValuePair<?>... params);
	public void setTarget(T target);
	public T getTarget();
	public void setParams(NamedValuePair<?>... params);
	public NamedValuePair<?>[] getParams();
}
