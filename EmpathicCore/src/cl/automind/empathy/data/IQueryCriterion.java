package cl.automind.empathy.data;


public interface IQueryCriterion<T> {
	boolean apply();
	boolean apply(T target);
	boolean apply(T target, NamedValuePair<?>... params);
	void setTarget(T target);
	T getTarget();
	void setParams(NamedValuePair<?>... params);
	NamedValuePair<?>[] getParams();
}
