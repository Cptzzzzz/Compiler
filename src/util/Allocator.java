package util;

import midend.ir.Declaration;
import midend.util.IRSupporter;
import midend.util.ValueType;
import midend.util.Value;

public class Allocator {
    private Allocator() {

    }

    private static Allocator allocator;

    public static Allocator getInstance() {
        if (allocator == null)
            allocator = new Allocator();
        return allocator;
    }

    private int blockNum = 0;

    public int getBlockNumber() {
        blockNum++;
        return blockNum;
    }

    private int whileNum = 0;

    public int getWhileNumber() {
        whileNum++;
        return whileNum;
    }

    private int ifNum = 0;

    public int getIfNumber() {
        ifNum++;
        return ifNum;
    }

    private int temp = 0;

    public Value getTemp() {
        temp++;
        Value value = new Value("t_" + temp);
        IRSupporter.getInstance().addIRCode(new Declaration(value.getName(), false, false, 4, false, ValueType.Variable, null));
        return value;
    }

    private int printStringNumber = 0;

    public int getPrintStringNumber() {
        printStringNumber++;
        return printStringNumber;
    }

    private int lAndNumber = 0;

    public int getLAndNumber() {
        lAndNumber++;
        return lAndNumber;
    }

    private int lOrNumber = 0;

    public int getLOrNumber() {
        lOrNumber++;
        return lOrNumber;
    }
}
