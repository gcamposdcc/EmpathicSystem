package cl.automind.empathy.fw.data;

import gcampos.dev.interfaces.behavioral.IDisposable;
import gcampos.dev.patterns.behavioral.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cl.automind.empathy.data.IDataManager;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;
import cl.automind.empathy.data.QueryOptions;
import cl.automind.empathy.data.UnmatchingClassException;



public abstract class AbstractDataManager implements IDataManager{
	/// ABSTRACT METHODS
	abstract protected IDataSource<?> getDataSource(String dataSourceName);
	@Override
	abstract public <T> IDataSource<T> getDataSource(String dataSourceName, T template) throws UnmatchingClassException;
	@Override
	abstract public <T> boolean registerDataSource(String dataSourceName, IDataSource<T> dataSource);
	@Override
	abstract public <T> IDataSource<T> createDataSource(String dataSourceName, T template);
	@Override
	abstract public boolean createSpaceIfNotFound();

	@SuppressWarnings("unchecked")
	protected <T> IDisposable suscribeDataSourceObserver(String dataSourceName, IObserver<IDataSource<T>> observer){
		IDataSource<T> source = null;
		try{
			source = (IDataSource<T>) getDataSource(dataSourceName);
		} catch (Exception e){
			return null;
		}
		return source != null ? source.suscribe(observer) : null;
	}

	@Override
	public <T> int pushValue(String dataSourceName, T value){
		IDataSource<T> source;
		try {
			source = getDataSource(dataSourceName, value);
			if (source == null) {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Source::" + dataSourceName + "NotFound;Returning invalid id = -1");
			}
			return source.insert(value);
		} catch (UnmatchingClassException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Returning invalid id = -1");
		}
		return -1;
	}
	@Override
	public <T> int updateValue(String dataSourceName, T value, IQueryOption option, IQueryCriterion<T>... criteria){
		IDataSource<T> source;
		try {
			source = getDataSource(dataSourceName, value);
			return source.update(value, option, criteria);
		} catch (UnmatchingClassException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Returning invalid id = -1");
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getValue(String dataSourceName, IQueryOption option, IQueryCriterion<T>... criteria){
		return ((IDataSource<T>)getDataSource(dataSourceName)).select(option, criteria);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAll(String dataSourceName, T template){
		IDataSource<T> source;
		try {
			source = getDataSource(dataSourceName, template);
			return source.select(QueryOptions.ALL);
		} catch (UnmatchingClassException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Returning empty list");
		}
		return new ArrayList<T>();
	}
	@Override
	public int countElements(String dataSourceName){
		IDataSource<?> datasource = getDataSource(dataSourceName);
		return datasource != null ? datasource.count() : 0;
	}

}
