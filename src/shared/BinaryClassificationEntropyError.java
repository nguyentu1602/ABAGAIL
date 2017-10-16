package shared;

public class BinaryClassificationEntropyError extends AbstractErrorMeasure
    implements GradientErrorMeasure {

  /**
   * @see nn.error.ErrorMeasure#error(double[], nn.Pattern[], int)
   */
  public double value(Instance output, Instance example) {
    double sum = 0;
    Instance label = example.getLabel(); // example is the ground truth

    for (int i = 0; i < output.size(); i++) {
      double y_true = label.getContinuous(i);
      double y_pred = output.getContinuous(i);  // output is the prediction from the network

      sum += -(y_true*Math.log(y_pred) + (1 - y_true)*Math.log(1 - y_pred))
          * example.getWeight();
    }
    return sum;
  }

  /**
   * @see nn.error.DifferentiableErrorMeasure#derivatives(double[], nn.Pattern[], int)
   */
  public double[] gradient(Instance output, Instance example) {
    double[] errorArray = new double[output.size()];
    Instance label = example.getLabel();
    for (int i = 0; i < output.size(); i++) {
      double y_true = label.getContinuous(i);
      double y_pred = output.getContinuous(i);
      errorArray[i] = ((1 - y_true)/(1 - y_pred) - (y_true / y_pred) )
          * example.getWeight();
    }
    return errorArray;
  }

}

