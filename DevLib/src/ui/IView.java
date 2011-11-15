package ui;

import patterns.behavioral.IObserver;

public interface IView<T extends IViewModel> extends IObserver<IViewModel>{
	public T getViewModel();
}
