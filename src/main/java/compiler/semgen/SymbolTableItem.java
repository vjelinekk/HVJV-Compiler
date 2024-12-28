package compiler.semgen;

public class SymbolTableItem {
    private final String id;
    private final int scope;
    private final int address;

    public SymbolTableItem(String id, int scope, int address) {
        this.id = id;
        this.scope = scope;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public int getScope() {
        return scope;
    }

    public int getAddress() {
        return address;
    }
}
