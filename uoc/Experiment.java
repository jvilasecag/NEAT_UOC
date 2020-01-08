/**
 * Class that represents the environment to use the algorithm in a defined problem
 * @author Joan-Antoni Vilaseca
 */

package uoc;

import java.util.ArrayList;

public class Experiment {
	private ArrayList<Double> inputs;
	private ArrayList<Double> expectedOutputs;
	private ArrayList<Double> outputs;
	
	/**
	 * Builder of the class with the inputs and expected outputs of the problem.
	 * @param inputs
	 * @param expectedOutputs
	 */
	public Experiment(ArrayList<Double> inputs, ArrayList<Double> expectedOutputs) {
		this.inputs = inputs;
		this.outputs = null;
		this.expectedOutputs = expectedOutputs;
	}

	/**
	 * Getter of inputs.
	 * @return inputs
	 */
	public ArrayList<Double> getInputs() {
		return this.inputs;
	}
	
	/**
	 * Getter of outputs.
	 * @return outputs
	 */
	public ArrayList<Double> getOutputs() {
		return this.outputs;
	}
	
	/**
	 * Setter of outputs.
	 * @param outputs
	 */
	public void setOutputs(ArrayList<Double> outputs) {
		this.outputs = outputs;
	}
	
	/**
	 * Getter of expectedOutputs.
	 * @return expectedOutputs
	 */
	public ArrayList<Double> getExpectedOutputs() {
		return this.expectedOutputs;
	}
	
	
}
