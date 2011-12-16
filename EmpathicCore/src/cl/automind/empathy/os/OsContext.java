package cl.automind.empathy.os;

public class OsContext {
	private NetFactory netFactory;

	public void setNetFactory(NetFactory netFactory) {
		this.netFactory = netFactory;
	}

	public NetFactory getNetFactory() {
		return netFactory;
	}
}
