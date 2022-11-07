package intermediate;

import backend.Mips;
import syntax.Variable;
import syntax.VariableTable;

import java.util.ArrayList;

public class SymbolTable {
    ArrayList<Symbol> symbols;

    //局部变量表的构造方式
    public SymbolTable(VariableTable variableTable, int paramNumber) {
        symbols = new ArrayList<>();
        ArrayList<Variable> variables = variableTable.getVariables();
        int length = variables.size();
        for (int i = 0; i < length; i++) {
            symbols.add(new Symbol(variables.get(i), i < paramNumber));
        }
    }

    public SymbolTable() {
        symbols = new ArrayList<>();
    }

    public void add(Symbol symbol) {
        symbols.add(symbol);
    }

    public int getSpace() {
        int res = 0;
        for (Symbol symbol : symbols) {
            res += symbol.getSpace();
        }
        return res;
    }

    /**
     * @param name      变量名
     * @param isAddress 是否为取址操作
     * @param dst       值的目标寄存器
     * @param src       如果是数组，数组内的偏移量
     */
    public void loadGlobalValue(String name, boolean isAddress, String dst, String src) {
        Mips.writeln(String.format("la $t7,%s", name));
        if (isAddress) {
            Mips.writeln(String.format("add %s,$t7,%s", dst, src));
        } else {
            if (!src.equals("$zero"))
                Mips.writeln(String.format("add $t7,$t7,%s", src));
            Mips.writeln(String.format("lw %s,0($t7)", dst));
        }
    }

    /*
    case 1: get variable value 直接把值lw到寄存器内
    case 2: get array[1] value declaration 利用sp内offset+数组下标 直接lw到寄存器内
    case 3: get array[1] location declaration 返回sp+sp内offset
    case 4: get array[2] value declaration 利用sp内offset+数组下标 lw到寄存器内
    case 4: get array[2] location declaration 利用sp+sp内offset+二维数组偏移
    case 2: get array[1] value reference lw地址，地址+offset,lw值到寄存器
    case 3: get array[1] location reference lw到寄存器即可
    case 4: get array[2] value reference lw地址，根据地址+数组内偏移找到存储位置，然后lw
    case 4: get array[2] location reference lw地址，根据数组内偏移在地址上加
     */

    /**
     * @param name      变量名
     * @param isAddress 是否为取址操作
     * @param dst       值的目标寄存器
     * @param src       如果是数组，数组内的偏移量
     */
    public void loadLocalValue(String name, boolean isAddress, String dst, String src) {
        int offset = getSpace();
        for (Symbol symbol : symbols) {
            if (symbol.getName().equals(name)) {
                if (symbol.isVariable())
                    Mips.writeln(String.format("lw %s,%d($sp)", dst, offset));
                else {
                    offset -= symbol.getSpace();
                    offset += 4;
                    if (symbol.isReference()) {
                        if (isAddress) {
                            Mips.writeln(String.format("lw %s,%d($sp)", dst, offset));
                            Mips.writeln(String.format("add %s,%s,%s", dst, dst, src));
                        } else {
                            Mips.writeln(String.format("lw $t7,%d($sp)", offset));
                            Mips.writeln(String.format("add $t7,$t7,%s", src));
                            Mips.writeln(String.format("lw %s,0($t7)", dst));
                        }
                    } else {
                        if (isAddress) {
                            Mips.writeln(String.format("addi %s,$sp,%d", dst, offset));
                            Mips.writeln(String.format("add %s,%s,%s", dst, dst, src));
                        } else {
                            Mips.writeln(String.format("add $t7,$sp,%s", src));
                            Mips.writeln(String.format("lw %s,%d($t7)", dst, offset));
                        }
                    }
                }
                return;
            } else {
                offset -= symbol.getSpace();
            }
        }
    }

    /**
     * 把src寄存器的值存在变量name的存储空间内，offset为数组下标的偏移量，如果不是数组则调用$zero寄存器
     *
     * @param name   变量名
     * @param src    存放变量值的寄存器
     * @param offset 数组的偏移量
     */
    public void storeLocalValue(String name, String src, String offset) {
        int off = getSpace();
        for (Symbol symbol : symbols) {
            if (symbol.getName().equals(name)) {
                if (!symbol.isVariable()) {
                    off -= symbol.getSpace();
                    off += 4;
                }
                if (symbol.isVariable()) {
                    Mips.writeln(String.format("sw %s,%d($sp)", src, off));
                } else {
                    if (symbol.isReference()) {
                        Mips.writeln(String.format("lw $t7,%d($sp)", off));
                        Mips.writeln(String.format("add $t7,$t7,%s", offset));
                        Mips.writeln(String.format("sw %s,0($t7)", src));
                    } else {
                        Mips.writeln(String.format("add $t7,$sp,%s", offset));
                        Mips.writeln(String.format("sw %s,%d($t7)", src, off));
                    }
                }
//                if (offset.equals("$zero")) {
//                    Mips.writeln(String.format("sw %s,%d($sp)", src, off));
//                } else {
//                    Mips.writeln(String.format("add $t7,$sp,%s", offset));
//                    Mips.writeln(String.format("sw %s,%d($t7)", src, off));
//                }
                return;
            } else {
                off -= symbol.getSpace();
            }
        }
    }

    public void storeGlobalValue(String name, String src, String offset) {
        Mips.writeln(String.format("la $t7,%s", name));
        Mips.writeln(String.format("add $t7,$t7,%s", offset));
        Mips.writeln(String.format("sw %s,0($t7)", src));
    }
}
