package gcampos.dev.patterns.creational;

import java.util.Collection;

import gcampos.dev.interfaces.structural.INamed;

public interface NamedFactory<TOut> extends IFactory<INamed, TOut> {
	boolean isValidName(INamed name);
	boolean isValidName(String name);
	TOut createElement(String name);
	Collection<String> getNames();
}
