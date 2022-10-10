package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class Stmt extends ParserUnit {
    Stmt() {

    }

    Stmt(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    Stmt(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static boolean assign(LexicalitySupporter lexicalitySupporter) {
        lexicalitySupporter = new LexicalitySupporter(Lexicality.getSize(), lexicalitySupporter.getPointer());
        if (LVal.pretreat(lexicalitySupporter)) {
            LVal.parser(lexicalitySupporter);
            if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
                return true;
            }
        }
        return false;
    }

    public static Stmt parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        if (assign(lexicalitySupporter)) {
            arrayList.add(LVal.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
            if (Exp.pretreat(lexicalitySupporter)) {
                arrayList.add(Exp.parser(lexicalitySupporter));

            } else if (lexicalitySupporter.read().getType().equals("GETINTTK")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
                if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                    lexicalities.add(lexicalitySupporter.read());
                    lexicalitySupporter.next();
                }
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    lexicalities.add(lexicalitySupporter.read());
                    lexicalitySupporter.next();
                }
            }

            if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        } else if (Exp.pretreat(lexicalitySupporter)) {
            arrayList.add(Exp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        } else if (lexicalitySupporter.read().getType().equals("SEMICN")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        } else if (Block.pretreat(lexicalitySupporter)) {
            arrayList.add(Block.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("IFTK") ||
                lexicalitySupporter.read().getType().equals("WHILETK")) {
            boolean flag = true;
            if (lexicalitySupporter.read().getType().equals("WHILETK"))
                flag = false;
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
            if (Cond.pretreat(lexicalitySupporter)) {
                arrayList.add(Cond.parser(lexicalitySupporter));
            }
            if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
            if (Stmt.pretreat(lexicalitySupporter)) {
                arrayList.add(Stmt.parser(lexicalitySupporter));
            }
            if (flag && lexicalitySupporter.read().getType().equals("ELSETK")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
                if (Stmt.pretreat(lexicalitySupporter)) {
                    arrayList.add(Stmt.parser(lexicalitySupporter));
                }
            }
        } else if (lexicalitySupporter.read().getType().equals("BREAKTK") ||
                lexicalitySupporter.read().getType().equals("CONTINUETK")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        } else if (lexicalitySupporter.read().getType().equals("RETURNTK")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (Exp.pretreat(lexicalitySupporter)) {
                arrayList.add(Exp.parser(lexicalitySupporter));
            }
            if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        } else if (lexicalitySupporter.read().getType().equals("PRINTFTK")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
            if (lexicalitySupporter.read().getType().equals("STRCON")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
            while (lexicalitySupporter.read().getType().equals("COMMA")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
                if (Exp.pretreat(lexicalitySupporter)) {
                    arrayList.add(Exp.parser(lexicalitySupporter));
                }
            }
            if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
            if (lexicalitySupporter.read().getType().equals("SEMICN")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        }

        return new Stmt("Stmt", arrayList, lexicalities);
    }

    public void output() {
        if (lexicalities.isEmpty()) {
            derivations.get(0).output();
        } else if (lexicalities.get(0).getContent().equals("if") ||
                lexicalities.get(0).getContent().equals("while")) {
            OutputWriter.writeln(lexicalities.get(0).toString());
            OutputWriter.writeln(lexicalities.get(1).toString());
            derivations.get(0).output();
            OutputWriter.writeln(lexicalities.get(2).toString());
            derivations.get(1).output();
            if (3 < lexicalities.size()) {
                OutputWriter.writeln(lexicalities.get(3).toString());
                derivations.get(2).output();
            }
        } else if (lexicalities.get(0).getContent().equals("break") ||
                lexicalities.get(0).getContent().equals("continue")) {
            OutputWriter.writeln(lexicalities.get(0).toString());
            OutputWriter.writeln(lexicalities.get(1).toString());
        } else if (lexicalities.get(0).getContent().equals("return")) {
            OutputWriter.writeln(lexicalities.get(0).toString());
            if (!derivations.isEmpty()) {
                derivations.get(0).output();
            }
            OutputWriter.writeln(lexicalities.get(1).toString());
        } else if (lexicalities.get(0).getContent().equals("printf")) {
            OutputWriter.writeln(lexicalities.get(0).toString());
            OutputWriter.writeln(lexicalities.get(1).toString());
            OutputWriter.writeln(lexicalities.get(2).toString());
            int length = derivations.size();
            for (int i = 0; i < length; i++) {
                OutputWriter.writeln(lexicalities.get(i + 3).toString());
                derivations.get(i).output();
            }
            length = lexicalities.size();
            OutputWriter.writeln(lexicalities.get(length - 2).toString());
            OutputWriter.writeln(lexicalities.get(length - 1).toString());
        } else if (lexicalities.get(0).getContent().equals(";")) {
            if (!derivations.isEmpty()) {
                derivations.get(0).output();
            }
            OutputWriter.writeln(lexicalities.get(0).toString());
        } else if (derivations.get(0).getName().equals("Exp")) {
            derivations.get(0).output();
            OutputWriter.writeln(lexicalities.get(0).toString());
        } else if (derivations.get(0).getName().equals("LVal")) {
            derivations.get(0).output();
            if (lexicalities.get(1).getContent().equals(";")) {
                OutputWriter.writeln(lexicalities.get(0).toString());
                derivations.get(1).output();
                OutputWriter.writeln(lexicalities.get(1).toString());
            } else {
                for (Lexicality lexicality : lexicalities) {
                    OutputWriter.writeln(lexicality.toString());
                }
            }
        }
        OutputWriter.writeln(String.format("<%s>", name));
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (LVal.pretreat(lexicalitySupporter)) {
            return true;
        } else if (Exp.pretreat(lexicalitySupporter) ||
                lexicalitySupporter.read().getType().equals("SEMICN")) {
            return true;
        } else if (Block.pretreat(lexicalitySupporter)) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("IFTK")) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("WHILETK")) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("BREAKTK")) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("RETURNTK")) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("PRINTFTK")) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("CONTINUETK")) {
            return true;
        }
        return false;
    }


}
