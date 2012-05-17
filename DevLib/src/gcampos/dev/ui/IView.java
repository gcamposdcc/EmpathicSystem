package gcampos.dev.ui;

import gcampos.dev.patterns.behavioral.IObserver;

public interface IView<T extends IViewModel> extends IObserver<IViewModel>{
	public T getViewModel();
}
