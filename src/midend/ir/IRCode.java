package midend.ir;

public class IRCode {
    private boolean entryCode = false;
    private int blockNumber = 0;

    public void setEntryCode() {
        entryCode = true;
    }
    public void setEntryCode(boolean entryCode) {
        this.entryCode = entryCode;
    }

    public boolean isEntryCode() {
        return entryCode;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getBlockNumber() {
        return blockNumber;
    }
}
