/**
 * Class that represents the NEAT algorithm. It contains a definitions of constant and parameters 
 * and a main method that launch the NEAT algorithm.
 * @author Joan-Antoni Vilaseca
 */

package uoc;

import java.util.ArrayList;

public class NEAT_UOC {

	public static int maxGenomes = 50;
	public static int inputNodes = 4;
	public static int outputNodes = 1;
	public static int maxInitialSpecies = 10;
	public static int maxGenerations = 200;
	public static double fitnessThreshold = 0.9;
	public static double survivalThreshold = 0.5;
	public static double WeightMutationProb = 1;
	public static double AddNodeMutationProb = 0.5;
	public static double AddConnectionMutationProb = 0.5;
	public static double c1 = 1.0;
	public static double c2 = 1.0;
	public static double c3 = 0.4;
	public static double speciationThreshold = 0.5;
	public static int countAddConnectionMutation = 50;
	
	/**
	 * Main method that implements the NEAT algorithm
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Double> inputs = new ArrayList<Double>();
		ArrayList<Double> expectedOutputs = new ArrayList<Double>();
		inputs.add(2.0);
		inputs.add(3.0);
		expectedOutputs.add(5.0);
		Experiment experiment = new Experiment(inputs, expectedOutputs);
		Population population = new Population(NEAT_UOC.inputNodes, outputNodes);
		while (population.getGeneration() < NEAT_UOC.maxGenerations) {
			population.mutate();
			population.reproduce();
			population.speciation();
			population.train(experiment);
		}
	}

}
