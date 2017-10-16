package opt.test;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.OptimizationAlgorithm;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;


public class VnstockTestCommon {

  public static int num_train = 4000;
  public static int num_test  = 1000;
  public static int num_attributes  = 9;

  public static String train_path = "src/opt/test/vnstock_light_train.txt";
  public static String test_path  = "src/opt/test/vnstock_light_test.txt";
   public static double theshold = 0.5;

//  public static ErrorMeasure measure = new SumOfSquaresError();

  public static ErrorMeasure measure = new BinaryClassificationEntropyError();

//  public static int num_train = 4177;
//  public static int num_test  = 4177;
//  public static int num_attributes  = 7;
//  public static double theshold = 15.0;
//
//  public static String train_path = "src/opt/test/abalone.txt";
//  public static String test_path  = "src/opt/test/abalone.txt";

  public static DecimalFormat df  = new DecimalFormat("0.000");


  public static Instance[] instances = initializeInstances(train_path, num_train);  // train instances
  public static DataSet set = new DataSet(instances);                    // train set

  private static Instance[] test_instances = initializeInstances(test_path, num_test);  // test instances
  private static DataSet test_set = new DataSet(test_instances);              // test set

  public static int inputLayer = num_attributes, hiddenLayer1 = 20, hiddenLayer2 = 12, outputLayer = 1;
  public static int trainingIterations = 1000;

  public static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();


  public static String[] oaNames = {"RHC"};
  public static String outfile   = "results.txt";
  public static PrintWriter outwrt;

  public static BackPropagationNetwork networks[] = new BackPropagationNetwork[oaNames.length];
  public static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[oaNames.length];
  public static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[oaNames.length];


  public static String results = "";


  public static int ReportFrequency   = 10;



  public static Instance[] initializeInstances( String pathname, int num_samples ) {

    double[][][] attributes = new double[num_samples][][];

    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(pathname)));

      for(int i = 0; i < attributes.length; i++) {
        Scanner scan = new Scanner(br.readLine());
        scan.useDelimiter(",");

        attributes[i] = new double[2][];
        attributes[i][0] = new double[num_attributes];
        attributes[i][1] = new double[1];

        for(int j = 0; j < num_attributes; j++)
          attributes[i][0][j] = Double.parseDouble(scan.next());

        attributes[i][1][0] = Double.parseDouble(scan.next());
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    Instance[] instances = new Instance[attributes.length];

    for(int i = 0; i < instances.length; i++) {
      instances[i] = new Instance(attributes[i][0]);
      // classifications range from 0 to 1; split into < 1 and >= 1
      instances[i].setLabel(new Instance(attributes[i][1][0] < theshold ? 0 : 1));
    }

    return instances;
  }

  public static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
//    System.out.println("\nError results for " + oaName + "\n---------------------------");
    double start = System.nanoTime(), end, trainingTime, testingTime;

    for(int i = 0; i < trainingIterations; i++) {
      oa.train();
      Instance optimalInstance = oa.getOptimal();
      network.setWeights(optimalInstance.getData());

      int iter = i + 1;

      if ((iter % ReportFrequency) == 0){
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);

        double train_error = calc_error(instances, network);
        double test_error = calc_error(test_instances, network);
        double train_acc = calc_accuracy(instances, network);
        double test_acc = calc_accuracy(test_instances, network);

        String joinedString = iter + "," +  df.format(train_error) + "," +  df.format(test_error) + "," +
            df.format(train_acc) + "," +  df.format(test_acc) + "," +  df.format(trainingTime);

        outwrt.println(joinedString);
//        outwrt.flush();
        System.out.println( joinedString );
      }
    }
  }

  public static void run_experiment(OptimizationAlgorithm oa_cur, BackPropagationNetwork network_cur, String oaName_cur){
    train(oa_cur, network_cur, oaName_cur); //trainer.train();
    outwrt.close();
  }


  public static double calc_error(Instance[] i_instances, BackPropagationNetwork network) {
    double error = 0;
    for(int j = 0; j < i_instances.length; j++) {
      network.setInputValues(i_instances[j].getData());
      network.run();

      Instance example = i_instances[j]; // y_true
      Instance output = new Instance(network.getOutputValues());  // y_pred

      output.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));


      error += measure.value(output, example);
    }

    return error/i_instances.length;
  }

  public static double calc_accuracy(Instance[] i_instances, BackPropagationNetwork network) {

    double correct = 0, incorrect = 0;

    double predicted, actual;

    for(int j = 0; j < i_instances.length; j++) {
      network.setInputValues(i_instances[j].getData());
      network.run();

      predicted = Double.parseDouble(i_instances[j].getLabel().toString());
      actual = Double.parseDouble(network.getOutputValues().toString());

      double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
    }

    return correct/(correct+incorrect)*100;
  }


  public static void prepare_experiment(String[] oaNames, int iterations, int reportFrequency, String outfile){
    trainingIterations = iterations;
    ReportFrequency = reportFrequency;
    networks = new BackPropagationNetwork[oaNames.length];
    nnop = new NeuralNetworkOptimizationProblem[oaNames.length];
    oa = new OptimizationAlgorithm[oaNames.length];

    try {
      outwrt = new PrintWriter(outfile, "UTF-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    for(int i = 0; i < oa.length; i++) {
      networks[i] = factory.createClassificationNetwork(
          new int[] {inputLayer, hiddenLayer1, hiddenLayer2, outputLayer});
      nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
    }
  }
}
