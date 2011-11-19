package cl.automind.empathy.data;

import interfaces.structural.INamed;

import java.util.Collection;
import java.util.List;

import patterns.behavioral.IObservable;

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
	boolean validId(int id);
	int count();

	public Type getType();

	public enum Type{
		Memory, Web, Sql
	}
}
