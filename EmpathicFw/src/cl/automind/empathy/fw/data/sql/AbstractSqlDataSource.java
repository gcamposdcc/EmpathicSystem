package cl.automind.empathy.fw.data.sql;

import interfaces.behavioral.IDisposable;

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

import patterns.behavioral.IObserver;
import util.NamedValuePair;
import util.Strings;
import cl.automind.empathy.data.DefaultQueryOptions;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;
import cl.automind.empathy.data.sql.Column;
import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.ISqlDataSource;
import cl.automind.empathy.data.sql.Id;
import cl.automind.empathy.data.sql.NamedQuery;
import cl.automind.empathy.data.sql.SqlMetadata;
import cl.automind.empathy.data.sql.SqlNamedValuePair;

public abstract class AbstractSqlDataSource<T> implements ISqlDataSource<T> {
	private final Map<String, String> queryMap;
	private final Collection<IObserver<IDataSource<T>>> observers;
	private final String name;
	private final T template;
	private final ISqlConnector connector;
	public AbstractSqlDataSource(T template, ISqlConnector connector){
		// INIT GLOBALS
		this.template = template;
		this.queryMap = new ConcurrentHashMap<String, String>();
		this.observers = new CopyOnWriteArrayList<IObserver<IDataSource<T>>>();
		this.connector = connector;
		Class<?> templateClass = template.getClass();
		// METADATA
		SqlMetadata metadata = getClass().getAnnotation(SqlMetadata.class);
		if (metadata != null){
			this.name = metadata.name().trim().equals("") ? Strings.englishPlural(templateClass.getSimpleName()) : metadata.name().trim();
			if(metadata.queries() != null){
				for (NamedQuery query: metadata.queries().queries()){
					getQueryMap().put(query.name(), query.query());
				}
			}
		} else {
			this.name = Strings.englishPlural(templateClass.getSimpleName()).toLowerCase();
		}
		String insert = "INSERT INTO "+ getName() + " ";
		String field_name = "";
		int field_count = 0;
		if (template != null){
			Column columnMetadata = null;
			Id idMetadata = null;
			for(Field field: templateClass.getDeclaredFields()){
				columnMetadata = field.getAnnotation(Column.class);
				if (columnMetadata != null){
					field_name = columnMetadata.name().trim().equals("") ? field.getName() : columnMetadata.name().trim();
					insert += (field_count == 0 ? "(" : ", ") + field_name;
					field_count++;
				} else {
					idMetadata = field.getAnnotation(Id.class);
					if (idMetadata != null){
						field_name = idMetadata.name().trim().equals("") ? field.getName() : idMetadata.name().trim();
						getQueryMap().put("selectById", "SELECT * FROM " + getName() + " WHERE "+ field_name +" = ?;");
						getQueryMap().put("deleteById", "DELETE FROM " + getName() + " WHERE "+ field_name +" = ?;");
					}
				}
			}
			if (field_count > 0) {
				insert += ") VALUES (";
				for (int i = 0; i < field_count; i++){
					insert += i == field_count - 1 ? "?" : "?, ";
				}
				insert += ");";
			}
		}
		getQueryMap().put("count", "SELECT count(*) FROM " + getName() + ";");
		getQueryMap().put("selectAll", "SELECT * FROM " + getName() + ";");
		getQueryMap().put("insert", insert);
		for (Map.Entry<String, String> entry: getQueryMap().entrySet()){
			System.out.println("Query::" + entry.getKey() + "::" + entry.getValue());
		}
	}

	@Override
	public ISqlConnector getConnector() {
		return connector;
	}

	@Override
	public int insert(T value) {
		int id = -1;
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("insert"));
		ResultSet rs;
		try {
			rs = query.executeQuery();
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
				rs = query.executeQuery();
				ids.add(rs.getInt(1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ids;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validId(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int count() {
		PreparedStatement count = getConnector().preparedStatement(getQueryMap().get("count"));
		ResultSet rs;
		int total = 0;
		try {
			rs = count.executeQuery();
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
			System.out.println("SqlSourceNotConnected::Connecting");
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
		if (option.getName().equals(DefaultQueryOptions.All.getName())){
			return selectAll();
		} else {
			//TODO build query
			String query = "SELECT * FROM " + getName() + " WHERE ";
			for (IQueryCriterion<T> criterion : criteria){
				for (NamedValuePair<?> pair: criterion.getParams()){
					query += pair;
				}
			}
			query += "LIMIT " + option.getValue() +";";
			ResultSet rs = null;
			try {
				rs = getConnector().preparedStatement(query).executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return parse(rs);
		}
	}

	public T select(int id) {
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("selectById"));
		ResultSet rs;
		List<T> result = new ArrayList<T>();
		try {
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(IQueryOption option, IQueryCriterion<T>... criteria) {
		// TODO Auto-generated method stub
		return false;
	}


	public int update(int id, T value) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean delete(int id) {
		PreparedStatement query = getConnector().preparedStatement(getQueryMap().get("deleteById"));
		try {
			query.setInt(0, id);
			query.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//FIXME result
		return true;
	}
}
