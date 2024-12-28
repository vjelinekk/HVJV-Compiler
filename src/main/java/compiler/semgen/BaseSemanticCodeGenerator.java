package compiler.semgen;

abstract public class BaseSemanticCodeGenerator<T> {
    private final SymbolTable symbolTable;
    private final T node;

    public BaseSemanticCodeGenerator(T node, SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.node = node;
    }

    public abstract void run();

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public T getNode() {
        return node;
    }
}
