package opt.test;

import opt.EvaluationFunction;
import opt.example.RastriginEvaluationFunction;
import shared.Instance;

import java.util.Arrays;

/**
 * A test using the Rastrigin evaluation function
 * @author cuong nguyen
 * @version 1.0
 */

public class RastriginTest {
    /** The n value */
    private static final int N = 64;

    public static void main(String[] args) {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);

        EvaluationFunction ef = new RastriginEvaluationFunction();

        double[] optim_arr = new double[N];
        Arrays.fill(optim_arr, 0);

        // Instance optim = new Instance( optim_arr );
       // System.out.println(ef.value(optim));

        // Test case: -1, -1
        String firstStr  = Integer.toBinaryString(Float.floatToRawIntBits(-1.0f));
        String secondStr = Integer.toBinaryString(Float.floatToRawIntBits(-1.0f));
        String fullBitStr = firstStr + secondStr;

        assert fullBitStr.length() == N;

        String[] bitStrArr = fullBitStr.split("");
        for(int i = 0; i < bitStrArr.length; i++){
            optim_arr[i] = Double.parseDouble(bitStrArr[i]);
        }

        System.out.println(ef.value(new Instance( optim_arr )));

        // Test case: 5, 3  - need to patch the zero infront for the missing sign bit
        String firstStr1  = "0" + Integer.toBinaryString(Float.floatToRawIntBits(5.0f));
        String secondStr1 = "0" + Integer.toBinaryString(Float.floatToRawIntBits(3.0f));
        String fullBitStr1 = firstStr1 + secondStr1;

        assert fullBitStr1.length() == N;

        String[] bitStrArr1 = fullBitStr1.split("");
        for(int i = 0; i < bitStrArr1.length; i++){
            optim_arr[i] = Double.parseDouble(bitStrArr1[i]);

        }

       System.out.println(ef.value(new Instance( optim_arr )));
    }
}
