package lexical;

import util.OutputWriter;

import java.util.*;

public class Lexicality {
    static ArrayList<Lexicality> lexicalities;
    static HashMap<String, String> keywords;
    static HashMap<String,String> symbols;
    static HashMap<String,String> others;
    public Lexicality(){

    }
    public static void init() {
        lexicalities = new ArrayList<Lexicality>();
        keywords =new HashMap<String,String>();
        keywords.put("main","MAINTK");
        keywords.put("const","CONSTTK");
        keywords.put("int","INTTK");
        keywords.put("break","BREAKTK");
        keywords.put("continue","CONTINUETK");
        keywords.put("if","IFTK");
        keywords.put("else","ELSETK");
        keywords.put("while","WHILETK");
        keywords.put("getint","GETINTTK");
        keywords.put("printf","PRINTFTK");
        keywords.put("return","RETURNTK");
        keywords.put("void","VOIDTK");

        symbols=new HashMap<String,String>();
        symbols.put("\\!","NOT");
        symbols.put("\\&\\&","AND");
        symbols.put("\\|\\|","OR");
        symbols.put("\\+","PLUS");
        symbols.put("\\-","MINU");
        symbols.put("\\*","MULT");
        symbols.put("\\/","DIV");
        symbols.put("\\%","MOD");
        symbols.put("\\<","LSS");
        symbols.put("\\<\\=","LEQ");
        symbols.put("\\>","GRE");
        symbols.put("\\>=","GEQ");
        symbols.put("\\=\\=","EQL");
        symbols.put("\\!\\=","NEQ");
        symbols.put("\\=","ASSIGN");
        symbols.put("\\;","SEMICN");
        symbols.put("\\,","COMMA");
        symbols.put("\\(","LPARENT");
        symbols.put("\\)","RPARENT");
        symbols.put("\\[","LBRACK");
        symbols.put("\\]","RBRACK");
        symbols.put("\\{","LBRACE");
        symbols.put("\\}","RBRACE");

        others=new HashMap<String,String>();
        others.put("\".*\"","STRCON");
        others.put("[\\_a-zA-Z]+[\\_a-z0-9A-Z]*","IDENFR");
        others.put("^(0|[1-9][0-9]*)$","INTCON");
    }

    public static void outputAll() {
        for (Lexicality i : lexicalities) {
            OutputWriter.writeln(i.toString());
        }
    }

    public static Lexicality get(int index) {
        return lexicalities.get(index);
    }

    public static ArrayList<Lexicality> get(int start, int end) {
        return new ArrayList<>(lexicalities.subList(start, end));
    }

    public static int getSize() {
        return lexicalities.size();
    }

    public static void add(Lexicality l) {
        lexicalities.add(l);
    }

    public static void solve(String word, int line) {
        Lexicality lexicality = new Lexicality(word);
        lexicality.setLineNumber(line);
        for(String i: keywords.keySet()){
            if(word.matches(i)){
                lexicality.setType(keywords.get(i));
                Lexicality.add(lexicality);
                return;
            }
        }
        for(String i: symbols.keySet()){
            if(word.matches(i)){
                lexicality.setType(symbols.get(i));
                Lexicality.add(lexicality);
                return;
            }
        }
        for(String i: others.keySet()){
            if(word.matches(i)){
                lexicality.setType(others.get(i));
                Lexicality.add(lexicality);
                return;
            }
        }
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

    public void output(){
        OutputWriter.writeln(toString());
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

    }
}
