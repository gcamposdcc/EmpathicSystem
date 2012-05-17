package cl.automind.empathy.fw.data;

import cl.automind.empathy.data.AbstractDataEntry;

public class MemoryDataEntry<T> extends AbstractDataEntry<T>{

	public MemoryDataEntry(T value){
		super(value);
	}
	public MemoryDataEntry(int id, T value){
		super(id, value);
	}
	@Override
	protected void onSetValue(T oldValue, T newValue) {
		setVersion(getVersion() + 1);
	}
}
