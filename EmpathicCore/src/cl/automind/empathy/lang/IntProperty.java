package cl.automind.empathy.lang;

public class IntProperty implements IProperty<Integer>{
	private int value;
	@Override
	public Integer get() {
		return value;
	}
	@Override
	public void set(Integer value) {
		this.value = value;
	}

}
