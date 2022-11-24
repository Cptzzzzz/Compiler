package backend;

import midend.ir.*;
import midend.util.IRSupporter;
import midend.util.Value;
import util.MipsWriter;

import java.util.ArrayList;

public class Mips {
    private static int indent = 0;

    private static void addIndent() {
        indent += 4;
    }

    private static void subIndent() {
        indent -= 4;
    }

    public static void generate() {
        init();
        data();
        text();
    }

    public static void init() {
        ArrayList<IRCode> irCodes = IRSupporter.getInstance().getIrCodes();
        int size = 4, pointer = 0, length = irCodes.size();
        for (int i = 0; i < length; i++) {
            IRCode irCode = irCodes.get(i);
            if (irCode instanceof FuncEntry) {
                size = 4;
                pointer = i;
            } else if (irCode instanceof Declaration) {
                size += ((Declaration) irCode).getSize();
            } else if (irCode instanceof FuncEnd) {
                ((FuncEntry) irCodes.get(pointer)).setSize(size);
            }
        }
        for (IRCode irCode : irCodes) {
            if (irCode instanceof FuncEntry)
                size = ((FuncEntry) irCode).getSize();
            else if (irCode instanceof Return)
                ((Return) irCode).setSize(size);
        }
        for (IRCode irCode : irCodes) {
            if (irCode instanceof FuncEntry) {
                size = ((FuncEntry) irCode).getSize() - 4;
            } else if (irCode instanceof Declaration) {
                Declaration declaration = (Declaration) irCode;
                if (declaration.isGlobal())
                    SymbolManager.getInstance().addSymbol(declaration.getName(), declaration.getType());
                else {
                    size -= declaration.getSize();
                    SymbolManager.getInstance().addSymbol(declaration.getName(), declaration.getType(), size, declaration.isReference());
                }
            }
        }
    }

    public static void data() {
        writeln(".data");
        addIndent();
        for (IRCode irCode : IRSupporter.getInstance().getIrCodes()) {
            if (irCode instanceof Declaration) {
                Declaration declaration = (Declaration) irCode;
                if (declaration.isGlobal()) {
                    StringBuilder s = new StringBuilder();
                    s.append(String.format("%s: .word ", declaration.getName()));
                    for (int value : declaration.getValues()) {
                        s.append(String.format(" %d,", value));
                    }
                    writeln(String.valueOf(s).substring(0, s.length() - 1));
                }
            } else if (irCode instanceof PrintString) {
                writeln(String.format("print_%d: .asciiz \"%s\"", ((PrintString) irCode).getNumber(), ((PrintString) irCode).getContent()));
            }
        }
        subIndent();
    }

    public static void text() {
        writeln(".text");
        addIndent();
        writeln("jal main_function");
        writeln("li $v0, 10");
        writeln("syscall");
        for (IRCode irCode : IRSupporter.getInstance().getIrCodes()) {
            if (irCode instanceof Declaration && ((Declaration) irCode).isGlobal()) {
                continue;
            }
            if (irCode instanceof BinaryAssign) {
                BinaryAssign binaryAssign = (BinaryAssign) irCode;
                SymbolManager.getInstance().load(binaryAssign.getRight(0), "$t3");
                SymbolManager.getInstance().store(binaryAssign.getRight(0), "$t3");
                SymbolManager.getInstance().load(binaryAssign.getRight(1), "$t4");
                SymbolManager.getInstance().load(binaryAssign.getRight(0), "$t3");
                switch (binaryAssign.getOperator()) {
                    case PLUS:
                        Mips.writeln("addu $t3, $t3, $t4");
                        break;
                    case MINUS:
                        Mips.writeln("subu $t3, $t3, $t4");
                        break;

                    case MULTI:
                        Mips.writeln("mul $t3, $t3, $t4");
                        break;

                    case DIV:
                        Mips.writeln("div $t3, $t3, $t4");
                        break;

                    case MOD:
                        Mips.writeln("div $t3,$t4");
                        Mips.writeln("mfhi $t3");
                        break;

                    case EQL:
                        Mips.writeln("seq $t3, $t3, $t4");
                        break;

                    case NEQ:
                        Mips.writeln("sne $t3, $t3, $t4");
                        break;

                    case GRE:
                        Mips.writeln("sgt $t3, $t3, $t4");
                        break;

                    case LSS:
                        Mips.writeln("sgt $t3, $t4, $t3");
                        break;

                    case LEQ:
                        Mips.writeln("sle $t3, $t3, $t4");
                        break;

                    case GEQ:
                        Mips.writeln("sge $t3, $t3, $t4");
                        break;
                    default:
                }
                SymbolManager.getInstance().store(binaryAssign.getLeft(), "$t3");
            } else if (irCode instanceof Branch) {
                Branch branch = (Branch) irCode;
                SymbolManager.getInstance().load(branch.getCondition(), "$t3");
                if (branch.isZeroBranch()) {
                    Mips.writeln(String.format("beqz $t3, %s", branch.getLabel()));
                } else {
                    Mips.writeln(String.format("bnez $t3, %s", branch.getLabel()));
                }
            } else if (irCode instanceof FuncCall) {
                FuncCall funccall = ((FuncCall) irCode);
                int index = -8;
                for (Value value : funccall.getParams()) {
                    SymbolManager.getInstance().load(value, "$t3");
                    Mips.writeln(String.format("sw $t3, %d($sp)", index));
                    index -= 4;
                }
                Mips.writeln(String.format("jal %s", funccall.getName()));
                if (funccall.getResult() != null)
                    SymbolManager.getInstance().store(funccall.getResult(), "$v0");
            } else if (irCode instanceof FuncEntry) {
                FuncEntry funcEntry = (FuncEntry) irCode;
                Mips.writeln(String.format("%s:", funcEntry.getName()));
                Mips.writeln(String.format("addu $sp,$sp,-%d", funcEntry.getSize()));
                Mips.writeln(String.format("sw $ra,%d($sp)", funcEntry.getSize() - 4));
            } else if (irCode instanceof GetInt) {
                GetInt getInt = (GetInt) irCode;
                Mips.writeln("li $v0, 5");
                Mips.writeln("syscall");
                SymbolManager.getInstance().store(getInt.getValue(), "$v0");
            } else if (irCode instanceof Jump) {
                Jump jump = (Jump) irCode;
                Mips.writeln(String.format("j %s", jump.getLabel()));
            } else if (irCode instanceof Label) {
                Label label = (Label) irCode;
                Mips.writeln(String.format("%s:", label.getLabel()));
            } else if (irCode instanceof PrintNumber) {
                PrintNumber printNumber = (PrintNumber) irCode;
                SymbolManager.getInstance().load(printNumber.getValue(), "$a0");
                Mips.writeln("li $v0, 1");
                Mips.writeln("syscall");
            } else if (irCode instanceof PrintString) {
                PrintString printString = (PrintString) irCode;
                Mips.writeln(String.format("la $a0, print_%d", printString.getNumber()));
                Mips.writeln("li $v0, 4");
                Mips.writeln("syscall");
            } else if (irCode instanceof Return) {
                Return return1 = (Return) irCode;
                if (return1.getValue() != null) {
                    SymbolManager.getInstance().load(return1.getValue(), "$v0");
                }
                Mips.writeln(String.format("lw $ra,%d($sp)", return1.getSize() - 4));
                Mips.writeln(String.format("addu $sp,$sp,%d", return1.getSize()));
                Mips.writeln("jr $ra");
            } else if (irCode instanceof UnaryAssign) {
                UnaryAssign unaryAssign = (UnaryAssign) irCode;
                SymbolManager.getInstance().load(unaryAssign.getRight(), "$t3");
                switch (unaryAssign.getOperator()) {
                    case MINUS:
                        Mips.writeln("neg $t3, $t3");
                        break;
                    case NOT:
                        Mips.writeln("seq $t3, $t3, $zero");
                        break;
                    default:
                }
                SymbolManager.getInstance().store(unaryAssign.getLeft(), "$t3");
            }
        }
        subIndent();
    }

    public static void writeln(String string) {
        for (int i = 0; i < indent; i++)
            MipsWriter.write(" ");
        MipsWriter.writeln(string);
    }
}
