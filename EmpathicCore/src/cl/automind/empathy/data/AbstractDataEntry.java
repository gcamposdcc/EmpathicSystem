package cl.automind.empathy.data;

public abstract class AbstractDataEntry<T> implements Comparable<AbstractDataEntry<T>>{
	private final int id;
	private int version;
	private T value;

	static public final AbstractDataEntry<Object> NULL = new AbstractDataEntry<Object>(null) {
		@Override protected void onSetValue(Object oldValue, Object newValue) { }
	};

	protected abstract void onSetValue(T oldValue, T newValue);

	public AbstractDataEntry(T value){
		this.id = 0;
		this.version = 0;
		this.value = value;
	}
	public AbstractDataEntry(int id, T value){
		this.id = id;
		this.version = 0;
		this.value = value;
	}

	public final int setValue(T value) {
		onSetValue(this.value, value);
		this.value = value;
		return getVersion();
	}
	public T getValue() {
		return value;
	}
	protected final void setVersion(int version) {
		this.version = version;
	}
	public int getVersion() {
		return version;
	}
	public int getId() {
		return id;
	}
	@Override
	public int compareTo(AbstractDataEntry<T> arg0) {
		return (Integer.valueOf(getId())).compareTo(arg0.getId());
	}
}
