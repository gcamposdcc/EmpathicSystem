package cl.automind.empathy.data;

public class QueryOption implements IQueryOption{
	private IQueryOption.Type type;
	private int value;
	private String name;

	public QueryOption(){
		this(Type.None, Type.None.getDefaultValue(), Type.None.toString());
	}
	public QueryOption(Type type){
		this(type, type.getDefaultValue());
	}
	public QueryOption(Type type, int value){
		this(type, value, type.toString());
	}
	public QueryOption(Type type, int value, String name){
		setType(type);
		setValue(value);
		setName(name);
	}

	@Override
	public final void setType(IQueryOption.Type type) {
		this.type = type;
	}
	@Override
	public IQueryOption.Type getType() {
		return type;
	}
	@Override
	public final void setValue(int value) {
		this.value = value;
	}
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
