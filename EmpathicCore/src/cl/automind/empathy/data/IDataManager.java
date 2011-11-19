package cl.automind.empathy.data;

import java.util.List;

public interface IDataManager {

	abstract public <T> IDataSource<T> getDataSource(String dataSourceName, T template) throws UnmatchingClassException;

	abstract public <T> boolean registerDataSource(String dataSourceName, IDataSource<T> dataSource);

	abstract public <T> IDataSource<T> createDataSource(String dataSourceName, T template);

	abstract public boolean createSpaceIfNotFound();

	abstract public <T> int pushValue(String dataSourceName, T value);

	abstract public <T> int updateValue(String dataSourceName, T value, IQueryOption option, IQueryCriterion<T>... criteria);

	abstract public <T> List<T> getValue(String dataSourceName, IQueryOption option, IQueryCriterion<T>... criteria);

	abstract public <T> List<T> getAll(String dataSourceName, T template);

	abstract public int countElements(String dataSourceName);

	abstract public boolean sourceExists(String dataSourceName);

	abstract public boolean assignableToSpace(Object value, String dataSourceName);

	abstract public boolean assignableToSpace(Class<?> clazz, String dataSourceName);

}