package cl.automind.empathy.data;

import interfaces.behavioral.IDisposable;

import java.util.List;

import patterns.behavioral.IObserver;



public abstract class AbstractDataManager{
	/// ABSTRACT METHODS
	abstract public IDataSource<?> getDataSource(String dataSourceName);
	abstract public <T> IDataSource<T> getDataSource(String dataSourceName, T template);
	abstract public <T> boolean registerDataSource(String dataSourceName, IDataSource<T> dataSource);
	abstract public <T> IDataSource<T> createDataSource(String dataSourceName, T template);
	abstract public boolean createSpaceIfNotFound();

	protected <T> IDisposable suscribeDataSourceObserver(String dataSourceName, IObserver<IDataSource<T>> observer){
		IDataSource<T> source = null;
		try{
			source = (IDataSource<T>) getDataSource(dataSourceName);
		} catch (Exception e){
			return null;
		}
		if (source == null) return null;
		return source.suscribe(observer);
	}

	public <T> int pushValue(String dataSourceName, T value){
		return getDataSource(dataSourceName, value).insert(value);
	}
	public <T> int updateValue(String dataSourceName, int id, T value){
		return getDataSource(dataSourceName, value).updateById(id, value);
	}

	public Object getValueById(String dataSourceName, int id){
		return getDataSource(dataSourceName).selectById(id);
	}
	public <T> List<T> getAll(String dataSourceName, T template){
		return getDataSource(dataSourceName, template).selectAll();
	}

	public int countElements(String dataSourceName){
		IDataSource<?> datasource = getDataSource(dataSourceName);
		return datasource != null ? datasource.count() : 0;
	}

}
