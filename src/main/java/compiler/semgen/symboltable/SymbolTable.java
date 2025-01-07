package compiler.semgen.symboltable;

import compiler.semgen.CodeBuilder;
import compiler.semgen.Instruction;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.exception.ExceptionContext;
import compiler.semgen.exception.GeneralSemanticAnalysisException;
import compiler.semgen.exception.SemanticAnalysisException;

import java.util.*;


public class SymbolTable {

    private static class Scope {

        private static class Pair {
            public final int address;
            public final int currentMemory;

            public Pair(int address, int currentMemory) {
                this.address = address;
                this.currentMemory = currentMemory;
            }
        }

        public final Map<String, SymbolTableItem> items;
        public final boolean isFunctionScope;
        private int freeAddress;
        private int allocated;
        private int allocatedInThisScope;
        private final Map<String, Pair> gotoLabels;
        private final Map<String, List<Pair>> requiredLabels;

        public Scope() {
            this.items = new HashMap<>();
            this.isFunctionScope = true;
            this.freeAddress = 3;
            this.allocated = 0;
            this.allocatedInThisScope = 0;
            this.gotoLabels = new HashMap<>();
            this.requiredLabels = new HashMap<>();
        }

        public Scope(int freeAddress, int allocated) {
            this.items = new HashMap<>();
            this.isFunctionScope = false;
            this.freeAddress = freeAddress;
            this.allocated = allocated;
            this.allocatedInThisScope = 0;
            this.gotoLabels = new HashMap<>();
            this.requiredLabels = new HashMap<>();
        }

        public void addGotoLabel(String label, int address, int memory) throws SemanticAnalysisException {
            if (gotoLabels.containsKey(label)) {
                throw new GeneralSemanticAnalysisException(
                        "Label " + label + " already declared in this scope",
                        ExceptionContext.getLineNumber(),
                        ExceptionContext.getFunctionName()
                );
            }
            Pair pair = new Pair(address, memory);
            gotoLabels.put(label, pair);

            if(requiredLabels.containsKey(label)) {
                for (Pair reqL : requiredLabels.get(label)) {
                    CodeBuilder.getPlaceholderInstruction(reqL.address - 1).setArg2(memory - reqL.currentMemory);
                    CodeBuilder.getPlaceholderInstruction(reqL.address).setArg2(address);
                }
                requiredLabels.remove(label);
            }

        }

        public void addRequiredLabel(String label, int gotoId, int memory) {
            Pair pair = new Pair(gotoId, memory);

            if(!requiredLabels.containsKey(label))
                requiredLabels.put(label, new ArrayList<>());

            requiredLabels.get(label).add(pair);
        }


        public int getGotoLabelAddress(String label) {
            if (!gotoLabels.containsKey(label))
                return -1;

            return gotoLabels.get(label).address;
        }

        public boolean containsLabel(String label) {
            return gotoLabels.containsKey(label);
        }

        public void deallocate(Scope parentScope) throws SemanticAnalysisException {
            if (allocatedInThisScope > 0)
                CodeBuilder.addInstruction(new Instruction(EInstruction.INT,0, -allocatedInThisScope));

            // share required labels with parent
            for(Map.Entry<String, List<Pair>> entry : requiredLabels.entrySet())
                for(Pair reqL : entry.getValue()) {
                    parentScope.addRequiredLabel(entry.getKey(), reqL.address, reqL.currentMemory);
                    System.out.println("required " + entry.getKey());
                }

            // share newly created labels with parent
            for(Map.Entry<String, Pair> entry : gotoLabels.entrySet()) {
                parentScope.addGotoLabel(entry.getKey(), entry.getValue().address, entry.getValue().currentMemory);
                System.out.println("newly created " + entry.getKey());
            }
            System.out.println("----------------------");
        }

        public void allocateMemory(int variablesCount) {
            if(variablesCount > allocated) {
                allocatedInThisScope = variablesCount - allocated;
                allocated += allocatedInThisScope;
                CodeBuilder.addInstruction(new Instruction(EInstruction.INT,0, allocatedInThisScope));
            }
        }

        public int assignAddress() {
            if(allocated > 0)
                allocated--;
            return freeAddress++;
        }
    }

    private final Stack<Scope> scopeStack;

    public SymbolTable() {
        this.scopeStack = new Stack<>();
        scopeStack.push(new Scope());
    }

    public void enterScope(boolean isFunctionScope, int variablesCount) {
        if (isFunctionScope) {
            Scope fcnScope = new Scope();
            fcnScope.allocateMemory(variablesCount);
            scopeStack.push(fcnScope);
            return;
        }

        Scope lastScope = scopeStack.peek();
        Scope newScope = new Scope(lastScope.freeAddress, lastScope.allocated);
        newScope.allocateMemory(variablesCount);
        scopeStack.push(newScope);
    }

    public void exitScope() throws SemanticAnalysisException {
        if (scopeStack.size() == 1) {
            throw new RuntimeException("Cannot exit global scope");
        }

        Scope exiting = scopeStack.pop();
        exiting.deallocate(scopeStack.peek());

        if (exiting.isFunctionScope && !exiting.requiredLabels.entrySet().isEmpty()) {
            throw new RuntimeException("Label " + exiting.requiredLabels.entrySet().stream().findFirst().get().getKey() + " not declared in this scope");
        }
    }

    public boolean isCurrentFunctionScope() {
        return scopeStack.peek().isFunctionScope;
    }

    public void addItem(SymbolTableItem symbolTableItem) throws SemanticAnalysisException {
        if (scopeStack.peek().items.containsKey(symbolTableItem.getId())) {
            throw new GeneralSemanticAnalysisException(
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

        throw new GeneralSemanticAnalysisException("Undeclared variable " + identifier, ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
    }

    public SymbolTableItem getFromGlobalScope(String identifier) throws SemanticAnalysisException {
        SymbolTableItem item = scopeStack.get(0).items.get(identifier);
        if (item != null) {
            return item;
        }
        throw new GeneralSemanticAnalysisException("Undeclared function " + identifier, ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
    }

    public int assignAddress() throws SemanticAnalysisException {
            if(!scopeStack.isEmpty())
                return scopeStack.get(scopeStack.size() -1).assignAddress();
            else
                throw new GeneralSemanticAnalysisException("Declaration out of function scope", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
    }

    public int getCurrentScopeFreeMemory() {
        return scopeStack.peek().allocated;
    }

    public void addGotoLabel(String label, int address) throws SemanticAnalysisException {
        scopeStack.peek().addGotoLabel(label, address, getCurrentMemory());
    }

    public void addRequiredGoToLabel(String label, int jumpInstructionId) {
        scopeStack.peek().addRequiredLabel(label, jumpInstructionId, getCurrentMemory());
    }

    public int getGotoLabelAddress(String label) throws SemanticAnalysisException {
        int address = -1;

        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            address = scope.getGotoLabelAddress(label);
            if(address > 0)
                return address;
        }

        return address;
    }
    public void returnToLabel(String label) {
        int currentMemory = getCurrentMemory();
        for(int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            if (scope.gotoLabels.containsKey(label)) {
                if(currentMemory != scope.gotoLabels.get(label).currentMemory) {
                    CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0,
                                         scope.gotoLabels.get(label).currentMemory - currentMemory));
                }
                break;
            }
        }
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

    private Scope getFunctionScope() {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            if (scope.isFunctionScope) return scope;
        }
        return null;
    }

    private int getCurrentMemory() {
        int usedMemory = 0;
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            usedMemory += scope.allocatedInThisScope;

            if(scope.isFunctionScope) break;
        }
        return usedMemory;
    }
}
