package midend.util;

import midend.ir.IRCode;
import util.CompilerMode;
import util.IRWriter;

import java.util.ArrayList;

public class IRSupporter {
    private IRSupporter() {
    }

    private static IRSupporter irSupporter;
    private ArrayList<IRCode> irCodes = new ArrayList<>();

    public static IRSupporter getInstance() {
        if (irSupporter == null)
            reset();
        return irSupporter;
    }

    public static void reset() {
        irSupporter = new IRSupporter();
    }

    public void addIRCode(IRCode irCode) {
        irCodes.add(irCode);
    }

    public void output() {
        if (CompilerMode.getInstance().isIr())
            for (IRCode irCode : irCodes)
                IRWriter.writeln(irCode.toString());
    }

    public ArrayList<IRCode> getIrCodes() {
        return irCodes;
    }
}