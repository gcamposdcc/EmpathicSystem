package gcampos.dev.patterns.behavioral;

public interface IVisitable {
	public <T, TVisitor extends IVisitor> T accept(TVisitor visitor);
}
