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

import cl.automind.empathy.data.DataEntry;
import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;

public class MemoryDataSource<T> implements IDataSource<T> {
	private List<IObserver<IDataSource<T>>> observers;
	private final Map<Integer, DataEntry<T>> data;
	private String name;
	private static int code = 0;
	private int id = 0;
	private final boolean useBackup;
	private final T template;
	public MemoryDataSource(boolean useBackup, T template){
		this.template = template;
		this.data = initiateMap();
		setObservers(new CopyOnWriteArrayList<IObserver<IDataSource<T>>>());
		this.name = "genname" + (code++);
		this.useBackup = useBackup;
	}

	public MemoryDataSource(String name, boolean useBackup, T template){
		this.template = template;
		this.data = initiateMap();
		setObservers(new CopyOnWriteArrayList<IObserver<IDataSource<T>>>());
		this.name = !Strings.isNullOrEmpty(name)? name : "genname" + (code++);
		this.useBackup = useBackup;
	}
	private Map<Integer, DataEntry<T>> initiateMap(){
		return new Hashtable<Integer, DataEntry<T>>();
	}
	public void setObservers(List<IObserver<IDataSource<T>>> observers) {
		this.observers = observers;
	}
	public List<IObserver<IDataSource<T>>> getObservers() {
		return observers;
	}
	protected Map<Integer, DataEntry<T>> getData(){
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
		getData().put(id, new MemoryDataEntry<T>(id, value, useBackup));
		printAll();
		return id;
	}
	@Override
	public List<Integer> insert(Collection<T> value) {
		List<Integer> ids = new ArrayList<Integer>();
		int id;
		for(T val: value){
			id = nextId();
			getData().put(id, new MemoryDataEntry<T>(getData().size(), val, useBackup));
			ids.add(id - 1);
		}
		return ids;
	}
	private List<DataEntry<T>> getEntriesOrderedById(){
		List<DataEntry<T>> list = new ArrayList<DataEntry<T>>(getData().values());
		Collections.sort(list);
		return list;
	}
	@Override
	public void clear() {
		getData().clear();
	}
	@Override
	public boolean validId(int id) {
		return getData().containsKey(id);
	}
	@Override
	public int count() {
		return data.size();
	}

	private void printAll(){
		for(DataEntry<T> t : getEntriesOrderedById()){
			System.out.println("Id::" + t.getId()+ "::Value::" + t.getValue());
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
			for(DataEntry<T> t : getData().values()){
				if (criterion.apply(t.getValue())) result.add(t.getValue());
			}
		}
		return result;
	}

	private List<T> selectById(IQueryOption option) {
		List<T> result = new ArrayList<T>();
		DataEntry<T> item = getData().get(option.getValue());
		if (item != null) result.add(item.getValue());
		return result;
	}
	public T selectById(int id) {
		DataEntry<T> d = getData().get(id);
		return d != null ? d.getValue() : null;
	}
	public List<T> selectAll() {
		List<T> list = new ArrayList<T>();
		for(DataEntry<T> t : getEntriesOrderedById()){
			list.add(t.getValue());
		}
		return Collections.unmodifiableList(list);
	}
	public int update(int id, T value) {
		DataEntry<T> item = getData().get(id);
		return item != null ? item.setValue(value) : -1;
	}
	@Override
	public int update(T value, IQueryOption option, IQueryCriterion<T>... criteria) {
		if (option.getType() == IQueryOption.Type.Id){
			DataEntry<T> item = getData().get(option.getValue());
			if (item != null) {
				item.setValue(value);
				return 1;
			} else {
				return 0;
			}
		} else {
			List<Integer> ids = new ArrayList<Integer>();
			for (IQueryCriterion<T> criterion : criteria){
				for(DataEntry<T> t : getData().values()){
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
			for(DataEntry<T> t : getData().values()){
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
			for(DataEntry<T> t : getData().values()){
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
