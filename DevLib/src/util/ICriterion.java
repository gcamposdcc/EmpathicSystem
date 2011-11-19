package util;

public interface ICriterion<T> {
	public boolean apply(T target, Object... params);
}
