package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.CompilerMode;
import util.Error;
import util.ErrorWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stmt extends ParserUnit {
    Stmt() {
        type = "Stmt";
    }

    public static Stmt parser(LexicalitySupporter lexicalitySupporter) {
        if (CompilerMode.getDebug()) {
            System.out.print("Stmt ");
            System.out.println(stmtType(lexicalitySupporter));
        }

        Stmt stmt = new Stmt();
        switch (stmtType(lexicalitySupporter)) {
            case 1:
                stmt.add(LVal.parser(lexicalitySupporter));
                stmt.add(lexicalitySupporter.readAndNext());
                if (Exp.pretreat(lexicalitySupporter)) {
                    stmt.add(Exp.parser(lexicalitySupporter));
                } else if (lexicalitySupporter.read().getType().equals("GETINTTK")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                    stmt.add(lexicalitySupporter.readAndNext());
                    if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                        stmt.add(lexicalitySupporter.readAndNext());
                    } else {
                        stmt.add(new Lexicality(")", "RPARENT"));
                        ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'j'));
                    }
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
                }
                break;
            case 2:
                if (Exp.pretreat(lexicalitySupporter))
                    stmt.add(Exp.parser(lexicalitySupporter));
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
                }
                break;
            case 3:
                stmt.add(Block.parser(lexicalitySupporter));
                break;
            case 4:
            case 5:
                boolean flag = true;
                if (lexicalitySupporter.read().getType().equals("WHILETK"))
                    flag = false;
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(Cond.parser(lexicalitySupporter));
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'j'));
                }
                stmt.add(Stmt.parser(lexicalitySupporter));

                if (flag && lexicalitySupporter.read().getType().equals("ELSETK")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                    stmt.add(Stmt.parser(lexicalitySupporter));
                }
                break;
            case 6:
                stmt.add(lexicalitySupporter.readAndNext());
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
                }
                break;
            case 7:
                stmt.add(lexicalitySupporter.readAndNext());
                if (Exp.pretreat(lexicalitySupporter)) {
                    stmt.add(Exp.parser(lexicalitySupporter));
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
                }
                break;
            case 8:
                int line = lexicalitySupporter.read().getLineNumber();
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());
                int tot = count(lexicalitySupporter.read());
                stmt.add(lexicalitySupporter.readAndNext());//格式字符串
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    tot--;
                    stmt.add(lexicalitySupporter.readAndNext());
                    stmt.add(Exp.parser(lexicalitySupporter));
                }
                if (tot != 0) {
                    ErrorWriter.add(new Error(line, 'l'));
                }
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'j'));
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
                }
                break;
            case 9:
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(Stmt.parser(lexicalitySupporter));
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(Cond.parser(lexicalitySupporter));
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());
        }
        return stmt;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (stmtType(lexicalitySupporter) != 0) {
            return true;
        }
        return false;

    }

    public static int stmtType(LexicalitySupporter lexicalitySupporter) {
        if (LVal.pretreat(lexicalitySupporter)) {
            LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
            LVal.parser(lexicalitySupporter1);
            if (!lexicalitySupporter1.read().getType().equals("ASSIGN")) return 2;
            return 1;
        } else if (Exp.pretreat(lexicalitySupporter) ||
                lexicalitySupporter.read().getType().equals("SEMICN")) {
            return 2;
        } else if (Block.pretreat(lexicalitySupporter)) {
            return 3;
        } else if (lexicalitySupporter.read().getType().equals("IFTK")) {
            return 4;
        } else if (lexicalitySupporter.read().getType().equals("WHILETK")) {
            return 5;
        } else if (lexicalitySupporter.read().getType().equals("BREAKTK")) {
            return 6;
        } else if (lexicalitySupporter.read().getType().equals("CONTINUETK")) {
            return 6;
        } else if (lexicalitySupporter.read().getType().equals("RETURNTK")) {
            return 7;
        } else if (lexicalitySupporter.read().getType().equals("PRINTFTK")) {
            return 8;
        }else if(lexicalitySupporter.read().getType().equals("REPEATTK")){
            return 9;
        }
        return 0;
    }

    public static int count(Lexicality lexicality) {
        Pattern pattern = Pattern.compile("%d");
        Matcher matcher = pattern.matcher(lexicality.getContent());
        int count = 0;
        while (matcher.find())
            count++;
        return count;
    }

    public static void main(String[] args) {
        String hello = "awd\n%daefdas%d";
        Pattern pattern = Pattern.compile("%d");
        Matcher matcher = pattern.matcher(hello);
        int count = 0;
        while (matcher.find())
            count++;
        System.out.println(count);
    }

    public void setup() {
        if (nodes.get(0) instanceof LVal) {
            getVariable(((LVal) nodes.get(0)).getVariableName(), ((LVal) nodes.get(0)).getVariableLineNumber(), true);
        } else if (nodes.get(0).getType().equals("RETURNTK")) {
            if (nodes.get(1).getType().equals("Exp") && !shouldReturnValue()) {
                ErrorWriter.add(new Error(((Lexicality) nodes.get(0)).getLineNumber(), 'f'));
            }
        } else if (nodes.get(0).getType().equals("BREAKTK") || nodes.get(0).getType().equals("CONTINUETK")) {
            if (!isLoop())
                ErrorWriter.add(new Error(((Lexicality) nodes.get(0)).getLineNumber(), 'm'));
        }
        super.setup();
    }

    public boolean judgeLoop() {
        if (nodes.get(0).getType().equals("WHILETK"))
            return true;
        return false;
    }

    public boolean isReturnStmt(){
        if(nodes.get(0).getType().equals("RETURNTK")) return true;
        return false;
    }
}
