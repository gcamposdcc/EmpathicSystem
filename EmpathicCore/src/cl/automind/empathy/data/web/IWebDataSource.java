package cl.automind.empathy.data.web;

import gcampos.dev.net.IHttpClient;

import java.util.List;

import cl.automind.empathy.data.IDataSource;

public interface IWebDataSource<T> extends IDataSource<T> {
	void setDefaultUrl(String targetUrl);
	String getDefaultUrl();
	boolean useSameUrl();
	String getInsertUrl();
	String getSelectUrl();
	String getUpdateUrl();
	String getDeleteUrl();
	String toWebString(T value);
	T fromWebString(String value);
	List<T> listFromWebString(String value);
	IHttpClient getHttpClient();
}
