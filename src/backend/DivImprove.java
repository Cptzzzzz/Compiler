package backend;

public class DivImprove {

public static void main(String[]args){

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
        long ml = 1L << (l + 32);
        long mh = ((1L << (l + 32)) + 1L << (1 + l)) / temp;
        while ((ml / 2) < (mh / 2) && sh > 0) {
            ml /= 2;
            mh /= 2;
            sh--;
        }
        System.out.println(mh+" "+sh+" "+l);
    }


}
