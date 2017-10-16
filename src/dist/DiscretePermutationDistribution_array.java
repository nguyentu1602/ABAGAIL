package dist;

import shared.DataSet;
import shared.Instance;
import util.ABAGAILArrays;
import java.util.Random;

/**
 * A distribution of all of the permutations
 * of a set size.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */


public class DiscretePermutationDistribution_array extends AbstractDistribution {
    /**
     * The size of the data
     */
    private int[] n;

    /**
     * The probability
     */
    private double p;

    /**
     * Make a new discrete permutation distribution
     * @param n the size of the data
     */
    public DiscretePermutationDistribution_array(int[] n) {
        this.n = n;
        p = n[0];
        for (int i = 1; i < n.length; i++) {
            p *= n[i];
        }
        System.out.println("length of array for distribution "+n.length);
        p = 1 / p;
    }

    /**
     * @see Distribution#probabilityOf(Instance)
     */
    public double p(Instance i) {
        return p;
    }

    /**
     * @see Distribution#generateRandom(Instance)
     */
    public Instance sample(Instance valid) {
        double[] d=new double[n.length];
        Random rnd = new Random();
        if(valid!=null) {
            d = new double[valid.getData().size()];
            for(int i=0;i<valid.getData().size();i++) {
                d[i]  = valid.getData().get(i);
            }
        }else{
            for(int i=0;i<d.length;i++)
                d[i]=rnd.nextInt(n[0]);
        }
        ABAGAILArrays.permute(d);
        return new Instance(d);
    }

    /**
     * @see Distribution#generateMostLikely(Instance)
     */
    public Instance mode(Instance ignored) {
        return sample(ignored);
    }

    /**
     * @see Distribution#estimate(DataSet)
     */
    public void estimate(DataSet observations) {
        return;
    }
}