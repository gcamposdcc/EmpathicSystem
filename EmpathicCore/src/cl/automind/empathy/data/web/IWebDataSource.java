package cl.automind.empathy.data.web;

import java.util.List;

import net.IHttpClient;

import cl.automind.empathy.data.IQueryableDataSource;

public interface IWebDataSource<T> extends IQueryableDataSource<T> {
	public void setDefaultUrl(String targetUrl);
	public String getDefaultUrl();
	public boolean useSameUrl();
	public String getInsertUrl();
	public String getSelectUrl();
	public String getUpdateUrl();
	public String getDeleteUrl();
	public String toWebString(T value);
	public T fromWebString(String value);
	public List<T> listFromWebString(String value);
	public IHttpClient getHttpClient();
}
