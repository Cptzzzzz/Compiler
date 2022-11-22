package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.*;
import util.ErrorWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stmt extends ParserUnit {
    Stmt() {
        setType("Stmt");
    }

    private int stmtType;

    public int getStmtType() {
        return stmtType;
    }

    public void setStmtType(int stmtType) {
        this.stmtType = stmtType;
    }

    public static Stmt parser(LexicalitySupporter lexicalitySupporter) {
        Stmt stmt = new Stmt();
        stmt.setStmtType(stmtType(lexicalitySupporter));
        switch (stmt.getStmtType()) {
            case 1:
                stmt.add(LVal.parser(lexicalitySupporter));
                stmt.add(lexicalitySupporter.readAndNext());
                if (Exp.pretreat(lexicalitySupporter)) {
                    stmt.add(Exp.parser(lexicalitySupporter));
                } else if (lexicalitySupporter.read().getType().equals("GETINTTK")) {
                    stmt.setStmtType(9);
                    stmt.add(lexicalitySupporter.readAndNext());
                    stmt.add(lexicalitySupporter.readAndNext());
                    if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                        stmt.add(lexicalitySupporter.readAndNext());
                    } else {
                        stmt.add(new Lexicality(")", "RPARENT"));
                        ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'j');
                    }
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'i');
                }
                break;
            case 2:
                if (Exp.pretreat(lexicalitySupporter))
                    stmt.add(Exp.parser(lexicalitySupporter));
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'i');
                }
                break;
            case 3:
                stmt.add(Block.parser(lexicalitySupporter));
                break;
            case 4:
            case 5:
                boolean flag = !lexicalitySupporter.read().getType().equals("WHILETK");
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(Cond.parser(lexicalitySupporter));
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'j');
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
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'i');
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
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'i');
                }
                break;
            case 8:
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());
                stmt.add(lexicalitySupporter.readAndNext());//格式字符串
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                    stmt.add(Exp.parser(lexicalitySupporter));
                }
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'j');
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    stmt.add(new Lexicality(";", "SEMICN"));
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'i');
                }
                break;
        }
        return stmt;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return stmtType(lexicalitySupporter) != 0;
    }

    /*
    1: LVal = Exp ;  / LVal = getint()
    2: [Exp] ;
    3: Block
    4: if
    5: while
    6: break/continue
    7: return
    8: printf
    9: LVal = getint();
     */
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
        }
        return 0;
    }

    public void semantic() {
        switch (stmtType) {
            case 1:
            case 9:
                Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(0).getNode(0).getContent());
                if (symbol != null && symbol.isConst()) {
                    ErrorWriter.add(getNode(0).getNode(0).getLineNumber(), 'h');
                }
                break;
            case 6:
                if (state.getLoopNumber() == 0)
                    ErrorWriter.add(getNode(0).getLineNumber(), 'm');
                break;
            case 7:
                if (nodes.size() == 3 && !state.shouldReturnValue())
                    ErrorWriter.add(getNode(0).getLineNumber(), 'f');
                break;
            case 8:
                String format = getNode(2).getContent();
                String s = format.replaceAll("\\\\n", "").replaceAll("%d", "");
                int length = s.length();
                char c;
                for (int i = 1; i < length - 1; i++) {
                    c = s.charAt(i);
                    if (c != 32 && c != 33 && !(40 <= c && c <= 91) && !(93 <= c && c <= 126)) {
                        ErrorWriter.add(getNode(2).getLineNumber(), 'a');
                        break;
                    }
                }
                int tot = 0, cnt = 0;
                Pattern pattern = Pattern.compile("%d");
                Matcher matcher = pattern.matcher(format);
                while (matcher.find())
                    tot++;
                for (Node node : nodes)
                    if (node.getType().equals("COMMA"))
                        cnt++;
                if (tot != cnt)
                    ErrorWriter.add(getNode(0).getLineNumber(), 'l');
                break;
        }
        super.semantic();
    }

    public void setState(State state) {
        if (stmtType == 4) {
            this.state = new State(state.getLoopNumber(), Allocator.getInstance().getIfNumber(), nodes.size() == 7, state.shouldReturnValue(), state.getBlockNumber());
        } else if (stmtType == 5) {
            this.state = new State(Allocator.getInstance().getWhileNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue(), state.getBlockNumber());
        } else {
            this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue(), state.getBlockNumber());
        }
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
        }
    }
}
