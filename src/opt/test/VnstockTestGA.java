package opt.test;

import func.nn.backprop.BackPropagationNetwork;
import opt.OptimizationAlgorithm;
import opt.ga.StandardGeneticAlgorithm;

public class VnstockTestGA extends VnstockTestCommon {
  public static void main(String[] args) {
    oaNames = new String[] {"GA"};

    outfile = "GA_500_200_100_20.txt";

    prepare_experiment(oaNames, 500, 10, outfile);

    oa[0] = new StandardGeneticAlgorithm(200, 100, 20, nnop[0]);

    for(int i = 0; i < oa.length; i++){
      OptimizationAlgorithm oa_cur = oa[i];
      BackPropagationNetwork network_cur = networks[i];
      String oaName_cur = oaNames[i];
      run_experiment(oa_cur, network_cur, oaName_cur);
    }
  }
}