package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.KnapsackEvaluationFunction;
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
import java.util.Random;

/**
 * A test of the knapsack problem
 *
 * Given a set of items, each with a weight and a value, determine the number of each item to include in a
 * collection so that the total weight is less than or equal to a given limit and the total value is as
 * large as possible.
 * https://en.wikipedia.org/wiki/Knapsack_problem
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KnapsackTest {
    /** Random number generator */
    private static final Random random = new Random();
    /** The number of items */
    private static final int NUM_ITEMS = 64;
    /** The number of copies each */
    private static final int COPIES_EACH = 3;
    /** The maximum value for a single element */
    private static final double MAX_VALUE = 50;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum weight for the knapsack */
    private static final double MAX_KNAPSACK_WEIGHT =
         MAX_WEIGHT * NUM_ITEMS * COPIES_EACH * .4;

    public static String outfile   = "knapsach_results.txt";
    public static PrintWriter outwrt;


    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {


        int[] copies = new int[NUM_ITEMS];
        Arrays.fill(copies, COPIES_EACH);
        double[] values = new double[NUM_ITEMS];
        double[] weights = new double[NUM_ITEMS];


        try {
            outwrt = new PrintWriter(outfile, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DecimalFormat dff  = new DecimalFormat("0.000");


        double num_experiments = 8;

        for (int trial = 0; trial < num_experiments; trial++) {

            for (int i = 0; i < NUM_ITEMS; i++) {
                values[i] = random.nextDouble() * MAX_VALUE;
                weights[i] = random.nextDouble() * MAX_WEIGHT;
            }
            int[] ranges = new int[NUM_ITEMS];
            Arrays.fill(ranges, COPIES_EACH + 1);

            EvaluationFunction ef = new KnapsackEvaluationFunction(values, weights, MAX_KNAPSACK_WEIGHT, copies);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
            Distribution df = new DiscreteDependencyTree(.5, ranges);

            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);


            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .995, hcp);
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 20, gap);
            MIMIC mimic = new MIMIC(200, 20, pop);

            FixedIterationTrainer rhc_fit = new FixedIterationTrainer(rhc, 1000);
            FixedIterationTrainer sa_fit = new FixedIterationTrainer(sa, 1000);
            FixedIterationTrainer ga_fit = new FixedIterationTrainer(ga, 5);
            FixedIterationTrainer mimic_fit = new FixedIterationTrainer(mimic, 5);

            double num_snap = 1000;

            for (int i = 1; i <= num_snap; i++) {
                rhc_fit.train();
                sa_fit.train();
                ga_fit.train();
                mimic_fit.train();

                double rhc_sc = ef.value(rhc.getOptimal());
                double sa_sc  = ef.value(sa.getOptimal());
                double ga_sc  = ef.value(ga.getOptimal());
                double mimic_sc = ef.value(mimic.getOptimal());

                String joinedString = i + "," +  dff.format(rhc_sc) + "," +  dff.format(sa_sc) + "," +
                    dff.format(ga_sc) + "," +  dff.format(mimic_sc) + ","  +  trial;

                outwrt.println(joinedString);
                //        outwrt.flush();
                System.out.println( joinedString );
            }
        }

    }
}
