package compiler.ast.model.functions;

import java.util.List;

public class Parameters {
    private final List<Parameter> parameters;

    public Parameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public String toString() {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int count = parameters.size();
        for (Parameter parameter : parameters) {
            if (--count == 0) {
                sb.append(parameter.toString());
                break;
            }
            sb.append(parameter.toString()).append(", ");
        }
        return sb.toString();
    }

    public int getCount() {
        if (parameters == null || parameters.isEmpty()) {
            return 0;
        }
        return parameters.size();
    }
}
