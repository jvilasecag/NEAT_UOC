/**
 * Class that defines a structural mutation where a new connection between two nodes is added.
 * @author Joan-Antoni Vilaseca
 */


package uoc;

import java.util.ArrayList;

public class AddConnectionMutation extends Mutation {

	/**
	 * Builder of the class.
	 * @param genome
	 */
	public AddConnectionMutation(Genome genome) {
		super(genome);
	}
	
	/**
	 * Method that adds a ConnectionNode into the Genome used by the mutation.
	 */
	public void mutation() {
		NodeGene nodeGene1 = null;
		NodeGene nodeGene2 = null;
		boolean found;
		/*if (this.getGenome().getConnectionGenesMap().size()<
				((this.getGenome().getNodeGenesList().size()-NEAT_UOC.inputNodes)*NEAT_UOC.inputNodes)+
				((this.getGenome().getNodeGenesList().size()-NEAT_UOC.inputNodes-NEAT_UOC.outputNodes)*NEAT_UOC.outputNodes)+
				)*/
		int count = 0;
		do {
			count++;
			// the count is used to finish
			if (count < NEAT_UOC.countAddConnectionMutation) {
				found = false;

				nodeGene1 = Random.random(new ArrayList<>(this.getGenome().getNodeGenesList()));
				nodeGene2 = Random.random(new ArrayList<>(this.getGenome().getNodeGenesList()));

				for (NodeGene existsIncoming : nodeGene2.getIncomingNodes()) {
					if (existsIncoming.equals(nodeGene1)) {
						found = true;
						break;
					}
				}
			} else {
				break;
			}			
		} while ((nodeGene1.getId() == nodeGene2.getId()) || 
				(nodeGene1.getState() == NodeGeneState.output) || (nodeGene2.getState() == NodeGeneState.input) ||
				(found == true));
		
		this.genome.addConnectionGeneToGenome(new ConnectionGene(nodeGene1, nodeGene2, Random.random(), true));
	}
}
