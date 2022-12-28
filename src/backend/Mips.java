package backend;

import midend.ir.*;
import midend.util.IRSupporter;
import midend.util.Value;
import midend.util.ValueType;
import util.CompilerMode;
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
        if (CompilerMode.getInstance().isOptimize()) {
            setEntryCode();
            realText();
        } else
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
            } else if (irCode instanceof BinaryAssign) {
                BinaryAssign binaryAssign = (BinaryAssign) irCode;
                SymbolManager.getInstance().addWeight(binaryAssign.getRight(0));
                SymbolManager.getInstance().addWeight(binaryAssign.getRight(1));
                SymbolManager.getInstance().addWeight(binaryAssign.getLeft());
            } else if (irCode instanceof Branch) {
                SymbolManager.getInstance().addWeight(((Branch) irCode).getCondition());
            } else if (irCode instanceof FuncCall) {
                FuncCall funcCall = (FuncCall) irCode;
                for (Value value : funcCall.getParams()) {
                    SymbolManager.getInstance().addWeight(value);
                }
            } else if (irCode instanceof GetInt) {
                SymbolManager.getInstance().addWeight(((GetInt) irCode).getValue());
            } else if (irCode instanceof PrintNumber) {
                SymbolManager.getInstance().addWeight(((PrintNumber) irCode).getValue());
            } else if (irCode instanceof Return) {
                if (((Return) irCode).getValue() != null)
                    SymbolManager.getInstance().addWeight(((Return) irCode).getValue());
            } else if (irCode instanceof UnaryAssign) {
                UnaryAssign unaryAssign = (UnaryAssign) irCode;
                SymbolManager.getInstance().addWeight(unaryAssign.getRight());
                SymbolManager.getInstance().addWeight(unaryAssign.getLeft());
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
                SymbolManager.getInstance().load(binaryAssign.getRight(1), "$t4");
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
                        if (binaryAssign.getRight(1).getType() == ValueType.Imm) {
                            DivImprove.getInstance().optimize("$t3", "$t4", binaryAssign.getRight(1).getValue());
                        } else {
                            Mips.writeln("div $t3, $t3, $t4");
                        }
                        break;

                    case MOD:
                        if (binaryAssign.getRight(1).getType() == ValueType.Imm) {
                            DivImprove.getInstance().optimize("$t3", "$t4", binaryAssign.getRight(1).getValue());
                            Mips.writeln("mul $t3,$t3," + binaryAssign.getRight(1).getValue());
                            SymbolManager.getInstance().load(binaryAssign.getRight(0), "$t4");
                            Mips.writeln("subu $t3,$t4,$t3");
                        } else {
                            Mips.writeln("div $t3,$t4");
                            Mips.writeln("mfhi $t3");
                        }
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

    private static void setEntryCode() {
        for (IRCode irCode : IRSupporter.getInstance().getIrCodes()) {
            irCode.setEntryCode(false);
        }
        ArrayList<String> labels = new ArrayList<>();
        boolean flag = true;
        for (IRCode irCode : IRSupporter.getInstance().getIrCodes()) {
            if (flag) {
                irCode.setEntryCode();
                flag = false;
            }
            if (irCode instanceof Jump) {
                labels.add(((Jump) irCode).getLabel());
                flag = true;
            } else if (irCode instanceof Branch) {
                labels.add(((Branch) irCode).getLabel());
                flag = true;
            }
        }
        for (IRCode irCode : IRSupporter.getInstance().getIrCodes()) {
            if (irCode instanceof Label && labels.contains(((Label) irCode).getLabel())) {
                irCode.setEntryCode();
            } else if (irCode instanceof FuncEntry) {
                irCode.setEntryCode();
            }
        }
    }

    private static void realText() {
        RegisterManager.getInstance().init();
        writeln(".text");
        addIndent();
        writeln("jal main_function");
        writeln("li $v0, 10");
        writeln("syscall");
        for (IRCode irCode : IRSupporter.getInstance().getIrCodes()) {
            if (irCode.isEntryCode()) {
                RegisterManager.getInstance().clear();
//                System.out.println(irCode);
            }
            if (irCode instanceof Declaration && ((Declaration) irCode).isGlobal()) {
                continue;
            }
            if (irCode instanceof BinaryAssign) {
                BinaryAssign binaryAssign = (BinaryAssign) irCode;
                if (binaryAssign.getRight(0).getType() == ValueType.Imm) {
                    int value = binaryAssign.getRight(0).getValue();
                    String right = RegisterManager.getInstance().load(binaryAssign.getRight(1));
                    RegisterManager.getInstance().protect(right);
                    String left = RegisterManager.getInstance().allocate(binaryAssign.getLeft());
                    switch (binaryAssign.getOperator()) {
                        case PLUS:
                            Mips.writeln(String.format("addu %s, %s, %d", left, right, value));
                            break;
                        case MINUS:
                            Mips.writeln(String.format("subu %s, %s, %d", left, right, value));
                            Mips.writeln(String.format("neg %s, %s", left, left));
                            break;
                        case MULTI:
                            Mips.writeln(String.format("mul %s, %s, %d", left, right, value));
                            break;
                        case DIV:
                            Mips.writeln(String.format("li $t0, %d", value));
                            Mips.writeln(String.format("div %s, $t0, %s", left, right));
                            break;
                        case MOD:
                            Mips.writeln(String.format("li $t0, %d", value));
                            Mips.writeln("div $t0," + right);
                            Mips.writeln("mfhi " + left);
                            break;
                        case EQL:
                            Mips.writeln(String.format("seq %s, %s, %d", left, right, value));
                            break;

                        case NEQ:
                            Mips.writeln(String.format("sne %s, %s, %d", left, right, value));
                            break;

                        case GRE:
                            Mips.writeln(String.format("li $t0, %d", value));
                            Mips.writeln(String.format("sgt %s, %s, %s", left, "$t0", right));
                            break;

                        case LSS:
                            Mips.writeln(String.format("sgt %s, %s, %d", left, right, value));
                            break;

                        case LEQ:
                            Mips.writeln(String.format("sge %s, %s, %d", left, right, value));
                            break;

                        case GEQ:
                            Mips.writeln(String.format("sle %s, %s, %d", left, right, value));
                            break;
                        default:
                    }
                    RegisterManager.getInstance().unprotect(right);
                } else if (binaryAssign.getRight(1).getType() == ValueType.Imm) {
                    int value = binaryAssign.getRight(1).getValue();
                    String right = RegisterManager.getInstance().load(binaryAssign.getRight(0));
                    RegisterManager.getInstance().protect(right);
                    String left = RegisterManager.getInstance().allocate(binaryAssign.getLeft());
                    switch (binaryAssign.getOperator()) {
                        case PLUS:
                            Mips.writeln(String.format("addu %s, %s, %d", left, right, value));
                            break;
                        case MINUS:
                            Mips.writeln(String.format("subu %s, %s, %d", left, right, value));
                            break;

                        case MULTI:
                            Mips.writeln(String.format("mul %s, %s, %d", left, right, value));
                            break;
                        case DIV:
                            Mips.writeln(String.format("move %s, %s", left, right));
                            DivImprove.getInstance().optimize(left, "$t0", value);
                            break;

                        case MOD:
                            Mips.writeln(String.format("move %s, %s", left, right));
                            DivImprove.getInstance().optimize(left, "$t0", value);
                            Mips.writeln(String.format("mul %s, %s, %d", left, left, value));
                            Mips.writeln(String.format("subu %s, %s, %s", left, right, left));
                            break;

                        case EQL:
                            Mips.writeln(String.format("seq %s, %s, %d", left, right, value));
                            break;

                        case NEQ:
                            Mips.writeln(String.format("sne %s, %s, %d", left, right, value));
                            break;

                        case GRE:
                            Mips.writeln(String.format("sgt %s, %s, %d", left, right, value));
                            break;

                        case LSS:
                            Mips.writeln(String.format("li $t0, %d", value));
                            Mips.writeln(String.format("sgt %s, %s, %s", left, "$t0", right));
                            break;

                        case LEQ:
                            Mips.writeln(String.format("sle %s, %s, %d", left, right, value));
                            break;

                        case GEQ:
                            Mips.writeln(String.format("sge %s, %s, %d", left, right, value));
                            break;
                        default:
                    }
                    RegisterManager.getInstance().unprotect(right);
                } else {
                    String r1 = RegisterManager.getInstance().load(binaryAssign.getRight(0));
                    RegisterManager.getInstance().protect(r1);
                    String r2 = RegisterManager.getInstance().load(binaryAssign.getRight(1));
                    RegisterManager.getInstance().protect(r2);
                    String left = RegisterManager.getInstance().allocate(binaryAssign.getLeft());
                    switch (binaryAssign.getOperator()) {
                        case PLUS:
                            Mips.writeln(String.format("addu %s, %s, %s", left, r1, r2));
                            break;
                        case MINUS:
                            Mips.writeln(String.format("subu %s, %s, %s", left, r1, r2));
                            break;

                        case MULTI:
                            Mips.writeln(String.format("mul %s, %s, %s", left, r1, r2));
                            break;

                        case DIV:
                            Mips.writeln(String.format("div %s, %s, %s", left, r1, r2));
                            break;

                        case MOD:
                            Mips.writeln(String.format("div %s, %s", r1, r2));
                            Mips.writeln(String.format("mfhi %s", left));
                            break;

                        case EQL:
                            Mips.writeln(String.format("seq %s, %s, %s", left, r1, r2));
                            break;

                        case NEQ:
                            Mips.writeln(String.format("sne %s, %s, %s", left, r1, r2));
                            break;

                        case GRE:
                            Mips.writeln(String.format("sgt %s, %s, %s", left, r1, r2));
                            break;

                        case LSS:
                            Mips.writeln(String.format("sgt %s, %s, %s", left, r2, r1));
                            break;

                        case LEQ:
                            Mips.writeln(String.format("sle %s, %s, %s", left, r1, r2));
                            break;

                        case GEQ:
                            Mips.writeln(String.format("sge %s, %s, %s", left, r1, r2));
                            break;
                        default:
                    }
                    RegisterManager.getInstance().unprotect(r1);
                    RegisterManager.getInstance().unprotect(r2);
                }
            } else if (irCode instanceof Branch) {
                Branch branch = (Branch) irCode;
                String register = RegisterManager.getInstance().load(branch.getCondition());
                RegisterManager.getInstance().clear();
                if (branch.isZeroBranch()) {
                    Mips.writeln(String.format("beqz %s, %s", register, branch.getLabel()));
                } else {
                    Mips.writeln(String.format("bnez %s, %s", register, branch.getLabel()));
                }
            } else if (irCode instanceof FuncCall) {
                FuncCall funccall = ((FuncCall) irCode);
                int index = -8;
                for (Value value : funccall.getParams()) {
                    String register = RegisterManager.getInstance().load(value);
                    Mips.writeln(String.format("sw %s, %d($sp)", register, index));
                    index -= 4;
                }
                RegisterManager.getInstance().clear();
                Mips.writeln(String.format("jal %s", funccall.getName()));
                if (funccall.getResult() != null)
                    RegisterManager.getInstance().allocate(funccall.getResult(), "$v0");
            } else if (irCode instanceof FuncEntry) {
                FuncEntry funcEntry = (FuncEntry) irCode;
                Mips.writeln(String.format("%s:", funcEntry.getName()));
                Mips.writeln(String.format("addu $sp,$sp,-%d", funcEntry.getSize()));
                Mips.writeln(String.format("sw $ra,%d($sp)", funcEntry.getSize() - 4));
                RegisterManager.getInstance().clear();
            } else if (irCode instanceof GetInt) {
                GetInt getInt = (GetInt) irCode;
                Mips.writeln("li $v0, 5");
                Mips.writeln("syscall");
                RegisterManager.getInstance().allocate(getInt.getValue(), "$v0");
            } else if (irCode instanceof Jump) {
                RegisterManager.getInstance().clear();
                Jump jump = (Jump) irCode;
                Mips.writeln(String.format("j %s", jump.getLabel()));
            } else if (irCode instanceof Label) {
                Label label = (Label) irCode;
                Mips.writeln(String.format("%s:", label.getLabel()));
            } else if (irCode instanceof PrintNumber) {
                PrintNumber printNumber = (PrintNumber) irCode;
                RegisterManager.getInstance().load(printNumber.getValue(), "$a0");
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
                    RegisterManager.getInstance().load(return1.getValue(), "$v0");
                }
                RegisterManager.getInstance().clear();
                Mips.writeln(String.format("lw $ra,%d($sp)", return1.getSize() - 4));
                Mips.writeln(String.format("addu $sp,$sp,%d", return1.getSize()));
                Mips.writeln("jr $ra");
            } else if (irCode instanceof UnaryAssign) {
                UnaryAssign unaryAssign = (UnaryAssign) irCode;
                String right = RegisterManager.getInstance().load(unaryAssign.getRight());
                RegisterManager.getInstance().protect(right);
                String left = RegisterManager.getInstance().allocate(unaryAssign.getLeft());
                switch (unaryAssign.getOperator()) {
                    case MINUS:
                        Mips.writeln(String.format("neg %s, %s", left, right));
                        break;
                    case NOT:
                        Mips.writeln(String.format("seq %s, %s, $zero", left, right));
                        break;
                    default:
                        Mips.writeln(String.format("move %s,%s", left, right));
                }
                RegisterManager.getInstance().unprotect(right);
//                if(unaryAssign.getLeft().getType()==ValueType.Array){
//                    SymbolManager.getInstance().store(unaryAssign.getLeft(),left);
//                    RegisterManager.getInstance().free(left);
//                }
            }
        }
    }

    public static void writeln(String string) {
        for (int i = 0; i < indent; i++)
            MipsWriter.write(" ");
        MipsWriter.writeln(string);
    }
}
