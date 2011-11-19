package cl.automind.empathy.data;

import util.ICriterion;

public interface IQueryable<T> {
	public void apply(ICriterion<T>...criteria);
}
