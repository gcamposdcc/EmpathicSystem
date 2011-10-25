package cl.automind.empathy.fw.data;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import interfaces.behavioral.IDisposable;
import patterns.behavioral.IObserver;
import cl.automind.empathy.data.DataEntry;
import cl.automind.empathy.data.IDataSource;

public class InternalDataSource<T> implements IDataSource<T> {
	private List<IObserver<IDataSource<T>>> observers;
	private final Map<Integer, DataEntry<T>> data;
	private String name;
	private static int code = 0;
	private int id = 0;
	private final boolean useBackup;
	public InternalDataSource(boolean useBackup){
		this.data = initiateMap();
		setObservers(new Vector<IObserver<IDataSource<T>>>());
		this.name = "" + (code++);
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
		getData().put(id, new InternalDataEntry<T>(id, value, useBackup));
		return id;
	}
	@Override
	public List<Integer> insert(Collection<T> value) {
		List<Integer> ids = new ArrayList<Integer>();
		int id;
		for(T val: value){
			id = nextId();
			getData().put(id, new InternalDataEntry<T>(getData().size(), val, useBackup));
			ids.add(id - 1);
		}
		return ids;
	}
	@Override
	public T selectById(int id) {
		DataEntry<T> d = getData().get(id);
		return d != null ? d.getValue() : null;
	}
//	@Override
//	public Collection<T> selectLike(T template, String... fields) {
//		Collection<T> likes = new ArrayList<T>();
//		for(DataEntry<T> t : getData().values()){
//			// FIXME
//			if(t.getValue().like(template, fields)){
//				likes.add(t.getValue());
//			}
//		}
//		return likes;
//	}
	@Override
	public List<T> selectAll() {
		List<T> list = new ArrayList<T>();
		for(DataEntry<T> t : getEntriesOrderedById()){
			list.add(t.getValue());
		}
		return list;
	}
	private List<DataEntry<T>> getEntriesOrderedById(){
		List<DataEntry<T>> list = new ArrayList<DataEntry<T>>(getData().values());
		Collections.sort(list);
		return list;
	}
	@Override
	public int updateById(int id, T value) {
		DataEntry<T> item = getData().get(id);
		return item != null ? item.setValue(value) : -1;
	}
	@Override
	public boolean deleteById(int id) {
		if (!validId(id)) return false;
		getData().remove(id);
		return true;
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
}