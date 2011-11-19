package cl.automind.empathy.data;

import interfaces.structural.INamed;

public interface IQueryOption extends INamed{
	public int getValue();
	public void setValue(int value);
}
