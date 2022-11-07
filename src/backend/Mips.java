package backend;

import intermediate.*;
import syntax.Variable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Mips {
    private static BufferedWriter out;
    private static int indent = 0;
    private static int paramOffset = -8;

    public static void resetParamOffset() {
        paramOffset = -8;
    }

    public static int getParamOffset() {
        paramOffset -= 4;
        return paramOffset + 4;
    }

    public static void addIndent() {
        indent += 4;
    }

    public static void minusIndent() {
        indent -= 4;
    }

    public static void start() {
        init();
        data();
        text();
        close();
    }

    public static void data() {
        writeln(".data");
        addIndent();
        for (IntermediateCode intermediateCode : IntermediateCode.get()) {
            if (intermediateCode instanceof Declaration) {
                Declaration declaration = ((Declaration) intermediateCode);
                if (declaration.isGlobal()) {
                    String res = String.format("%s: .word ", declaration.getName());
                    for (Value value : declaration.getValues())
                        res += String.format("%s,", value.toString());//  arr1: .word 1,2,3,4,5
                    writeln(res.substring(0, res.length() - 1));
                    SymbolManager.addGlobal(new Symbol(declaration));
                }
            }
        }
        writeln("_temp: .space 1000");
        for (IntermediateCode intermediateCode : IntermediateCode.get()) {
            if (intermediateCode instanceof Printf) {
                Printf printf = ((Printf) intermediateCode);
                String[] strings = printf.getStrings();
                int cnt = 0;
                int number = printf.getNumber();
                int length = strings.length;
                for (int i = 0; i < length; i++) {// str1: .asciiz "My String\n"
                    if (!strings[i].equals(""))
                        writeln(String.format("printf_str_%d_%d : .asciiz \"%s\"", number, cnt++, strings[i]));
                }
            }
        }
        minusIndent();
    }

    public static void text() {
        writeln(".text");
        writeln("la $s0,_temp");
        writeln("jal main_function");
        writeln("nop");
        writeln("nop");
        writeln("li $v0,10");
        writeln("syscall");
        addIndent();
        for (IntermediateCode intermediateCode : IntermediateCode.removeGlobal()) {
            intermediateCode.solve();
        }
        minusIndent();
    }


    public static void init() {
        SymbolManager.init();
        try {
            out = new BufferedWriter(new FileWriter("mips.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        FuncDeclaration funcDeclaration=null;
        for (IntermediateCode intermediateCode : IntermediateCode.get()) {
            if(intermediateCode instanceof Assign){
                Assign assign=((Assign) intermediateCode);
                if(assign.getLeft().isTemp()){
                    funcDeclaration.addSymbol(new Symbol(assign.getLeft().getName(),0,false,4));
                }
            }else if(intermediateCode instanceof FuncDeclaration){
                funcDeclaration=((FuncDeclaration) intermediateCode);
            }else if(intermediateCode instanceof CallFunction){
                CallFunction callFunction=((CallFunction) intermediateCode);
                funcDeclaration.addSymbol(new Symbol(callFunction.getValue().getName(),0,false,4));
            }
        }
    }

    public static void close() {
        try {
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeln(String string) {
        try {
            for (int i = 0; i < indent; i++) out.write(" ");
            out.write(string + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
