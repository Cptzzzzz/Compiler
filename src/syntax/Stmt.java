package syntax;

import lexical.LexicalitySupporter;
import util.CompilerMode;

public class Stmt extends ParserUnit {
    Stmt() {
        name = "Stmt";
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
                        //todo 错误处理
                    }
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    //todo 错误处理
                }
                break;
            case 2:
                if (Exp.pretreat(lexicalitySupporter))
                    stmt.add(Exp.parser(lexicalitySupporter));
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    //todo 错误处理
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
                    //todo 错误处理
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
                    //todo 错误处理
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
                    //todo 错误处理
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
                    //todo 错误处理
                }
                if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                    stmt.add(lexicalitySupporter.readAndNext());
                } else {
                    //todo 错误处理
                }
                break;
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
            LexicalitySupporter lexicalitySupporter1=new LexicalitySupporter(lexicalitySupporter.getPointer());
            LVal.parser(lexicalitySupporter1);
            if(!lexicalitySupporter1.read().getType().equals("ASSIGN")) return 2;
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
}
