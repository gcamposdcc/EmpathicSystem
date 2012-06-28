package gcampos.dev.patterns.creational;

public interface IFactory<TIn, TOut> {
	TOut createElement(TIn param);
}
