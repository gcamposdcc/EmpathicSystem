package cl.automind.empathy.fw.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import patterns.creational.FlyweightNamedFactory;
import cl.automind.empathy.data.AbstractDataManager;
import cl.automind.empathy.data.IDataSource;

public class DefaultDataManager extends AbstractDataManager{

	private final FlyweightNamedFactory<IDataSource<?>> dataSources;
	public DefaultDataManager(){
		dataSources = new FlyweightNamedFactory<IDataSource<?>>() {
			@Override
			public Map<String, IDataSource<?>> initializeMap() {
				return new ConcurrentHashMap<String, IDataSource<?>>();
			}
		};
	}
	@Override
	public boolean createSpaceIfNotFound() {
		return true;
	}
	@Override
	public IDataSource<?> getDataSource(String dataSourceName) {
		return getDataSources().createElement(dataSourceName);
	}
	@Override
	public <T> IDataSource<T> getDataSource(String dataSourceName, T template) {
		IDataSource<T> source = null;
		try{
			source = (IDataSource<T>) getDataSources().createElement(dataSourceName);
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
		if (!getDataSources().elementRegistered(dataSourceName)){
			getDataSources().registerElement(dataSourceName, DataSource);
			return true;
		}
		return false;
	}
	@Override
	public <T> IDataSource<T> createDataSource(String dataSourceName, T template) {
		IDataSource<T> source = new InternalDataSource<T>(true);
		getDataSources().registerElement(dataSourceName, source);
		return source;
	}
	private FlyweightNamedFactory<IDataSource<?>> getDataSources() {
		return dataSources;
	}

}
