package backend;

public class DivImprove {

    public static void main(String[] args) {
        generate("1", 3);
    }

    public static void generate(String register, int n) {
        //choose multiplier
        int l, sh;

        int temp = n < 0 ? -n : n;
        boolean flag = false;
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
        long ml = (1L << (l + 32)) / temp;
        long mh = ((1L << (l + 32)) + (1L << (1 + l))) / temp;
        while ((ml / 2) < (mh / 2) && sh > 0) {
            ml /= 2;
            mh /= 2;
            sh--;
        }
//        System.out.println(mh + " " + sh + " " + l);
        if (n == 1) {
            n = n + 3 - 2 - 1;
        } else if (!flag) {
            Mips.writeln("sra $t4,$t3," + (l - 1));
            Mips.writeln("srl $t4,$t4," + (32 - l));
            Mips.writeln("addu $t3,$t3,$t4");
            Mips.writeln("sra $t3,$t3," + l);
        } else if (mh < (1L << 31)) {
            Mips.writeln("sra $t4,$t3,31");
            Mips.writeln("mul $t3,$t3," + mh);
            Mips.writeln("mfhi $t3");
            Mips.writeln("sra $t3,$t3," + sh);
            Mips.writeln("subu $t3,$t3,$t4");
        } else {
            Mips.writeln("mul $t4,$t3," + (ml - (1L << 32)));//MULSH(m-2^N,n)
            Mips.writeln("mfhi $t4");//$4=MULSH(m-2^N,n)
            Mips.writeln("addu $t4,$t4,$t3");//n+MULSH(m-2^N)
            Mips.writeln("sra $t4,$t4," + sh);//SRA(n+MULSH(m-2^N),sh)
            Mips.writeln("sra $t3,$t3,31");
            Mips.writeln("subu $t3,$t4,$t3");
        }
        if (n < 0) {
            Mips.writeln("neg $t3,$t3");
        }
    }


}
