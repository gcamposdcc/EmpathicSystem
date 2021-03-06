package cl.automind.empathy.fw.data.web;

import gcampos.dev.interfaces.behavioral.IDisposable;
import gcampos.dev.net.IHttpClient;
import gcampos.dev.patterns.behavioral.IObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryCriterion;
import cl.automind.empathy.data.IQueryOption;
import cl.automind.empathy.data.NamedValuePair;
import cl.automind.empathy.data.web.IWebDataSource;

public abstract class AbstractWebDataSource<T> implements IWebDataSource<T> {
	private List<IObserver<IDataSource<T>>> observers;
	private final T template;
	private final String name;
	private final boolean useSameUrl;
	private String defaultUrl;
	private String updateUrl;
	private String selectUrl;
	private String deleteUrl;
	private String insertUrl;
	private final ExecutorService executorService;
	public AbstractWebDataSource(T template){
		this.observers = new CopyOnWriteArrayList<IObserver<IDataSource<T>>>();
		this.template = template;
		this.executorService = Executors.newSingleThreadExecutor();
		// METADATA
		Class<?> thisClass = getClass();
		if (thisClass.isAnonymousClass()){
			thisClass = thisClass.getSuperclass();
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("AnonymousClassDetected::Parent::" + thisClass.getSimpleName());
		}
		WebMetadata metadata = thisClass.getAnnotation(WebMetadata.class);
		if (metadata != null){
			this.name = metadata.name().trim().equals("") ?
					thisClass.getSimpleName().replaceAll("DataSource", "")
					: metadata.name().trim();
			this.useSameUrl = metadata.useSameUrl();
			this.defaultUrl = metadata.defaultUrl();
			if (metadata.useSameUrl()){
				this.insertUrl = metadata.defaultUrl();
				this.deleteUrl = metadata.defaultUrl();
				this.selectUrl = metadata.defaultUrl();
				this.updateUrl = metadata.defaultUrl();
			} else {
				this.insertUrl = metadata.insertUrl();
				this.deleteUrl = metadata.deleteUrl();
				this.selectUrl = metadata.selectUrl();
				this.updateUrl = metadata.updateUrl();
			}
		} else {
			this.name = thisClass.getSimpleName().replaceAll("DataSource", "");
			this.useSameUrl = true;
		}
	}
	@Override
	public T getTemplate() {
		return template;
	}

	@Override
	public int insert(T value) {
		String message = toWebString(value);
		String[] params = message.split("&");
		
		final IHttpClient client = getHttpClient();
		System.out.println(client.getClass());
		System.out.println(useSameUrl() + "?" + getDefaultUrl() + ":" + getInsertUrl());
		client.setUrl(useSameUrl() ? getDefaultUrl() : getInsertUrl());
		System.out.println("DataSource:"+getName()+"::Insert:"+(useSameUrl() ? getDefaultUrl() : getInsertUrl()));
//		client.addRequestParameter("new", message);
		String[] paramSplit = null;
		for(String param: params){
			if (param.isEmpty()) continue;
			paramSplit = param.split("=");
			if (paramSplit.length != 2) continue;
			client.addRequestParameter(paramSplit[0], paramSplit[1]);
			System.out.println("Adding param " + paramSplit[0] + "=" + paramSplit[1]);
		}
		String response = sendRequest(client);
		int id = -1;
		try{
			id = Integer.parseInt(response);
		} catch (Exception e){
//			e.printStackTrace();
		}
		return id;
	}

	@Override
	public List<Integer> insert(Collection<T> values) {
		List<Integer> ids = new ArrayList<Integer>();
		int id = -1;
		for (T value : values){
			id = insert(value);
			if (id > 0) ids.add(id);
		}
		return ids;
	}

	@Override
	public List<T> select(IQueryOption option, IQueryCriterion<T>... criteria) {
		IHttpClient client = getHttpClient();
		client.setUrl(useSameUrl() ? getDefaultUrl() : getSelectUrl());
		for (IQueryCriterion<T> criterion : criteria){
			for (NamedValuePair<?> pair: criterion.getParams()){
				client.addRequestParameter(pair.getKey(), ""+pair.getValue());
			}
		}
		String response = sendRequest(client);
		List<T> result = listFromWebString(response);
		return result;
	}

	@Override
	public int update(T value, IQueryOption option, IQueryCriterion<T>... criteria) {
		String message = toWebString(value);
		final IHttpClient client = getHttpClient();
		client.setUrl(useSameUrl() ? getDefaultUrl() : getUpdateUrl());
		client.addRequestParameter("new", message);
		String response = sendRequest(client);
		int id = -1;
		try{
			id = Integer.parseInt(response);
		} catch (Exception e){
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public boolean delete(IQueryOption option, IQueryCriterion<T>... criteria) {
		IHttpClient client = getHttpClient();
		client.setUrl(useSameUrl() ? getDefaultUrl() : getSelectUrl());
		for (IQueryCriterion<T> criterion : criteria){
			for (NamedValuePair<?> pair: criterion.getParams()){
				client.addRequestParameter(pair.getKey(), ""+pair.getValue());
			}
		}
		String response = sendRequest(client);
		response += response;
		// FIXME return value: delete(IQueryOption option, IQueryCriterion<T>... criteria)
		return false;
	}

	@Override
	public void clear() {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ClearMethodNotImplemented");
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public cl.automind.empathy.data.IDataSource.Type getType() {
		return Type.Web;
	}

	@Override
	public void update(IDataSource<T> value) {
		for(IObserver<IDataSource<T>> observer: getObservers()){
			observer.onNew(value);
		}
	}

	@Override
	public IDisposable suscribe(IObserver<IDataSource<T>> observer) {
		if (getObservers().contains(observer)){
			return null;
		} else {
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
		return this.name;
	}

	@Override
	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	@Override
	public String getDefaultUrl() {
		return defaultUrl;
	}

	@Override
	public boolean useSameUrl() {
		return useSameUrl;
	}

	@Override
	public String getInsertUrl() {
		return insertUrl;
	}

	@Override
	public String getSelectUrl() {
		return selectUrl;
	}

	@Override
	public String getUpdateUrl() {
		return updateUrl;
	}

	@Override
	public String getDeleteUrl() {
		return deleteUrl;
	}
	protected void setObservers(List<IObserver<IDataSource<T>>> observers) {
		this.observers = observers;
	}
	protected List<IObserver<IDataSource<T>>> getObservers() {
		return observers;
	}

	protected ExecutorService getExecutorService() {
		return executorService;
	}

	protected String sendRequest(final IHttpClient client){
		Future<String> future = getExecutorService().submit(new Callable<String>() {
			@Override public String call() {
				return client.sendRequest();
			}
		});
		try {
			return future.get(); // use future
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
