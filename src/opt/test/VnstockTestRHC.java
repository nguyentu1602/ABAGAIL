package opt.test;

import func.nn.backprop.BackPropagationNetwork;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;

public class VnstockTestRHC extends VnstockTestCommon {
  public static void main(String[] args) {
    oaNames = new String[] {"RHC"};
    outfile = "RHC_100000.txt";

    prepare_experiment(oaNames, 100000, 2000, outfile);

    oa[0] = new RandomizedHillClimbing(nnop[0]);

    for(int i = 0; i < oa.length; i++){
      OptimizationAlgorithm oa_cur = oa[i];
      BackPropagationNetwork network_cur = networks[i];
      String oaName_cur = oaNames[i];
      run_experiment(oa_cur, network_cur, oaName_cur);
    }
  }
}