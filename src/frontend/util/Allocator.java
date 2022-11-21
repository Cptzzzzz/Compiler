package frontend.util;

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
}
