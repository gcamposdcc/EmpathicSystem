package cl.automind.empathy.data;

import gcampos.dev.interfaces.structural.INamed;
import gcampos.dev.patterns.behavioral.IObservable;

import java.util.Collection;
import java.util.List;


public interface IDataSource<T> extends IObservable<IDataSource<T>>, INamed{

	T getTemplate();
	// CREATE
	int insert(T value);
	List<Integer> insert(Collection<T> value);

	// READ
	List<T> select(IQueryOption option, IQueryCriterion<T>...criteria);

	// UPDATE
	int update(T value, IQueryOption option, IQueryCriterion<T>...criteria);

	// DELETE
	boolean delete(IQueryOption option, IQueryCriterion<T>...criteria);

	// CLEAR
	void clear();

	// UTILS
	int count();

	Type getType();

	public enum Type{
		Memory, Web, Sql
	}
}
