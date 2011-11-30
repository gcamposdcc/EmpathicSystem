package cl.automind.empathy.fw.data;

import java.util.ArrayList;
import java.util.List;

import cl.automind.empathy.data.DataEntry;

public class MemoryDataEntry<T> extends DataEntry<T>{

	private final List<T> backup;
	
	public MemoryDataEntry(T value){
		this(value, false);
	}
	public MemoryDataEntry(int id, T value){
		this(id, value, false);
	}
	public MemoryDataEntry(T value, boolean useBackup){
		super(value);
		backup = useBackup ? initializeBackup() : null;
	}
	public MemoryDataEntry(int id, T value, boolean useBackup){
		super(id,value);
		backup = useBackup ? initializeBackup() : null;
	}
	private List<T> initializeBackup() {
		return new ArrayList<T>();
	}
	@Override
	protected void onSetValue(T oldValue, T newValue) {
		if(backup != null) backup.add(newValue);
		setVersion(getVersion() + 1);
	}
	public T getValue(int version){
		if (backup == null) return getValue();
		if (version < 0) return null;
		if (version > backup.size() - 1) return null;
		return backup.get(version);
	}
}