package compiler.ast.model.functions;

import compiler.ast.model.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class Arguments {
    private final List<Expression> arguments;

    public Arguments(List<Expression> arguments) {
        this.arguments = arguments;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Expression argument : arguments) {
            sb.append(argument.toString());
            sb.append(", ");
        }
        return sb.toString();
    }
}
