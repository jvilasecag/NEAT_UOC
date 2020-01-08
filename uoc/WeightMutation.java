/**
 * Class that defines a mutation of the weights of the Connection Genes of a Genome.
 * @author Joan-Antoni Vilaseca
 */

package uoc;

import java.util.Map;

public class WeightMutation extends Mutation {

	/**
	 * Builder of the class.
	 * @param genome
	 */
	public WeightMutation(Genome genome) {
		super(genome);
	}
	
	/**
	 * Method to mutate the weights of the Genome of the WeightMutation class.
	 */
	public void mutation() {
		for (Map.Entry<Integer, ConnectionGene> entry : this.getGenome().getConnectionGenesMap().entrySet()) {
			if (Random.randomFloat() < NEAT_UOC.WeightMutationProb) {
				entry.getValue().setWeight(entry.getValue().getWeight() * Random.randomFloat());
			}
		}
	}
}
