package opt.test;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.BinaryClassificationEntropyError;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;

public class VnstockTest extends VnstockTestCommon {
  public static void main(String[] args) {
  oaNames = new String[] {"RHC", "SA", "GA"};
    outfile = "vnstock_combo_100.txt";
    prepare_experiment(oaNames, 100, 100, outfile);

    for(int i = 0; i < oa.length; i++) {
      networks[i] = factory.createClassificationNetwork(
          new int[] {inputLayer, hiddenLayer1, hiddenLayer2, outputLayer});
      nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
    }

    oa[0] = new RandomizedHillClimbing(nnop[0]);
    oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
//    oa[2] = new SimulatedAnnealing(1E11, .95, nnop[2]);

    oa[2] = new StandardGeneticAlgorithm(200, 100, 20, nnop[2]);

    for(int i = 0; i < oa.length; i++) {
      OptimizationAlgorithm oa_cur = oa[i];
      BackPropagationNetwork network_cur = networks[i];
      String oaName_cur = oaNames[i];
      run_experiment(oa_cur, network_cur, oaName_cur);
    }
 }
}