package intermediate;

import syntax.Variable;

import java.util.ArrayList;

public class Declaration extends IntermediateCode {
    Variable variable;

    public Declaration(Variable variable) {
        this.variable=variable;
        this.type="Declaration";
    }

    public String toString() {
        return variable.toDeclarationString();
    }
}
