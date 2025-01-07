package compiler.semgen.symboltable;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EReturnType;
import compiler.semgen.enums.ESymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableItem {
    private final String id;
    private int address;
    private int lastAddress;
    private boolean used;
    private final ESymbolTableType type;
    private EReturnType returnType;
    private List<EDataType> parametersTypes;

    public SymbolTableItem(String id, int address, ESymbolTableType type) {
        this.id = id;
        this.address = address;
        this.lastAddress = 0;
        this.type = type;
        this.used = false;
    }

    public String getId() {
        return id;
    }

    public int getAddress() {
        return address;
    }

    public int getLastAddress() {
        return lastAddress;
    }

    public boolean isUsed() {
        return used;
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

    public List<EDataType> getParametersTypes() {
        if (type != ESymbolTableType.FUNCTION) {
            throw new RuntimeException("Parameters types can only be retrieved for functions");
        }

        return parametersTypes == null ? new ArrayList<>() : parametersTypes;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setLastAddress(int lastAddress) {this.lastAddress = lastAddress;}

    public void setReturnType(EReturnType returnType) {
        if (type != ESymbolTableType.FUNCTION) {
            throw new RuntimeException("Return type can only be set for functions");
        }

        this.returnType = returnType;
    }

    public void setParametersTypes(List<EDataType> parametersTypes) {
        if (type != ESymbolTableType.FUNCTION) {
            throw new RuntimeException("Parameters types can only be set for functions");
        }

        this.parametersTypes = parametersTypes;
    }

    public void setUsed() {
        this.used = true;
    }

    @Override
    public String toString() {
        return "SymbolTableItem{" +
                "id='" + id + '\'' +
                ", used=" + used +
                ", address=" + address +
                ", type=" + type +
                ", returnType=" + returnType +
                ", parametersTypes=" + parametersTypes +
                '}';
    }
}
