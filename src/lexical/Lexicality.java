package lexical;

import util.OutputWriter;

import java.util.ArrayList;

public class Lexicality {
    static ArrayList<Lexicality> lexicalities;

    public static void init() {
        lexicalities = new ArrayList<Lexicality>();
    }

    public static void outputAll() {
        for (Lexicality i : lexicalities) {
            OutputWriter.writeln(String.format("%s %s", i.getType(), i.getContent()));
        }
    }

    public static void add(Lexicality l) {
        lexicalities.add(l);
    }

    public static void solve(String word, int line) {
//        System.out.println(word);
        Lexicality lexicality = new Lexicality(word);
        lexicality.setLineNumber(line);
        if (word.matches("\".*\"")) {
            lexicality.setType("STRCON");
        } else if (word.equals("main")) {
            lexicality.setType("MAINTK");
        } else if (word.equals("const")) {
            lexicality.setType("CONSTTK");
        } else if (word.equals("int")) {
            lexicality.setType("INTTK");
        } else if (word.equals("break")) {
            lexicality.setType("BREAKTK");
        } else if (word.equals("continue")) {
            lexicality.setType("CONTINUETK");
        } else if (word.equals("if")) {
            lexicality.setType("IFTK");
        } else if (word.equals("else")) {
            lexicality.setType("ELSETK");
        } else if (word.equals("!")) {
            lexicality.setType("NOT");
        } else if (word.equals("&&")) {
            lexicality.setType("AND");
        } else if (word.equals("||")) {
            lexicality.setType("OR");
        } else if (word.equals("while")) {
            lexicality.setType("WHILETK");
        } else if (word.equals("getint")) {
            lexicality.setType("GETINTTK");
        } else if (word.equals("printf")) {
            lexicality.setType("PRINTFTK");
        } else if (word.equals("return")) {
            lexicality.setType("RETURNTK");
        } else if (word.equals("+")) {
            lexicality.setType("PLUS");
        } else if (word.equals("-")) {
            lexicality.setType("MINU");
        } else if (word.equals("void")) {
            lexicality.setType("VOIDTK");
        } else if (word.equals("*")) {
            lexicality.setType("MULT");
        } else if (word.equals("/")) {
            lexicality.setType("DIV");
        } else if (word.equals("%")) {
            lexicality.setType("MOD");
        } else if (word.equals("<")) {
            lexicality.setType("LSS");
        } else if (word.equals("<=")) {
            lexicality.setType("LEQ");
        } else if (word.equals(">")) {
            lexicality.setType("GRE");
        } else if (word.equals(">=")) {
            lexicality.setType("GEQ");
        } else if (word.equals("==")) {
            lexicality.setType("EQL");
        } else if (word.equals("!=")) {
            lexicality.setType("NEQ");
        } else if (word.equals("=")) {
            lexicality.setType("ASSIGN");
        } else if (word.equals(";")) {
            lexicality.setType("SEMICN");
        } else if (word.equals(",")) {
            lexicality.setType("COMMA");
        } else if (word.equals("(")) {
            lexicality.setType("LPARENT");
        } else if (word.equals(")")) {
            lexicality.setType("RPARENT");
        } else if (word.equals("[")) {
            lexicality.setType("LBRACK");
        } else if (word.equals("]")) {
            lexicality.setType("RBRACK");
        } else if (word.equals("{")) {
            lexicality.setType("LBRACE");
        } else if (word.equals("}")) {
            lexicality.setType("RBRACE");
        } else if (word.matches("[\\_a-zA-Z]+[\\_a-z0-9A-Z]*")) {
            lexicality.setType("IDENFR");
        } else if (word.matches("^(0|[1-9][0-9]*)$")) {
            lexicality.setType("INTCON");
        } else {
            return;
        }
        Lexicality.add(lexicality);
    }

    String type;
    String content;
    int lineNumber;

    Lexicality(String content, String type) {
        this.content = content;
        this.type = type;
    }

    Lexicality(String content) {
        this.content = content;
    }

    public String toString() {
        return String.format("%s %s", type, content);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public static void main(String args[]) {
        System.out.println("abc".matches("^[_|a-z][_|a-z|0-9]*"));
    }
}
