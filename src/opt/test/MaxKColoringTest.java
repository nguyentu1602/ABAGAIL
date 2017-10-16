package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution_array;
import dist.Distribution;
import opt.*;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

/**
 * 
 * @author kmandal
 * @version 1.0
 */
public class MaxKColoringTest {
    /** The n value */
    private static final int N = 70; // number of vertices
    private static final int L = 4;  // L adjacent nodes per vertex

    private static final int K = 5;  // K possible colors

    public static String outfile   = "results.txt";
    public static PrintWriter outwrt;



    public static void run_exp( int N, int L, int K){

        System.out.println("Begin experiment with {N, L, K}: {" + N + "," + L + "," +  K + "}");

        // create the random velocity
        Random random = new Random((long) N*L);
        Vertex[] vertices = new Vertex[N];
        for (int i = 0; i < N; i++) {
            vertices[i] = new Vertex();
        }
        for (int i = 0; i < N; i++) {
            vertices[i].setAdjMatrixSize(L);
            int j=0; int runAway=0;
            while(vertices[i].getAadjacencyColorMatrix().size() < L){
                int aNeighbor=random.nextInt(N);
                if(aNeighbor>i &&
                    !vertices[i].getAadjacencyColorMatrix().contains(aNeighbor)
                    && vertices[aNeighbor].getAadjacencyColorMatrix().size()<L) {
                    vertices[i].getAadjacencyColorMatrix().add(aNeighbor);
                    vertices[aNeighbor].getAadjacencyColorMatrix().add(i);
                    runAway=0;
                } else runAway++;
                if(runAway>1000) {
                    System.out.println("can't find solution");break;
                }
            }
        }

//        System.out.println("Adjacency graph");
//        for (int i = 0; i < N; i++) {
//            Vertex vertex = vertices[i];
//            System.out.println(i+" "+Arrays.toString(vertex.getAadjacencyColorMatrix().toArray()));
//        }

        System.out.printf("---------------");
        // for rhc, sa, and ga we use a permutation based encoding
        int[] ranges = new int[N];  // N nodes, each with K choices of colors
        Arrays.fill(ranges, K);
        MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
        Distribution odd = new DiscretePermutationDistribution_array(ranges);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        UniformCrossOver cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        long starttime = System.currentTimeMillis();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 500);
        fit.train();


        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));

        System.out.println("============================");

        starttime = System.currentTimeMillis();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E10, 0.99, hcp);
        fit = new FixedIterationTrainer(sa, 500);
        fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));

        System.out.println("============================");

        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 20, 120, gap);
        fit = new FixedIterationTrainer(ga, 500);
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));

        System.out.println("============================");

        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new FixedIterationTrainer(mimic, 1000);
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));

        System.out.println("Done experiment with {N, L, K}: {" + N + "," + L + "," +  K + "}");
    }


    /**
     * The test main
     * @param args ignored
     */

    public static void main(String[] args) {

        try {
            outwrt = new PrintWriter(outfile, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int[] Ns = {50, 60, 70};
        int[] Ks = {5,  7,  10};

        for(int n: Ns){
            for(int k: Ks) {
                int l = k - 1;
                run_exp(n, l, k);
            }
        }
    }
}
