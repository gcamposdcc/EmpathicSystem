package cl.automind.empathy.data;

import gcampos.dev.interfaces.structural.INamed;

public interface IQueryOption extends INamed{
	int getValue();
	void setValue(int value);
	Type getType();
	void setType(Type type);
	public enum Type{
		/**
		 * Limits the number of elements
		 */
		Id {@Override public int getDefaultValue() {return 1;} },
		None {@Override public int getDefaultValue() {return 0;} },
		Filter {@Override public int getDefaultValue() {return 10;} },
		CustomFilter {@Override public int getDefaultValue() {return 10;} },
		All {@Override public int getDefaultValue() {return 0;} };
		public int getDefaultValue(){
			return 0;
		}
	}
}
