package cl.automind.empathy.data;

public enum DefaultQueryOptions implements IQueryOption{
	/**
	 * Limits the number of elements
	 */
	Id(1),
	None(1),
	Filter(1),
	All(1);
	private int value;
	private DefaultQueryOptions(int value){
		this.value = Math.max(1, value);
	}
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String getName() {
		return this.toString();
	}

}
