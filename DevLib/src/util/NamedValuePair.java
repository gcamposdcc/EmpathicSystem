package util;

import interfaces.structural.INamed;

public class NamedValuePair<V> extends KeyValuePair<String, V> implements INamed {

	public NamedValuePair(String key, V value){
		super(key, value);
	}

	@Override
	public String getName() {
		return getKey();
	}

}
