package backend;

import java.math.BigInteger;

public class DivImprove {
    private DivImprove() {
    }

    private static DivImprove divImprove;

    public static DivImprove getInstance() {
        if (divImprove == null) divImprove = new DivImprove();
        return divImprove;
    }

    private long m;
    private int sh, l, n;
    private boolean flag;

    public void optimize(String reg, String temp, int n) {
        generate(n);
        solve(reg, temp);
    }

    private void generate(int n) {
        this.n = n;
        int temp = n < 0 ? -n : n;
        flag = false;
        int low = -1;
        for (int i = 0; i < 31; i++) {
            if ((temp & (1 << i)) > 0) {
                if (low != -1) {
                    flag = true;
                }
                low = i;
            }
        }
        if (flag) {
            l = low + 1;
        } else {
            l = low;
        }
        sh = l;
        BigInteger mLow = BigInteger.valueOf(1).shiftLeft(l + 32).divide(BigInteger.valueOf(temp));
        BigInteger mHigh = BigInteger.valueOf(1).shiftLeft(l + 32).add(BigInteger.valueOf(1).shiftLeft(1 + l)).divide(BigInteger.valueOf(temp));
        while ((mLow.divide(BigInteger.valueOf(2))).compareTo(mHigh.divide(BigInteger.valueOf(2))) < 0 && sh > 0) {
            mLow = mLow.divide(BigInteger.valueOf(2));
            mHigh = mHigh.divide(BigInteger.valueOf(2));
            sh--;
        }
        m = mHigh.longValue();
    }

    private void solve(String src, String temp) {
        if (n != 1) {
            if (!flag) {
                Mips.writeln(String.format("sra %s,%s,%d", temp, src, l - 1));
                Mips.writeln(String.format("srl %s,%s,%d", temp, temp, 32 - l));
                Mips.writeln(String.format("addu %s,%s,%s", src, src, temp));
                Mips.writeln(String.format("sra %s,%s,%d", src, src, l));
            } else if (m < (1L << 31)) {
                Mips.writeln("sra $t4,$t3,31");
                Mips.writeln(String.format("sra %s,%s,%d", temp, src, 31));
                Mips.writeln(String.format("mul %s,%s,%d", src, src, m));
                Mips.writeln(String.format("mfhi %s", src));
                Mips.writeln(String.format("sra %s,%s,%d", src, src, sh));
                Mips.writeln(String.format("subu %s,%s,%s", src, src, temp));
            } else {
                Mips.writeln(String.format("mul %s,%s,%d", temp, src, m - (1L << 32)));
                Mips.writeln(String.format("mfhi %s", temp));
                Mips.writeln(String.format("addu %s,%s,%s", temp, temp, src));
                Mips.writeln(String.format("sra %s,%s,%d", temp, temp, sh));
                Mips.writeln(String.format("sra %s,%s,%d", src, src, 31));
                Mips.writeln(String.format("subu %s,%s,%s", src, temp, src));
            }
        }
        if (n < 0) {
            Mips.writeln(String.format("neg %s,%s", src, src));
        }
    }
}
