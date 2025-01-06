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
        public final Map<String, SymbolTableItem> items;
        public final boolean isFunctionScope;
        private int freeAddress;
        private int allocated;
        private int allocatedInThisScope;
        private final Map<String, Integer> gotoLabels;
        private final Map<String, List<Integer>> requiredLabels;

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

        public void addGotoLabel(String label, int address) throws SemanticAnalysisException {
            if (gotoLabels.containsKey(label)) {
                throw new GeneralSemanticAnalysisException(
                        "Label " + label + " already declared in this scope",
                        ExceptionContext.getLineNumber(),
                        ExceptionContext.getFunctionName()
                );
            }
            gotoLabels.put(label, address);

            if(requiredLabels.containsKey(label)) {
                for (int instructionId : requiredLabels.get(label)) {
                    CodeBuilder.getPlaceholderInstruction(instructionId).setArg2(address);
                }
                requiredLabels.remove(label);
            }
        }

        public void addRequiredLabel(String label, int jumpInstructionId) {
            if(requiredLabels.containsKey(label)) {
                requiredLabels.get(label).add(jumpInstructionId);
            }
            else {
                List<Integer> newList = new ArrayList<>();
                newList.add(jumpInstructionId);
                requiredLabels.put(label, newList);
            }
        }


        public int  getGotoLabelAddress(String label) throws SemanticAnalysisException {
//            if (!gotoLabels.containsKey(label)) {
//                throw new GeneralSemanticAnalysisException(
//                        "Label " + label + " not declared in this scope",
//                        ExceptionContext.getLineNumber(),
//                        ExceptionContext.getFunctionName()
//                );
//            }
            if (!gotoLabels.containsKey(label))
                return -1;

            return gotoLabels.get(label);
        }

        public boolean containsLabel(String label) {
            return gotoLabels.containsKey(label);
        }

        public void checkIfDeclarationIsAfterGotoLabel(int address) throws SemanticAnalysisException {
            for (Map.Entry<String, Integer> entry : gotoLabels.entrySet()) {
                if (entry.getValue() >= address) {
                    throw new GeneralSemanticAnalysisException(
                            "Declaration after goto statement",
                            ExceptionContext.getLineNumber(),
                            ExceptionContext.getFunctionName()
                    );
                }
            }
        }

        public void deallocate(Scope parentScope) {
            if (allocatedInThisScope > 0)
                CodeBuilder.addInstruction(new Instruction(EInstruction.INT,0, -allocatedInThisScope));

            for(Map.Entry<String, List<Integer>> entry : requiredLabels.entrySet())
                for(int instructionId : entry.getValue()) {
                    parentScope.addRequiredLabel(entry.getKey(), instructionId);
                }
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
            scopeStack.push(new Scope());
            return;
        }

        Scope lastScope = scopeStack.peek();
        Scope newScope = new Scope(lastScope.freeAddress, lastScope.allocated);
        newScope.allocateMemory(variablesCount);
        scopeStack.push(newScope);
    }

    public void exitScope() throws GeneralSemanticAnalysisException {
        if (scopeStack.size() == 1) {
            throw new RuntimeException("Cannot exit global scope");
        }
        Scope exiting = scopeStack.pop();
        exiting.deallocate(scopeStack.peek());
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
        scopeStack.peek().addGotoLabel(label, address);
    }

    public void addRequiredGoToLabel(String label, int jumpInstructionId) {
        scopeStack.peek().addRequiredLabel(label, jumpInstructionId);
    }

    public boolean containsGoToLabel(String label) {
        return scopeStack.getLast().containsLabel(label);
    }

    public int getGotoLabelAddress(String label) throws SemanticAnalysisException {
        int address = -1;

        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);

            address = scope.getGotoLabelAddress(label);
            if(address > 0)
                return address;

            if(scope.isFunctionScope)
                throw new RuntimeException("Label " + label + " not declared in this scope");
        }

        return address;
    }

    public void checkIfDeclarationIsAfterGotoLabel(int address) throws SemanticAnalysisException {
        scopeStack.peek().checkIfDeclarationIsAfterGotoLabel(address);
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
}
