package cl.automind.empathy.data;


public abstract class DataEntry<T> implements Comparable<DataEntry<T>>{
	private final int id;
	private int version;
	private T value;

	public final static DataEntry<Object> Null = new DataEntry<Object>(null) {
		@Override protected void onSetValue(Object oldValue, Object newValue) { }
	};

	protected abstract void onSetValue(T oldValue, T newValue);

	public DataEntry(T value){
		id = 0;
		setVersion(0);
		setValue(value);
	}
	public DataEntry(int id, T value){
		this.id = id;
		setVersion(0);
		setValue(value);
	}

	public int setValue(T value) {
		onSetValue(this.value, value);
		this.value = value;
		return getVersion();
	}
	public T getValue() {
		return value;
	}
	protected void setVersion(int version) {
		this.version = version;
	}
	public int getVersion() {
		return version;
	}
	public int getId() {
		return id;
	}
	@Override
	public int compareTo(DataEntry<T> arg0) {
		return (new Integer(getId())).compareTo(arg0.getId());
	}
}
