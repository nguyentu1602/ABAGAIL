package opt.test;

import func.nn.backprop.BackPropagationNetwork;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;

public class VnstockTestSA extends VnstockTestCommon {
  public static void main(String[] args) {
    oaNames = new String[] {"SA"};
    outfile = "SA_100000_1E11_0.992.txt";
    // running for 100000 gets test acc of 69%

    prepare_experiment(oaNames, 100000, 2000, outfile);

    oa[0] = new SimulatedAnnealing(1E11, .992, nnop[0]);

    for(int i = 0; i < oa.length; i++){
      OptimizationAlgorithm oa_cur = oa[i];
      BackPropagationNetwork network_cur = networks[i];
      String oaName_cur = oaNames[i];
      run_experiment(oa_cur, network_cur, oaName_cur);
    }
  }
}