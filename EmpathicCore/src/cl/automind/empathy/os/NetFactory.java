package cl.automind.empathy.os;

import gcampos.dev.net.IHttpClient;
import gcampos.dev.net.SimpleHttpClient;

public class NetFactory {
	public IHttpClient getClient(){
		return new SimpleHttpClient();
	}
}
