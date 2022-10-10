package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class UnaryExp extends ParserUnit {
    UnaryExp() {

    }

    UnaryExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    UnaryExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static UnaryExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        if (PrimaryExp.pretreat(lexicalitySupporter)) {
            arrayList.add(PrimaryExp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
                if (FuncRParams.pretreat(lexicalitySupporter)) {
                    arrayList.add(FuncRParams.parser(lexicalitySupporter));
                }
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    lexicalities.add(lexicalitySupporter.read());
                    lexicalitySupporter.next();
                }
            } else {

            }
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            arrayList.add(UnaryOp.parser(lexicalitySupporter));
            if (UnaryExp.pretreat(lexicalitySupporter)) {
                arrayList.add(UnaryExp.parser(lexicalitySupporter));
            }
        }
        return new UnaryExp("UnaryExp", arrayList, lexicalities);
    }

    public void output() {
        if(lexicalities.isEmpty()){
            if(derivations.get(0).getName().equals("PrimaryExp")){
                derivations.get(0).output();
            }else if(derivations.get(0).getName().equals("UnaryOp")){
                derivations.get(0).output();
                derivations.get(1).output();
            }
        }else{
            OutputWriter.writeln(lexicalities.get(0).toString());
            OutputWriter.writeln(lexicalities.get(1).toString());
            if(!derivations.isEmpty()){
                derivations.get(0).output();
            }
            OutputWriter.writeln(lexicalities.get(2).toString());
        }
        OutputWriter.writeln(String.format("<%s>", name));
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (PrimaryExp.pretreat(lexicalitySupporter)) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            return true;
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
