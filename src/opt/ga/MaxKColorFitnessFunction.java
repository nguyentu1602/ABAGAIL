package opt.ga;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

/**
 * A Max K Color evaluation function
 * @author kmandal
 * @version 1.0
 */
public class MaxKColorFitnessFunction implements EvaluationFunction {
    
    /**
     * 
     */
    private Vertex[] vertices;
    private int graphSize;
    
    public MaxKColorFitnessFunction(Vertex[] vertices) {
        this.vertices = vertices;
        this.graphSize = vertices.length;
    }
    
    private boolean conflict = false;

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     * Find how many iterations does it take to find if k-colors can be or can not be assigned to a given graph.
     */

    public double value(Instance d) {
        Vector data = d.getData();                      // Data is color vector of length=graph vertices
        int N = data.size();
        if(N!=graphSize) throw new ValueException("\n\n Data vector and Graph size don't match\n\n");
        double iterations = 0;
        conflict = false;
        //System.out.println("Sample color " + d.toString());
        for (int i = 0; i < N; i++) {
            int sampleColor = ((int) data.get(i));
            for(int adjInd : vertices[i].getAadjacencyColorMatrix()) {
                if ((int) data.get(adjInd) != sampleColor) {
                    iterations++;
                } else {
                    conflict = true;
                    break;
                }
            }
            if(conflict) break;
        }
        return iterations;
    }
    
    public String foundConflict(){
    	return conflict ? "Failed to find Max-K Color combination !" : "Found Max-K Color Combination !";
    }
}
