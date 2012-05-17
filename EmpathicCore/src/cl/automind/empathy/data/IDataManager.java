package cl.automind.empathy.data;

import java.util.List;

public interface IDataManager {

	<T> IDataSource<T> getDataSource(String dataSourceName, T template) throws UnmatchingClassException;

	<T> boolean registerDataSource(String dataSourceName, IDataSource<T> dataSource);

	<T> IDataSource<T> createDataSource(String dataSourceName, T template);

	boolean createSpaceIfNotFound();

	<T> int pushValue(String dataSourceName, T value);

	<T> int updateValue(String dataSourceName, T value, IQueryOption option, IQueryCriterion<T>... criteria);

	<T> List<T> getValue(String dataSourceName, IQueryOption option, IQueryCriterion<T>... criteria);

	<T> List<T> getAll(String dataSourceName, T template);

	int countElements(String dataSourceName);

	boolean sourceExists(String dataSourceName);

	boolean assignableToSpace(Object value, String dataSourceName);

	boolean assignableToSpace(Class<?> clazz, String dataSourceName);

}