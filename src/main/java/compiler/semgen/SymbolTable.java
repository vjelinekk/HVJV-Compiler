package compiler.semgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {
    private final Stack<Map<String, SymbolTableItem>> scopeStack;

    public SymbolTable() {
        this.scopeStack = new Stack<>();
        scopeStack.push(new HashMap<>());
    }

    public void enterScope() {
        scopeStack.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopeStack.size() == 1) {
            throw new RuntimeException("Cannot exit global scope");
        }
        scopeStack.pop();
    }

    public int getCurrentScope() {
        return scopeStack.size() - 1;
    }

    public void addItem(SymbolTableItem symbolTableItem) {
        if (scopeStack.peek().containsKey(symbolTableItem.getId())) {
            throw new RuntimeException("Variable " + symbolTableItem.getId() + " already declared in this scope");
        }
        scopeStack.peek().put(symbolTableItem.getId(), symbolTableItem);
    }

    public SymbolTableItem getItem(String identifier) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            SymbolTableItem item = scopeStack.get(i).get(identifier);
            if (item != null) {
                return item;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map<String, SymbolTableItem> scope : scopeStack) {
            sb.append("{");
            for (Map.Entry<String, SymbolTableItem> entry : scope.entrySet()) {
                sb.append(entry.getKey()).append(" ");
            }
            sb.append("}\n");
        }
        return sb.toString();
    }
}
