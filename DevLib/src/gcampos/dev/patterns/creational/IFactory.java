package gcampos.dev.patterns.creational;

public interface IFactory<TIn, TOut> {
	<TI extends TIn> TOut createElement(TI param);
}
