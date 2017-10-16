package opt.example;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

import java.math.BigInteger;
import java.util.BitSet;



/**
 * A Rastrigin function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RastriginEvaluationFunction implements EvaluationFunction {
    /**
     * The constant A
     */
    private double A = 1000.0;

    public RastriginEvaluationFunction() {}

    /**
     * Make a new Rastrigin function
     * @param A the t value
     */
    public RastriginEvaluationFunction(double A) {
        this.A = A;
    }

    private static int bitSetToInt(BitSet bitSet)
    {
        int bitInteger = 0;
        for(int i = 0 ; i < 32; i++)
            if(bitSet.get(i))
                bitInteger |= (1 << i);
        return bitInteger;
    }

    /**
     * turn the bitchar to an int
     * turn the int to a float
     * https://stackoverflow.com/questions/5157664/java-how-to-convert-a-string-of-binary-values-to-a-float-and-vice-versa
     *
     * Make sure to use BigInteger
     */
    private float convert32BitVecToFloat(Vector input) {
        char[] bitChars = new char[input.size()];
        for(int i = 0; i < input.size(); i++) {
            if(input.get(i) != 0.0) {
                bitChars[i] = '1';
            } else {
                bitChars[i] = '0';
            }
        }

//        int bitInt = bitSetToInt(bits); // turn the bitset to int
//        System.out.println(bitInt);
//        System.out.println(String.valueOf(bitChars));
//
//        return Float.intBitsToFloat(bitInt); // turn the int partern to float

        int intBits = new BigInteger(String.valueOf(bitChars), 2).intValue();
        return Float.intBitsToFloat(intBits);
    }

    /**
     * @see opt.EvaluationFunction # value(opt.OptimizationData)
     */

    public double value(Instance d) {
        Vector data = d.getData();
        /* data is a vector of 64 bits, I need to cut it into 2, then turn them
            into 2 floats and feed to the real formula
        */

        Vector first_half  = data.get(0,  32);
        Vector second_half = data.get(32, 64);

        float x = convert32BitVecToFloat(first_half)  - 0.15625f;
        float y = convert32BitVecToFloat(second_half) - 0.15625f;
//        double y2 = Math.pow(y, 2);
//        double x2 = Math.pow(x, 2);

        return -Math.pow(x,2) + A*Math.cos(x*2*Math.PI) - Math.pow(y,2) + A*Math.cos(y*2*Math.PI);
//        return - (0.5 + (Math.pow(Math.sin(x2 - y2),2) - 0.5)/(1 + 0.001*(x2 + y2))/(1 + 0.001*(x2 + y2)))*10000;
    }
}
