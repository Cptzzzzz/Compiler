package midend.util;

public enum Operator {
    PLUS, MINUS, MULTI, DIV, MOD, EQL, NEQ, GRE, LSS, LEQ, GEQ, NOT;

    @Override
    public String toString() {
        switch (this){
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTI:
                return "*";
            case DIV:
                return "/";
            case MOD:
                return "%";
            case EQL:
                return "==";
            case NEQ:
                return "!=";
            case GRE:
                return ">";
            case LSS:
                return "<";
            case LEQ:
                return "<=";
            case GEQ:
                return ">=";
            default:
                return "!";
        }
    }
}
