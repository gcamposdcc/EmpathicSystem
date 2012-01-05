package cl.automind.empathy.data;


public interface IQueryCriterion<T> {
	public boolean apply();
	public boolean apply(T target);
//	public boolean apply(T target, NamedValuePair<?> pair);
//	public boolean apply(T target, NamedValuePair<?> pair00, NamedValuePair<?> pair01);
//	public boolean apply(T target, NamedValuePair<?> pair00, NamedValuePair<?> pair01, NamedValuePair<?> pair02);
	public boolean apply(T target, NamedValuePair<?>... params);
	public void setTarget(T target);
	public T getTarget();
	public void setParams(NamedValuePair<?>... params);
	public NamedValuePair<?>[] getParams();
}
