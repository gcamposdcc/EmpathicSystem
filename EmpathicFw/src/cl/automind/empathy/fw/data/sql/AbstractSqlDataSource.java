package cl.automind.empathy.fw.data.sql;

import gcampos.dev.interfaces.behavioral.IDisposable;
import gcampos.dev.patterns.behavioral.IObserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;
import cl.automind.empathy.data.NamedValuePair;
import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.ISqlDataSource;
import cl.automind.empathy.data.sql.NamedQuery;
import cl.automind.empathy.data.sql.SqlMetadata;
import cl.automind.empathy.data.sql.SqlNamedValuePair;
import cl.automind.empathy.data.sql.SqlPairs;

public abstract class AbstractSqlDataSource<T> implements ISqlDataSource<T> {
	public final static String UPDATE_OLD_FIELD_PREFIX = "#GCUPDOLD";
	private final Map<String, String> queryMap;
	private final Map<String, PreparedStatement> preparedStatementMap;
	private final Collection<IObserver<IDataSource<T>>> observers;
	private final String name;
	private final T template;
	private final ISqlConnector connector;
	private final SqlDescriptor descriptor;
	private final SqlMetadata metadata;

	public AbstractSqlDataSource(T template, ISqlConnector connector){
		// INIT GLOBALS
		this.template = template;
		this.queryMap = new ConcurrentHashMap<String, String>();
		this.observers = new CopyOnWriteArrayList<IObserver<IDataSource<T>>>();
		this.preparedStatementMap = new ConcurrentHashMap<String, PreparedStatement>();
		this.connector = connector;
		this.descriptor = new SqlDescriptor();
		Class<?> templateClass = template.getClass();

		// METADATA
		Class<?> thisClass = getCurrentClass();
		this.metadata = thisClass.getAnnotation(SqlMetadata.class);
		this.name = SqlClassExtractor.extractDataSourceName(metadata, templateClass, thisClass);
		doInitTasks();
	}
	public final void drop(){
		executeNamedUpdate("drop", SqlPairs.EMPTY);
	}
	public final void reset(){
		drop();
		clearAll();
		doInitTasks();
	}
	private void clearAll(){
		getDescriptor().setIdName("id");
		getDescriptor().setUsesId(false);
		getDescriptor().getFieldNames().clear();
		getDescriptor().getColumnDescriptorMap().clear();
		getDescriptor().getQueryDescriptorsMap().clear();
		getPreparedStatementMap().clear();
	}
	private void doInitTasks(){
		SqlClassExtractor<T> extractor = new SqlClassExtractor<T>(this);
		extractor.extractAll();
		if (getMetadata().createOnInit()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("CreationalQuery::" + getQueryMap().get("create"));
			try {
				getConnector().createStatement().execute(getQueryMap().get("create"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (NamedQuery query: metadata.initQueries()){
			try {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("StartupQuery::" + query.query());
				getConnector().createStatement().execute(query.query());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (NamedQuery query: metadata.namedQueries()){
			addPreparedStatement(query.name(), getQueryMap().get(query.name()));
		}
		for (Map.Entry<String, String> entry: getQueryMap().entrySet()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("AddingQuery::" + entry.getKey() + "::" + entry.getValue());
		}
		buildPrepareStatements();
	}
	private void buildPrepareStatements(){
		if (getDescriptor().getUsesId()){
			String[] byId = {"updateById", "selectById", "deleteById"};
			initPreparedStatements(byId);
		}
		String[] queries = {"drop", "update", "select", "delete", "insert", "count", "selectAll"};
		initPreparedStatements(queries);
	}
	private void initPreparedStatements(String[] queryNames){
		for (String queryName: queryNames) {
			addPreparedStatement(queryName, getQueryMap().get(queryName));
		}
	}
	private void addPreparedStatement(String queryName, String query){
		try {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("AddingPreparedStatement::" + queryName +"::" + query);
			getPreparedStatementMap().put(queryName, getConnector().prepareStatement(query));
		} catch (Exception e) { 
			e.printStackTrace();	
		}
	}
	private Class<?> getCurrentClass() {
		Class<?> thisClass = getClass();
		if (thisClass.isAnonymousClass()){
			thisClass = thisClass.getSuperclass();
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("AnonymousClassDetected::Parent::" + thisClass.getSimpleName());
		}
		return thisClass;
	}

	@Override
	public ISqlConnector getConnector() {
		return connector;
	}
	protected void putInPreparedStatement(final PreparedStatement query, String queryName, T value){
		try {
			Collection<SqlNamedValuePair<?>> pairs = toPairs(value);
			for (SqlNamedValuePair<?> pair : pairs){
				for (Integer index : getDescriptor().getParamPosition(queryName, pair.getKey())){
					pair.setIn(query, index);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	protected void putInPreparedStatement(final PreparedStatement query, String queryName, IQueryCriterion<T>... criteria){
		for (IQueryCriterion<T> criterion: criteria){
			for (NamedValuePair<?> pair : criterion.getParams()){
				for (Integer index : getDescriptor().getParamPosition(queryName, pair.getKey())){
					try {
						((SqlNamedValuePair<?>)pair).setIn(query, index);
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	@Override
	public int insert(T value) {
		int id = -1;
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get("insert"));
		ResultSet rs;
		try {
			putInPreparedStatement(query, "insert", value);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(getQueryMap().get("insert"));
			rs = query.executeQuery();
			rs.next();
			id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public List<Integer> insert(Collection<T> values) {
		List<Integer> ids = new ArrayList<Integer>();
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get("insert"));
		ResultSet rs;
		for(T value: values){
			try {
				putInPreparedStatement(query, "insert", value);
				rs = query.executeQuery();
				rs.next();
				ids.add(rs.getInt(1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ids;
	}

	@Override
	public void clear() {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ClearMethodNotImplemented");
	}

	@Override
	public int count() {
		PreparedStatement count = getConnector().prepareStatement(getQueryMap().get("count"));
		ResultSet rs;
		int total = 0;
		try {
			rs = count.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public void update(IDataSource<T> value) {
		for(IObserver<IDataSource<T>> observer : getObservers()){
			observer.onNew(this);
		}
	}

	protected Collection<IObserver<IDataSource<T>>> getObservers() {
		return observers;
	}

	@Override
	public IDisposable suscribe(IObserver<IDataSource<T>> observer) {
		if (getObservers().contains(observer)) return null;
		getObservers().add(observer);
		return null;
	}

	@Override
	public void unsuscribe(IObserver<IDataSource<T>> observer) {
		getObservers().remove(observer);
	}

	@Override
	public String getName() {
		return name;
	}

	protected Map<String, String> getQueryMap() {
		return queryMap;
	}

	@Override
	public List<T> executeNamedQuery(String queryName, SqlNamedValuePair<?>... constrains) {
		ResultSet rs = executeCustomNamedQuery(queryName, constrains);
		return rs!= null? parse(rs) : new ArrayList<T>();
	}
	@Override
	public int executeNamedUpdate(String queryName, SqlNamedValuePair<?>... constrains) {
		verifyConnection();
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get(queryName));
		fillPreparedStatement(query, constrains);
		try {
			return query.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public ResultSet executeCustomNamedQuery(String queryName, SqlNamedValuePair<?>... constrains) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingCustomNamedQuery::" + queryName + "::" + getQueryMap().get(queryName));
		verifyConnection();
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get(queryName));
		fillPreparedStatement(query, constrains);
		ResultSet output = null;
		try {
			output = query.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}
	private PreparedStatement fillPreparedStatement(PreparedStatement query, SqlNamedValuePair<?>... constrains) {
		int index = 1;
		for(SqlNamedValuePair<?> constrain: constrains){
			constrain.setIn(query, index++);
		}
		return query;
	}
	private void verifyConnection() {
		if (!getConnector().isConnected()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("SqlSourceNotConnected::Connecting");
			getConnector().openConnection();
		}
	}
	@Override
	public T getTemplate() {
		return template;
	}

	@Override
	public final cl.automind.empathy.data.IDataSource.Type getType() {
		return Type.Sql;
	}

	@Override
	public List<T> select(IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.All){
			return selectAll();
		} if (option.getType() == IQueryOption.Type.Id){
			List<T> list = new ArrayList<T>();
			T t = null;
			try{
				t = selectById((Integer) criteria[0].getParams()[0].getValue());
			} catch (Exception e){
				e.printStackTrace();
			}
			if (t!=null) list.add(t);
			return list;
		} else if (option.getType() == IQueryOption.Type.Filter){
			try {
				String queryName = "select";
				String queryString = getQueryMap().get(queryName);
				PreparedStatement query = getPreparedStatementMap().get(queryName);
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
				putInPreparedStatement(query, queryName, criteria);
				ResultSet rs = query.executeQuery();
				return parse(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new ArrayList<T>();
		} else {
			String queryName = "selectCustom";
			String queryString = completeQuery(getQueryMap().get(queryName), criteria);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
			ResultSet rs = null;
			try {
				rs = getConnector().prepareStatement(queryString).executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return parse(rs);
		}
	}

	public T selectById(int id) {
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get("selectById"));
		ResultSet rs;
		List<T> result = new ArrayList<T>();
		try {
			query.setInt(0, id);
			rs = query.executeQuery();
			result = parse(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.size() > 0 ? result.get(0) : null;
	}

	public List<T> selectAll() {
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get("selectAll"));
		ResultSet rs;
		List<T> result = new ArrayList<T>();
		try {
			rs = query.executeQuery();
			result = parse(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public int update(T value, IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.Id){
			String queryName = "updateById";
			String queryString = getQueryMap().get(queryName);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
			PreparedStatement query = getConnector().prepareStatement(queryString);
			try {
				SqlNamedValuePair<Integer> idPair = SqlPairs.pair(getDescriptor().getIdName(), option.getValue());
				for (Integer index : getDescriptor().getParamPosition(queryName, UPDATE_OLD_FIELD_PREFIX +idPair.getKey())){
					idPair.setIn(query, index);
				}
				putInPreparedStatement(query, queryName, value);
				query.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 1;
		} else if (option.getType() == IQueryOption.Type.Filter){
			try {
				String queryName = "update";
				String queryString = getQueryMap().get(queryName);
				PreparedStatement query = getPreparedStatementMap().get(queryName);
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
				putInPreparedStatement(query, queryName, criteria);
				query.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 1;
		} else {
			String queryName = "updateCustom";
			String queryString = completeQuery(getQueryMap().get(queryName), criteria);
			PreparedStatement query = getConnector().prepareStatement(queryString);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
			try {
				putInPreparedStatement(query, queryName, value);
				query.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 1;
		}
	}

	@Override
	public boolean delete(IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.Id){
			return deleteById((Integer) criteria[0].getParams()[0].getValue());
		} else if (option.getType() == IQueryOption.Type.Filter){
			try {
				String queryName = "delete";
				String queryString = getQueryMap().get(queryName);
				PreparedStatement query = getPreparedStatementMap().get(queryName);
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
				putInPreparedStatement(query, queryName, criteria);
				query.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String queryName = "deleteCustom";
			String queryString = completeQuery(getQueryMap().get(queryName), criteria);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
			PreparedStatement query = getConnector().prepareStatement(queryString);
			try {
				query.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//FIXME result
		return true;
	}
	private boolean deleteById(int id){
		PreparedStatement query = getConnector().prepareStatement(getQueryMap().get("deleteById"));
		try {
			query.setInt(0, id);
			query.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public SqlDescriptor getDescriptor() {
		return descriptor;
	}
	private String completeQuery(String qs, IQueryCriterion<T>... criteria){
		String queryString = qs;
		if (criteria == null) return queryString + ";";
		if (criteria.length == 0) return queryString + ";";
		queryString += " WHERE ";
		for (IQueryCriterion<T> criterion : criteria){
			NamedValuePair<?>[] pairs = criterion.getParams();
			if (pairs!= null){
				for (int i = 0; i < pairs.length ; i++){
					if (pairs[i] == null) continue;
					if (SqlNamedValuePair.class.isAssignableFrom(pairs[i].getClass())){
						SqlNamedValuePair<?> sqlpair = (SqlNamedValuePair<?>) pairs[i];
						queryString += " " + sqlpair + " ";
					} else {
						queryString += " " + pairs[i].getKey() + " = " + pairs[i].getValue() + " ";
					}
					queryString += (i < pairs.length - 1 ? " AND " : "");
				}
			}
		}
		queryString += ";";
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("QueryBuilt::" + queryString);
		return queryString;
	}

	public Map<String, PreparedStatement> getPreparedStatementMap() {
		return preparedStatementMap;
	}
	public SqlMetadata getMetadata() {
		return metadata;
	}
}
