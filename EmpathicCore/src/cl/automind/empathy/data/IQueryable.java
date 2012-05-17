package cl.automind.empathy.data;

import gcampos.dev.util.ICriterion;

public interface IQueryable<T> {
	void apply(ICriterion<T>...criteria);
}
