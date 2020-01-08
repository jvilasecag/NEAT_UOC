/**
 * Class that defines a structural mutation where a connection is split and a new node is added.
 * @author Joan-Antoni Vilaseca
 */

package uoc;

public class AddNodeMutation extends Mutation {

	/**
	 * Builder of the class
	 * @param genome
	 */
	public AddNodeMutation(Genome genome) {
		super(genome);
	}
	
	/**
	 * Method that adds a new Node splitting a ConnnectionGene.
	 */
	public void mutate() {
	
		ConnectionGene geneToDisable = this.getGenome().getConnectionGenesMap().
				get(this.getGenome().getConnectionGenesMap().ceilingKey(Random.randomInt(this.getGenome().getConnectionGenesMap().lastKey())+1));

		geneToDisable.setEnableBit(false);
		
		int newId = this.getGenome().getNodeGenesList().get(this.getGenome().getNodeGenesList().size()-1).getId();
		NodeGene newNode = new NodeGene(newId + 1, NodeGeneState.hidden);
		
		// two new connection genes
		NodeGene previousGene = geneToDisable.getInNode();
		NodeGene postGene = geneToDisable.getOutNode();
		
	//	newNode.addIncomingNode(previousGene);
	//	postGene.addIncomingNode(newNode);
		postGene.deleteIncomingNode(previousGene);

		this.genome.addNodeGeneToGenome(newNode);
		this.genome.addConnectionGeneToGenome(new ConnectionGene(previousGene, newNode, geneToDisable.getWeight(), true));
		this.genome.addConnectionGeneToGenome(new ConnectionGene(newNode, postGene, 1D, true));
	}
	
}
