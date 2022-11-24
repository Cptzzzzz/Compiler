package frontend.util;

public class State {
    private final int loopNumber;
    private final int ifNumber;
    private final boolean haveElse;
    private final boolean shouldReturnValue;
    private int blockNumber;
    private final int lAndNumber;
    private final int lOrNumber;

    private int symbolNumber;

    public void setSymbolNumber(int symbolNumber) {
        this.symbolNumber = symbolNumber;
    }

    public int getSymbolNumber() {
        return symbolNumber;
    }

    public State() {
        loopNumber = 0;
        ifNumber = 0;
        haveElse = false;
        shouldReturnValue = false;
        blockNumber = 0;
        lAndNumber = 0;
        lOrNumber = 0;
    }

    public State(int loopNumber, int ifNumber, boolean haveElse, boolean shouldReturnValue, int blockNumber,
                 int lAndNumber, int lOrNumber) {
        this.loopNumber = loopNumber;
        this.ifNumber = ifNumber;
        this.haveElse = haveElse;
        this.shouldReturnValue = shouldReturnValue;
        this.blockNumber = blockNumber;
        this.lAndNumber = lAndNumber;
        this.lOrNumber = lOrNumber;
    }

    public boolean shouldReturnValue() {
        return shouldReturnValue;
    }

    public int getLoopNumber() {
        return loopNumber;
    }

    public int getIfNumber() {
        return ifNumber;
    }

    public boolean isHaveElse() {
        return haveElse;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public int getLAndNumber() {
        return lAndNumber;
    }

    public int getLOrNumber() {
        return lOrNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }
}
