package compiler.semgen.symboltable;

import compiler.semgen.CodeBuilder;
import compiler.semgen.Instruction;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;
import compiler.semgen.exception.ExceptionContext;
import compiler.semgen.exception.SemanticAnalysisException;

import java.util.*;


public class SymbolTable {
    private static class Scope {
        public final Map<String, SymbolTableItem> items;
        public final boolean isFunctionScope;
        private int freeAddress;
        private int deallocated;
        private final Map<String, Integer> gotoLabels;

        public Scope() {
            this.items = new HashMap<>();
            this.isFunctionScope = true;
            this.freeAddress = 3;
            this.deallocated = 0;
            this.gotoLabels = new HashMap<>();
        }

        public Scope(int freeAddress, int deallocated) {
            this.items = new HashMap<>();
            this.isFunctionScope = false;
            this.freeAddress = freeAddress;
            this.deallocated = deallocated;
            this.gotoLabels = new HashMap<>();
        }

        public void addGotoLabel(String label, int address) throws SemanticAnalysisException {
            if (gotoLabels.containsKey(label)) {
                throw new SemanticAnalysisException(
                        "Label " + label + " already declared in this scope",
                        ExceptionContext.getLineNumber(),
                        ExceptionContext.getFunctionName()
                );
            }

            gotoLabels.put(label, address);
        }

        public int getGotoLabelAddress(String label) throws SemanticAnalysisException {
            if (!gotoLabels.containsKey(label)) {
                throw new SemanticAnalysisException(
                        "Label " + label + " not declared in this scope",
                        ExceptionContext.getLineNumber(),
                        ExceptionContext.getFunctionName()
                );
            }

            return gotoLabels.get(label);
        }

        public void deallocate(Scope scope) {
            int variablesCount = scope.items.size();

            deallocated += variablesCount;
        }

        public void allocateMemory(int variablesCount) {
            if(variablesCount > deallocated) {
                variablesCount -= deallocated;
                deallocated += variablesCount;
                CodeBuilder.addInstruction(new Instruction(EInstruction.INT,0, variablesCount));
            }
        }

        public int assignAddress() {
            if(deallocated > 0)
                deallocated--;
            return freeAddress++;
        }
    }

    private final Stack<Scope> scopeStack;

    public SymbolTable() {
        this.scopeStack = new Stack<>();
        scopeStack.push(new Scope());
        System.out.println(scopeStack.size());
    }

    public void enterScope(boolean isFunctionScope, int variablesCount) {
        if (isFunctionScope) {
            scopeStack.push(new Scope());
            return;
        }

        Scope lastScope = scopeStack.peek();
        Scope newScope = new Scope(lastScope.freeAddress, lastScope.deallocated);
        lastScope.deallocated = 0;
        newScope.allocateMemory(variablesCount);
        scopeStack.push(newScope);
    }

    public void exitScope() {
        if (scopeStack.size() == 1) {
            throw new RuntimeException("Cannot exit global scope");
        }
        Scope scope = scopeStack.pop();
        scopeStack.peek().deallocate(scope);
    }

    private Scope getFunctionScope() {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            if (scope.isFunctionScope) return scope;
        }
        return null;
    }

    public boolean isCurrentFunctionScope() {
        return scopeStack.peek().isFunctionScope;
    }

    public void addItem(SymbolTableItem symbolTableItem) throws SemanticAnalysisException {
        if (scopeStack.peek().items.containsKey(symbolTableItem.getId())) {
            throw new SemanticAnalysisException(
                    "Variable " + symbolTableItem.getId() + " already declared in this scope",
                    ExceptionContext.getLineNumber(),
                    ExceptionContext.getFunctionName()
            );
        }
        scopeStack.peek().items.put(symbolTableItem.getId(), symbolTableItem);
    }

    public SymbolTableItem getItem(String identifier) throws SemanticAnalysisException {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            SymbolTableItem item = scopeStack.get(i).items.get(identifier);
            if (item != null) {
                return item;
            }

            if (scope.isFunctionScope) {
                break;
            }
        }

        throw new SemanticAnalysisException("Undeclared variable " + identifier, ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
    }

    public SymbolTableItem getFromGlobalScope(String identifier) throws SemanticAnalysisException {
        SymbolTableItem item = scopeStack.get(0).items.get(identifier);
        if (item != null) {
            return item;
        }
        throw new SemanticAnalysisException("Undeclared function " + identifier, ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
    }

    public int assignAddress() throws SemanticAnalysisException {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            if(scope != null)
                return scope.assignAddress();
        }

        throw new SemanticAnalysisException("Declaration out of function scope", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
    }

    public int getCurrentScopeFreeMemory() {
        return scopeStack.peek().deallocated;
    }

    public void addGotoLabel(String label, int address) throws SemanticAnalysisException {
        scopeStack.peek().addGotoLabel(label, address);
    }

    public int getGotoLabelAddress(String label) throws SemanticAnalysisException {
        return scopeStack.peek().getGotoLabelAddress(label);
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
