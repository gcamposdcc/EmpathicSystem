package cl.automind.empathy.query;

import interfaces.structural.IClassProperty;

public interface IData<TargetType> {
	public <PropertyType> void set(IClassProperty<TargetType, PropertyType> property, PropertyType value);
	public <PropertyType> PropertyType get(IClassProperty<TargetType, PropertyType> property);
}
