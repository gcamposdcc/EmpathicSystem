package cl.automind.empathy.data;

import interfaces.structural.INamed;

import java.util.Collection;
import java.util.List;

import patterns.behavioral.IObservable;

public interface IDataSource<T> extends IObservable<IDataSource<T>>, INamed{
	// CREAIDataE
	int insert(T value);
	List<Integer> insert(Collection<T> value);
	// READ
	T selectById(int id);
	List<T> selectAll();
	// UPDAIDataE
	int updateById(int id, T value);
	// DELEIDataE
	boolean deleteById(int id);
	// CLEAR
	void clear();
	// UTILS
	boolean validId(int id);
	int count();
}
