package cl.automind.empathy.data;

import gcampos.dev.interfaces.structural.INamed;

public interface IQueryOption extends INamed{
	public int getValue();
	public void setValue(int value);
}
