package patterns.creational;

import interfaces.structural.INamed;

public interface NamedFactory<TOut> extends IFactory<INamed, TOut> {
	boolean isValidName(INamed name);
	boolean isValidName(String name);
	TOut createElement(String name);
}
