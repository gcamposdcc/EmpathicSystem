package cl.automind.empathy.data;

public class QueryOption implements IQueryOption{
	private IQueryOption.Type type;
	private int value;

	public QueryOption(){
		setType(Type.None);
		setValue(0);
	}
	public QueryOption(Type type){
		setType(type);
		setValue(type.getDefaultValue());
	}
	public QueryOption(Type type, int value){
		setType(type);
		setValue(value);
	}

	@Override
	public void setType(IQueryOption.Type type) {
		this.type = type;
	}
	@Override
	public IQueryOption.Type getType() {
		return type;
	}
	@Override
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public String getName() {
		return getType().toString();
	}
}
