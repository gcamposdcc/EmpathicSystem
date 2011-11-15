package interfaces.structural;

public interface IClassSetter<TargetType, PropertyType> {
	public void set(TargetType target, PropertyType value);
}
