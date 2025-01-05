package compiler.semgen.symboltable;

import compiler.semgen.CodeBuilder;
import compiler.semgen.Instruction;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.exception.ExceptionContext;
import compiler.semgen.exception.SemanticAnalysisException;

import java.util.*;


public class SymbolTable {
    private static class Scope {
        public final Map<String, SymbolTableItem> items;
        public final boolean isFunctionScope;
        private int freeAddress;
        private int memorySize;
        private final Map<String, Integer> gotoLabels;

        public Scope() {
            this.items = new HashMap<>();
            this.isFunctionScope = true;
            this.freeAddress = 3;
            this.memorySize = 0;
            this.gotoLabels = new HashMap<>();
        }

        public Scope(int freeAddress, int memorySize) {
            this.items = new HashMap<>();
            this.isFunctionScope = false;
            this.freeAddress = freeAddress;
            this.memorySize = memorySize;
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

        public void deallocate() {
            CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, -memorySize));
        }

        public void allocate() {
            CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, memorySize));
        }

        public int assignAddress() {
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
        Scope newScope = new Scope(lastScope.freeAddress, variablesCount);
        newScope.allocate();
        scopeStack.push(newScope);
    }

    public void exitScope() {
        if (scopeStack.size() == 1) {
            throw new RuntimeException("Cannot exit global scope");
        }
        scopeStack.pop().deallocate();
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

    public int getCurrentScopeMemory() {
        return scopeStack.peek().memorySize;
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
