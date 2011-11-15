package cl.automind.empathy.data.web;

import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IStringMappeable;

public interface IWebDataSource<T extends IStringMappeable<T>> extends IDataSource<T> {
	public void setTargetUrl(String targetUrl);
	public String getTargetUrl();
}
