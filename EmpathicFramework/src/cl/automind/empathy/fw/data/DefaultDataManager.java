package cl.automind.empathy.fw.data;

import java.util.Hashtable;
import java.util.Map;

import patterns.creational.FlyweightNamedFactory;
import cl.automind.empathy.data.AbstractDataManager;
import cl.automind.empathy.data.IDataSource;

public class DefaultDataManager extends AbstractDataManager{
	
	private final FlyweightNamedFactory<IDataSource<?>> dataSources;
	public DefaultDataManager(){
		dataSources = new FlyweightNamedFactory<IDataSource<?>>() {
			@Override
			public Map<String, IDataSource<?>> initializeMap() {
				return new Hashtable<String, IDataSource<?>>();
			}
		};
	}
	@Override
	public boolean createSpaceIfNotFound() {
		return true;
	}
	@Override
	public IDataSource<?> getDataSource(String dataSourceName) {
		return dataSources.createElement(dataSourceName);
	}
	@Override
	public <T> IDataSource<T> getDataSource(String dataSourceName, T template) {
		IDataSource<T> source = null;
		try{
			source = (IDataSource<T>) dataSources.createElement(dataSourceName);
		} catch (NullPointerException e){
			if (createSpaceIfNotFound()){
				source = createDataSource(dataSourceName, template);
			}
		} catch (Exception e){
			return null;
		}
		if (source == null && createSpaceIfNotFound()){
			source = createDataSource(dataSourceName, template);
		}
		return source;
	}
	@Override
	public <T> boolean registerDataSource(String dataSourceName, IDataSource<T> DataSource) {
		if (!dataSources.elementRegistered(dataSourceName)){
			dataSources.registerElement(dataSourceName, DataSource);
			return true;
		}
		return false;
	}
	@Override
	public <T> IDataSource<T> createDataSource(String dataSourceName, T template) {
		IDataSource<T> source = new InternalDataSource<T>(true);
		dataSources.registerElement(dataSourceName, source);
		return source;
	}

}
