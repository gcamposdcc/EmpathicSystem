package cl.automind.empathy.fw.data;


import gcampos.dev.interfaces.behavioral.IDisposable;
import gcampos.dev.patterns.behavioral.IObserver;
import gcampos.dev.util.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import cl.automind.empathy.data.AbstractDataEntry;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;

public class MemoryDataSource<T> implements IDataSource<T> {
	private List<IObserver<IDataSource<T>>> observers;
	private final Map<Integer, AbstractDataEntry<T>> data;
	private String name;
	private static int code = 0;
	private int id = 0;
	private final T template;
	public MemoryDataSource(T template){
		this.template = template;
		this.data = initiateMap();
		this.observers = new CopyOnWriteArrayList<IObserver<IDataSource<T>>>();
		this.name = "genname" + (code++);
	}

	public MemoryDataSource(String name, T template){
		this.template = template;
		this.data = initiateMap();
		this.observers = new CopyOnWriteArrayList<IObserver<IDataSource<T>>>();
		this.name = !Strings.isNullOrEmpty(name)? name : "genname" + (code++);
	}
	private Map<Integer, AbstractDataEntry<T>> initiateMap(){
		return new Hashtable<Integer, AbstractDataEntry<T>>();
	}
	public void setObservers(List<IObserver<IDataSource<T>>> observers) {
		this.observers = observers;
	}
	public List<IObserver<IDataSource<T>>> getObservers() {
		return observers;
	}
	protected Map<Integer, AbstractDataEntry<T>> getData(){
		return data;
	}
	private synchronized int nextId() {
		this.id++;
		return id;
	}
	@Override
	public void update(IDataSource<T> value) {
		for(IObserver<IDataSource<T>> observer: getObservers()){
			observer.onNew(value);
		}
	}
	@Override
	public IDisposable suscribe(IObserver<IDataSource<T>> observer) {
		if (getObservers().contains(observer))return null;
		else {
			getObservers().add(observer);
			//TODO implement IDisposable
			return null;
		}
	}
	@Override
	public void unsuscribe(IObserver<IDataSource<T>> observer) {
		getObservers().remove(observer);
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public synchronized int insert(T value) {
		int id = nextId();
		getData().put(id, new MemoryDataEntry<T>(id, value));
		printAll();
		return id;
	}
	@Override
	public List<Integer> insert(Collection<T> value) {
		List<Integer> ids = new ArrayList<Integer>();
		int id;
		for(T val: value){
			id = nextId();
			getData().put(id, new MemoryDataEntry<T>(getData().size(), val));
			ids.add(id - 1);
		}
		return ids;
	}
	private List<AbstractDataEntry<T>> getEntriesOrderedById(){
		List<AbstractDataEntry<T>> list = new ArrayList<AbstractDataEntry<T>>(getData().values());
		Collections.sort(list);
		return list;
	}
	@Override
	public void clear() {
		getData().clear();
	}
	@Override
	public int count() {
		return data.size();
	}

	private void printAll(){
		for(AbstractDataEntry<T> t : getEntriesOrderedById()){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Id::" + t.getId()+ "::Value::" + t.getValue());
		}
	}
	@Override
	public T getTemplate() {
		return template;
	}
	@Override
	public final cl.automind.empathy.data.IDataSource.Type getType() {
		return Type.Memory;
	}
	@Override
	public List<T> select(IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.Id){
			return selectById(option);
		} else if (option.getType() == IQueryOption.Type.All){
			return selectAll();
		} else if (option.getType() == IQueryOption.Type.Filter){
			return selectFiltering(criteria);
		} else {
			return selectFiltering(criteria);
		}
	}

	private List<T> selectFiltering(IQueryCriterion<T>... criteria) {
		List<T> result = new ArrayList<T>();
		for (IQueryCriterion<T> criterion : criteria){
			for(AbstractDataEntry<T> t : getData().values()){
				if (criterion.apply(t.getValue())) result.add(t.getValue());
			}
		}
		return result;
	}

	private List<T> selectById(IQueryOption option) {
		List<T> result = new ArrayList<T>();
		AbstractDataEntry<T> item = getData().get(option.getValue());
		if (item != null) result.add(item.getValue());
		return result;
	}
	public T selectById(int id) {
		AbstractDataEntry<T> d = getData().get(id);
		return d != null ? d.getValue() : null;
	}
	public List<T> selectAll() {
		List<T> list = new ArrayList<T>();
		for(AbstractDataEntry<T> t : getEntriesOrderedById()){
			list.add(t.getValue());
		}
		return Collections.unmodifiableList(list);
	}
	public int update(int id, T value) {
		AbstractDataEntry<T> item = getData().get(id);
		return item != null ? item.setValue(value) : -1;
	}
	@Override
	public int update(T value, IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.Id){
			return updateById(option.getValue(), value);
		} else {
			return defaultUpdate(value, option, criteria);
		}
	}

	private int defaultUpdate(T value, IQueryOption option, IQueryCriterion<T>... criteria) {
		List<Integer> ids = new ArrayList<Integer>();
		for (IQueryCriterion<T> criterion : criteria){
			for(AbstractDataEntry<T> t : getData().values()){
				if (criterion.apply(t.getValue())) {
					t.setValue(value);
					ids.add(t.getId());
					if (option.getValue() > 0 && ids.size() == option.getValue()) {
						//FIXME return type
						return ids.size();
					}
				}
			}
		}
		//FIXME return type
		return ids.size();
	}
	private int updateById(int id, T value){
		AbstractDataEntry<T> item = getData().get(id);
		if (item != null) {
			item.setValue(value);
			return 1;
		} else {
			return 0;
		}
	}
	@Override
	public boolean delete(IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.Id){
			return deleteById(option);
		} else if (option.getType() == IQueryOption.Type.All){
			return deleteAll();
		} else if (option.getType() == IQueryOption.Type.Filter){
			return deleteFiltering(option, criteria);
		} else {
			return defaultDelete(criteria);
		}

	}

	private boolean defaultDelete(IQueryCriterion<T>... criteria) {
		Set<Integer> ids = new HashSet<Integer>();
		for (IQueryCriterion<T> criterion : criteria){
			for(AbstractDataEntry<T> t : getData().values()){
				if (criterion.apply(t.getValue())) {
					ids.add(t.getId());
				}
			}
		}
		for (int id : ids){
			getData().remove(id);
		}
		return ids.size() > 0;
	}

	private boolean deleteFiltering(IQueryOption option,IQueryCriterion<T>... criteria) {
		Set<Integer> ids = new HashSet<Integer>();
		for (IQueryCriterion<T> criterion : criteria){
			for(AbstractDataEntry<T> t : getData().values()){
				if (criterion.apply(t.getValue())) {
					ids.add(t.getId());
				}
				if (option.getValue() > 0 && ids.size() == option.getValue()) break;
			}
			if (option.getValue() > 0 && ids.size() == option.getValue()) break;
		}
		for (int id : ids){
			getData().remove(id);
		}
		return ids.size() > 0;
	}

	private boolean deleteAll() {
		boolean nonEmpty = getData().size() > 0;
		clear();
		return nonEmpty;
	}

	private boolean deleteById(IQueryOption option) {
		return getData().remove(option.getValue()) == null;
	}
}
