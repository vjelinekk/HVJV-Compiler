package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EReturnType;
import compiler.semgen.enums.ESymbolTableType;

public class SymbolTableItem {
    private final String id;
    private int level;
    private int address;
    private final ESymbolTableType type;
    private EReturnType returnType;

    public SymbolTableItem(String id, int level, int address, ESymbolTableType type) {
        this.id = id;
        this.level = level;
        this.address = address;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getAddress() {
        return address;
    }

    public ESymbolTableType getType() {
        return type;
    }

    public EReturnType getReturnType() {
        if (type != ESymbolTableType.FUNCTION) {
            throw new RuntimeException("Return type can only be retrieved for functions");
        }

        return returnType;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setReturnType(EReturnType returnType) {
        if (type != ESymbolTableType.FUNCTION) {
            throw new RuntimeException("Return type can only be set for functions");
        }

        this.returnType = returnType;
    }
}
