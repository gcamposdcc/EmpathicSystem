package gcampos.dev.patterns.behavioral;

import gcampos.dev.interfaces.behavioral.IDisposable;

public interface IObservable<T> {
	public void update(T value);
	public IDisposable suscribe(IObserver<T> observer);
	public void unsuscribe(IObserver<T> observer);
}
