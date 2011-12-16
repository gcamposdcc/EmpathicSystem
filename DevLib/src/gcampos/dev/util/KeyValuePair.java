package gcampos.dev.util;

public class KeyValuePair<K,V> {

	private final K key;
	private V value;

	public KeyValuePair(K key, V value){
		if (key == null) throw new NullPointerException();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public V setValue(V newValue) {
		V temp = this.value;
		this.value = newValue;
		return temp;
	}
}
