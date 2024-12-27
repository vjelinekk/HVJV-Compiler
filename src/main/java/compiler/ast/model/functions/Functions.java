package compiler.ast.model.functions;

import java.util.List;

public class Functions {
    private final List<Function> functions;

    public Functions(List<Function> functions) {
        this.functions = functions;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Function function : functions) {
            sb.append(function.toString()).append("\n");
        }
        return sb.toString();
    }
}
