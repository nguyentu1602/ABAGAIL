package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.RastriginEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * A test using the Rastrigin evaluation function
 * @author cuong nguyen
 * @version 1.0
 */

public class RastriginTest {
    /**
     * The n value
     */
    private static final int N = 64;

    public static String outfile = "rastrigin_results.txt";
    public static PrintWriter outwrt;


    public static void main(String[] args) {

        try {
            outwrt = new PrintWriter(outfile, "UTF-8");
        } catch (FileNotFoundException|UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DecimalFormat dff = new DecimalFormat("0.000");

        double num_experiments = 8;

        for (int trial = 0; trial < num_experiments; trial++) {
            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);

            EvaluationFunction ef = new RastriginEvaluationFunction();

            double[] optim_arr = new double[N];
            Arrays.fill(optim_arr, 0);

            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
            Distribution df = new DiscreteDependencyTree(.00001, ranges);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);


            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            SimulatedAnnealing sa = new SimulatedAnnealing(1E10, .9995, hcp);
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(100, 20, 10, gap);
            MIMIC mimic = new MIMIC(200, 50, pop);


            FixedIterationTrainer rhc_fit = new FixedIterationTrainer(rhc, 1000);
            FixedIterationTrainer sa_fit = new FixedIterationTrainer(sa, 1000);
            FixedIterationTrainer ga_fit = new FixedIterationTrainer(ga, 5);
            FixedIterationTrainer mimic_fit = new FixedIterationTrainer(mimic, 5);

            double num_snap = 1000;

            for (int i = 1; i <= num_snap; i++) {
                rhc_fit.train();
                sa_fit.train();

                double mimic_sc = 0;
                try {
                    ga_fit.train();
                    mimic_fit.train();
                    mimic_sc = ef.value(mimic.getOptimal());

                } catch (NullPointerException e) {
                    // do nothing
                }

                double rhc_sc = ef.value(rhc.getOptimal());
                double sa_sc = ef.value(sa.getOptimal());
                double ga_sc = ef.value(ga.getOptimal());

                if (ga_sc < 0.0 || Double.isNaN(ga_sc)) ga_sc = 0;

                String joinedString = i + "," + dff.format(rhc_sc) + "," + dff.format(sa_sc) + "," +
                    ga_sc + "," + dff.format(mimic_sc) + "," + trial;

                outwrt.println(joinedString);
                System.out.println(joinedString);
            }
        }
    }

        // The below block test the function implementation
//        // Instance optim = new Instance( optim_arr );
//       // System.out.println(ef.value(optim));
//
//        // Test case: -1, -1
//        String firstStr  = Integer.toBinaryString(Float.floatToRawIntBits(-1.0f));
//        String secondStr = Integer.toBinaryString(Float.floatToRawIntBits(-1.0f));
//        String fullBitStr = firstStr + secondStr;
//
//        assert fullBitStr.length() == N;
//
//        String[] bitStrArr = fullBitStr.split("");
//        for(int i = 0; i < bitStrArr.length; i++){
//            optim_arr[i] = Double.parseDouble(bitStrArr[i]);
//        }
//
//        System.out.println(ef.value(new Instance( optim_arr )));
//
//        // Test case: 5, 3  - need to patch the zero infront for the missing sign bit
//        String firstStr1  = "00" + Integer.toBinaryString(Float.floatToRawIntBits(.15625f));
//        System.out.println(firstStr1);
//        System.out.println(sa.getOptimal());

        //        String secondStr1 = "0" + Integer.toBinaryString(Float.floatToRawIntBits(3.0f));
//        String fullBitStr1 = firstStr1 + secondStr1;
//
//        assert fullBitStr1.length() == N;
//
//        String[] bitStrArr1 = fullBitStr1.split("");
//        for(int i = 0; i < bitStrArr1.length; i++){
//            optim_arr[i] = Double.parseDouble(bitStrArr1[i]);
//
//        }
//        System.out.println(ef.value(new Instance( optim_arr )));
}
