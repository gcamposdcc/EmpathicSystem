package cl.automind.empathy.fwdroid.data.sql;

import net.IHttpClient;
import cl.automind.android.net.AndroidHttpClient;
import cl.automind.empathy.fw.data.web.AbstractWebDataSource;

public abstract class AbstractDroidWebDataSource<T> extends AbstractWebDataSource<T> {

	public AbstractDroidWebDataSource(T template) {
		super(template);
	}

	@Override
	public IHttpClient getHttpClient() {
		return new AndroidHttpClient();
	}

}
