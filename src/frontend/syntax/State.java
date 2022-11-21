package frontend.syntax;

public class State {
    private final int loopNumber;
    private final int ifNumber;
    private final boolean haveElse;
    private final boolean shouldReturnValue;

    public State(int loopNumber, int ifNumber, boolean haveElse, boolean shouldReturnValue) {
        this.loopNumber = loopNumber;
        this.ifNumber = ifNumber;
        this.haveElse = haveElse;
        this.shouldReturnValue=shouldReturnValue;
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
}
