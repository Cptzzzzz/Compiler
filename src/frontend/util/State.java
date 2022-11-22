package frontend.util;

public class State {
    private final int loopNumber;
    private final int ifNumber;
    private final boolean haveElse;
    private final boolean shouldReturnValue;
    private final int blockNumber;

    public State() {
        loopNumber = 0;
        ifNumber = 0;
        haveElse = false;
        shouldReturnValue = false;
        blockNumber = 0;
    }

    public State(int loopNumber, int ifNumber, boolean haveElse, boolean shouldReturnValue, int blockNumber) {
        this.loopNumber = loopNumber;
        this.ifNumber = ifNumber;
        this.haveElse = haveElse;
        this.shouldReturnValue = shouldReturnValue;
        this.blockNumber = blockNumber;
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
}
