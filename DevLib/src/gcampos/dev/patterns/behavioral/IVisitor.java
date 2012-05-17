package gcampos.dev.patterns.behavioral;

public interface IVisitor {
	public <T, TVisitable extends IVisitable> T visit(TVisitable t);
}
