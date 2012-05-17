package gcampos.dev.interfaces.structural;

public interface IClassGetter<TargetType, PropertyType> {
	public PropertyType get(TargetType t);
}
