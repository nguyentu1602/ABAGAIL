package opt.test;

import opt.EvaluationFunction;
import opt.example.RastriginEvaluationFunction;
import shared.Instance;
import util.linalg.DenseVector;

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

        double[] optim = new double[N];
        Arrays.fill(optim, 0);

        Instance inst = Instance( optim );

        System.out.println(ef.value(rhc.getOptimal()));

        System.out.println(ef.value(mimic.getOptimal()));
    }

}
