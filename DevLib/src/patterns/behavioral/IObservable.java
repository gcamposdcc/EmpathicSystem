package patterns.behavioral;

import interfaces.behavioral.IDisposable;

public interface IObservable<T> {
	public void update(T value);
	public IDisposable suscribe(IObserver<T> observer);
	public void unsuscribe(IObserver<T> observer);
}
