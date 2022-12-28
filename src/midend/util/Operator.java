package midend.util;

public enum Operator {
    PLUS, MINUS, MULTI, DIV, MOD, EQL, NEQ, GRE, LSS, LEQ, GEQ, NOT, BITAND;

    @Override
    public String toString() {
        switch (this) {
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
            case BITAND:
                return "&";
            default:
                return "!";
        }
    }

    public String toName() {
        switch (this) {
            case PLUS:
                return "PLUS";
            case MINUS:
                return "MINUS";
            case MULTI:
                return "MULTI";
            case DIV:
                return "DIV";
            case MOD:
                return "MOD";
            case EQL:
                return "EQL";
            case NEQ:
                return "NEQ";
            case GRE:
                return "GRE";
            case LSS:
                return "LSS";
            case LEQ:
                return "LEQ";
            case GEQ:
                return "GEQ";
            case BITAND:
                return "BITAND";
            default:
                return "NOT";
        }
    }
}
