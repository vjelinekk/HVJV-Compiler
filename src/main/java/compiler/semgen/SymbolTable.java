package compiler.semgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class SymbolTable {
    private static class Scope {
        public final Map<String, SymbolTableItem> items;
        public final boolean isFunctionScope;
        private int freeAddress = 3;

        public Scope(boolean isFunctionScope) {
            this.items = new HashMap<>();
            this.isFunctionScope = isFunctionScope;
        }

        public boolean isFunctionScope() {
            return isFunctionScope;
        }

        public int assignAddress() {
            return freeAddress++;
        }
    }

    private final Stack<Scope> scopeStack;

    public SymbolTable() {
        this.scopeStack = new Stack<>();
        scopeStack.push(new Scope(true));
    }

    public void enterScope(boolean isFunctionScope) {
        scopeStack.push(new Scope(isFunctionScope));
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
        if (scopeStack.peek().items.containsKey(symbolTableItem.getId())) {
            throw new RuntimeException("Variable " + symbolTableItem.getId() + " already declared in this scope");
        }
        scopeStack.peek().items.put(symbolTableItem.getId(), symbolTableItem);
    }

    public SymbolTableItem getItem(String identifier) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            SymbolTableItem item = scopeStack.get(i).items.get(identifier);
            if (item != null) {
                item.setLevel(i);
                return item;
            }

            if (scope.isFunctionScope) {
                break;
            }
        }

        throw new RuntimeException("Undeclared variable " + identifier);
    }

    public SymbolTableItem getFromGlobalScope(String identifier) {
        SymbolTableItem item = scopeStack.get(0).items.get(identifier);
        if (item != null) {
            return item;
        }
        throw new RuntimeException("Undeclared function " + identifier);
    }

    public int assignAddress() {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            if(scope.isFunctionScope)
                return scope.assignAddress();
        }

        throw new RuntimeException("Declaration out of function scope");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Scope scope : scopeStack) {
            sb.append("{");
            for (Map.Entry<String, SymbolTableItem> entry : scope.items.entrySet()) {
                sb.append(entry.getKey()).append(" ");
            }
            sb.append("}\n");
        }
        return sb.toString();
    }
}
