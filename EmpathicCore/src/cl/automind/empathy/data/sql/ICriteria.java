package cl.automind.empathy.data.sql;

public interface ICriteria<T> {
	public boolean apply(T target, Object... params);
}
