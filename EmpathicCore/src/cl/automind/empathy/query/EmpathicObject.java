package cl.automind.empathy.query;

import interfaces.structural.IClassProperty;

public class EmpathicObject implements IData<EmpathicObject>{
	private int id;
	public final static IClassProperty<EmpathicObject, Integer> Id = new IClassProperty<EmpathicObject, Integer>() {
		@Override
		public Integer get(EmpathicObject target) {
			return target.id;
		}
		@Override
		public void set(EmpathicObject target, Integer value) {
			target.id = value;
		}
	};
	public int compareTo(EmpathicObject t){
		return 1;
	}
	@Override
	public <PropertyType> void set(
			IClassProperty<EmpathicObject, PropertyType> property,
			PropertyType value) {
		property.set(this, value);
	}
	@Override
	public <PropertyType> PropertyType get(
			IClassProperty<EmpathicObject, PropertyType> property) {
		return property.get(this);
	}

	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}

}
