package cl.automind.empathy.data;

import gcampos.dev.util.ICriterion;

public interface IQueryable<T> {
	public void apply(ICriterion<T>...criteria);
}
