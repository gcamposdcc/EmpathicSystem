package cl.automind.empathy.fw.data;

import gcampos.dev.patterns.creational.FlyweightNamedFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.UnmatchingClassException;

public class DefaultDataManager extends AbstractDataManager{

	private final FlyweightNamedFactory<IDataSource<?>> dataSources;
	private final FlyweightNamedFactory<Class<?>> classes;
	private final Set<String> dataSourcesNames;
	public DefaultDataManager(){
		dataSources = new FlyweightNamedFactory<IDataSource<?>>() {
			@Override
			public Map<String, IDataSource<?>> initializeMap() {
				return new ConcurrentHashMap<String, IDataSource<?>>();
			}
		};
		classes = new FlyweightNamedFactory<Class<?>>() {
			@Override
			public Map<String, Class<?>> initializeMap() {
				return new ConcurrentHashMap<String, Class<?>>();
			}
		};
		dataSourcesNames = new CopyOnWriteArraySet<String>();
	}
	@Override
	public boolean createSpaceIfNotFound() {
		return true;
	}
	@Override
	public IDataSource<?> getDataSource(String dataSourceName) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("GettingSource::" + dataSourceName + "::UsingNoTemplate");
		return getDataSources().createElement(dataSourceName);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> IDataSource<T> getDataSource(String dataSourceName, T template) throws UnmatchingClassException{
		IDataSource<T> source = null;
		try{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("GettingSource::" + dataSourceName + "::" +template.getClass());
			IDataSource<?> genericSource = getDataSources().createElement(dataSourceName);
			if (genericSource != null){
				if (!genericSource.getTemplate().getClass().isAssignableFrom(template.getClass())){
					throw new UnmatchingClassException("Cannot handle Objects of " + template.getClass() + "in DataSource of " + genericSource.getTemplate().getClass());
				}
				source = (IDataSource<T>) genericSource;
			}
		} catch (NullPointerException e){
			e.printStackTrace();
			if (createSpaceIfNotFound()){
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("AutoCreatingSource::" + dataSourceName + "::" +template.getClass());
				source = createDataSource(dataSourceName, template);
			}
		} catch (ClassCastException e){
			e.printStackTrace();
			return null;
		}
		if (source == null && createSpaceIfNotFound()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("SourceNotFound::" + dataSourceName);
			source = createDataSource(dataSourceName, template);
		}
		return source;
	}
	@Override
	public <T> boolean registerDataSource(String dataSourceName, IDataSource<T> dataSource) {
		if (!getDataSources().elementRegistered(dataSourceName)){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("RegisteringSource::" + dataSourceName + "::" +dataSource);
			getDataSources().registerElement(dataSourceName, dataSource);
			getDataSourcesNames().add(dataSourceName);
			getDataSourcesClasses().registerElement(dataSourceName, dataSource.getTemplate().getClass());
			return true;
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> IDataSource<T> createDataSource(String dataSourceName, T template) {
		if (sourceExists(dataSourceName)) {
			try{
				return (IDataSource<T>) getDataSources().createElement(dataSourceName);
			} catch (Exception e){
				return null;
			}
		}
		IDataSource<T> source = new MemoryDataSource<T>(template);
		getDataSources().registerElement(dataSourceName, source);
		getDataSourcesNames().add(dataSourceName);
		getDataSourcesClasses().registerElement(dataSourceName, template.getClass());
		return source;
	}
	private FlyweightNamedFactory<IDataSource<?>> getDataSources() {
		return dataSources;
	}
	@Override
	public boolean sourceExists(String dataSourceName) {
		return getDataSourcesNames().contains(dataSourceName);
	}
	public Class<?> classOf(String dataSourceName) {
		return getDataSourcesClasses().createElement(dataSourceName);
	}
	private FlyweightNamedFactory<Class<?>> getDataSourcesClasses() {
		return classes;
	}
	private Set<String> getDataSourcesNames() {
		return dataSourcesNames;
	}
	@Override
	public boolean assignableToSpace(Object value, String dataSourceName){
		return assignableToSpace(value.getClass(), dataSourceName);
	}
	@Override
	public boolean assignableToSpace(Class<?> clazz, String dataSourceName){

		return true;
	}
}
