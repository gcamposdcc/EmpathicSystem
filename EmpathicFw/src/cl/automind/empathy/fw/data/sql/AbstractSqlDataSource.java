package cl.automind.empathy.fw.data.sql;

import gcampos.dev.interfaces.behavioral.IDisposable;
import gcampos.dev.patterns.behavioral.IObserver;
import gcampos.dev.util.Strings;

import java.lang.reflect.Field;
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
import cl.automind.empathy.data.sql.Column;
import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.ISqlDataSource;
import cl.automind.empathy.data.sql.Id;
import cl.automind.empathy.data.sql.NamedQuery;
import cl.automind.empathy.data.sql.SqlMetadata;
import cl.automind.empathy.data.sql.SqlNamedValuePair;
import cl.automind.empathy.data.sql.SqlPairs;

public abstract class AbstractSqlDataSource<T> implements ISqlDataSource<T> {
	private final Map<String, String> queryMap;
	private final Collection<IObserver<IDataSource<T>>> observers;
	private final String name;
	private final T template;
	private final ISqlConnector connector;
	private final SqlDescriptor descriptor;

	public AbstractSqlDataSource(T template, ISqlConnector connector){
		// INIT GLOBALS
		this.template = template;
		this.queryMap = new ConcurrentHashMap<String, String>();
		this.observers = new CopyOnWriteArrayList<IObserver<IDataSource<T>>>();
		this.connector = connector;
		this.descriptor = new SqlDescriptor();
		Class<?> templateClass = template.getClass();

		// METADATA
		Class<?> thisClass = getCurrentClass();
		SqlMetadata metadata = thisClass.getAnnotation(SqlMetadata.class);
		this.name = extractDataSourceName(metadata, templateClass, thisClass);
		extractClassMetadata(metadata);
		extractTemplateData(template, templateClass);
		for (Map.Entry<String, String> entry: getQueryMap().entrySet()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Query::" + entry.getKey() + "::" + entry.getValue());
		}
	}

	private void extractTemplateData(T template, Class<?> templateClass) {
		String idName = "id";
		String tempFieldName = "";
		List<String> fieldNames = new ArrayList<String>();
		boolean hasId = false;
		if (template != null){
			Column columnMetadata = null;
			Id idMetadata = null;
			for(Field field: templateClass.getDeclaredFields()){
				columnMetadata = field.getAnnotation(Column.class);
				if (columnMetadata != null){
					tempFieldName = columnMetadata.name().trim().equals("") ? field.getName() : columnMetadata.name().trim();
					if (fieldNames.contains(tempFieldName)) continue;
					fieldNames.add(tempFieldName);
				} else {
					idMetadata = field.getAnnotation(Id.class);
					if (idMetadata != null){
						idName = idMetadata.name().trim().equals("") ? field.getName() : idMetadata.name().trim();
						hasId = true;
					}
				}
			}
			getDescriptor().setIdName(idName);
			getDescriptor().getFieldNames().addAll(fieldNames);
			// BUILD QUERIES

			int fieldCount = fieldNames.size();
			// BY ID
			String updateById = buildUpdateByIdQuery(idName, fieldNames,
					fieldCount);
			getQueryMap().put("selectById", "SELECT * FROM " + getName() + " WHERE "+ idName + " = ?;");
			getDescriptor().addParamToQuery("selectById", idName, 1);
			getQueryMap().put("deleteById", "DELETE FROM " + getName() + " WHERE "+ idName + " = ?;");
			getDescriptor().addParamToQuery("deleteById", idName, 1);
			getQueryMap().put("updataById", updateById);
			// NORMAL
			// INSERT
			String insert = buildInsertQuery(idName, hasId, fieldNames, fieldCount);
			// UPDATE
			String update = buildUpdateQuery(fieldNames, fieldCount);
			// DELETE
			String delete = "DELETE FROM " + getName();
			// SELECT
			String select = "SELECT * FROM " + getName();
			select = buildSelectQuery(fieldNames, fieldCount, select);
			getQueryMap().put("insert", insert);
			getQueryMap().put("update", update);
			getQueryMap().put("delete", delete);
			getQueryMap().put("select", select);
			getQueryMap().put("count", "SELECT count(*) FROM " + getName() + ";");
			getQueryMap().put("selectAll", "SELECT * FROM " + getName() + ";");
		}
	}

	private String extractDataSourceName(SqlMetadata metadata, Class<?> templateClass, Class<?> thisClass) {
		String name = "";
		if (metadata != null){
			name = metadata.name().trim().equals("") ? Strings.englishPlural(templateClass.getSimpleName()) : metadata.name().trim();
		} else {
			name = Strings.englishPlural(templateClass.getSimpleName()).toLowerCase();
		}
		return name;
	}
	private void extractClassMetadata(SqlMetadata metadata){
		if (metadata != null) {
			if(metadata.queries() != null){
				for (NamedQuery query: metadata.queries().queries()){
					getQueryMap().put(query.name(), query.query());
				}
			}
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

	private String buildUpdateByIdQuery(String idName, List<String> fieldNames,
			int fieldCount) {
		String updateById = "UPDATE " + getName() + " SET ";
		if (fieldCount > 0) {
			for (int i = 0; i < fieldCount; i++){
				updateById += fieldNames.get(i) + ((i == fieldCount - 1) ? " ? " : " ?, ");
				getDescriptor().addParamToQuery("updateById", fieldNames.get(i), i + 1);
			}
			updateById += " WHERE " + idName + " = ?;";
			getDescriptor().addParamToQuery("updateById", idName, fieldCount + 1);
		}
		return updateById;
	}

	private String buildSelectQuery(List<String> fieldNames, int fieldCount, String preselect) {
		String select = preselect;
		if (fieldCount > 0) {
			for (int i = 0; i < fieldCount; i++){
				select += fieldNames.get(i) + ((i == fieldCount - 1) ? " ? " : " ?, ");
				getDescriptor().addParamToQuery("update", fieldNames.get(i), i + 1);
			}
		}
		return select;
	}

	private String buildUpdateQuery(List<String> fieldNames, int fieldCount) {
		String update = "UPDATE " + getName() + " SET ";
		if (fieldCount > 0) {
			for (int i = 0; i < fieldCount; i++){
				update += fieldNames.get(i) + " = " + ((i == fieldCount - 1) ? " ? " : " ?, ");
				getDescriptor().addParamToQuery("update", fieldNames.get(i), i + 1);
			}
//				update += " WHERE ";
		}
		return update;
	}

	private String buildInsertQuery(String idName, boolean hasId,
			List<String> fieldNames, int fieldCount) {
		String insert = "INSERT INTO "+ getName() + " ";
		if (fieldCount > 0 ) {
			for (int i = 0; i < fieldCount; i++){
				insert += (i == 0 ? "(" : ", ") + fieldNames.get(i);
				getDescriptor().addParamToQuery("insert", fieldNames.get(i), i + 1);
			}
			insert += ") VALUES (";
			for (int i = 0; i < fieldCount; i++){
				insert += (i == fieldCount - 1) ? "?" : "?, ";
			}
			insert += ")";
			if (hasId) insert += " RETURNING " + idName;
			insert += ";";
		}
		return insert;
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
					pair.set(query, index);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public int insert(T value) {
		int id = -1;
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("insert"));
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
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("insert"));
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
		PreparedStatement count = getConnector().preparedStatement(getQueryMap().get("count"));
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

	private Map<String, String> getQueryMap() {
		return queryMap;
	}

	@Override
	public List<T> executeNamedQuery(String queryName, SqlNamedValuePair<?>... constrains) {
		if (!getConnector().isConnected()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("SqlSourceNotConnected::Connecting");
			getConnector().openConnection();
		}
		List<T> output = new ArrayList<T>();
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get(queryName));
		int index = 1;
		for(SqlNamedValuePair<?> constrain: constrains){
			constrain.set(query, index++);
		}
		ResultSet rs;
		try {
			rs = query.executeQuery();
			output.addAll(parse(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return output;
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
		} else {
			String queryName = "insert";
			String queryString = completeQuery(getQueryMap().get(queryName), criteria);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
			ResultSet rs = null;
			try {
				rs = getConnector().preparedStatement(queryString).executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return parse(rs);
		}
	}

	public T selectById(int id) {
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("selectById"));
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
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("selectAll"));
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
			PreparedStatement query = getConnector().preparedStatement(queryString);
			try {
				SqlNamedValuePair<Integer> idPair = SqlPairs.pair(getDescriptor().getIdName(), option.getValue());
				for (Integer index : getDescriptor().getParamPosition(queryName, idPair.getKey())){
					idPair.set(query, index);
				}
				putInPreparedStatement(query, queryName, value);
				query.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 1;
		} else {
			String queryName = "update";
			String queryString = completeQuery(getQueryMap().get(queryName), criteria);
			PreparedStatement query = getConnector().preparedStatement(queryString);
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
		} else {
			String queryName = "delete";
			String queryString = completeQuery(getQueryMap().get(queryName), criteria);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ExecutingQuery::" + queryName + "::" + queryString);
			PreparedStatement query = getConnector().preparedStatement(queryString);
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
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("deleteById"));
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
}
