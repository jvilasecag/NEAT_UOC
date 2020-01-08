/**
 * Definition of a Genome and the methods that needs its phenotype (Artificial Neural Network).
 * It is composed for a list of its NodeGenes and a TreeMap of ConnectionGenes, plus the last computed fitness.
 * @author Joan-Antoni Vilaseca
 */


package uoc;

import java.util.*;


public class Genome {

	private double fitness;
	private TreeMap<Integer, ConnectionGene> connectionGenesMap = new TreeMap<>();
	private ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
	
	/**
	 * Builder method of the class with no parameters.
	 */
	public Genome() {
	// To do	
	};
	
	/**
	 * Builder method of the class with parameters.
	 * @param inputNodes
	 * @param outputNodes
	 */
	public Genome(int inputNodes, int outputNodes) {
		ConnectionGene.innovationCount = 0;
		int i, j;
		this.fitness = 0;
		for (i=0; i<inputNodes; i++) {
			nodeGenesList.add(new NodeGene(i, NodeGeneState.input));
		}
		for (j=i; j<(inputNodes+outputNodes); j++) {
			nodeGenesList.add(new NodeGene(j, NodeGeneState.output));
		}
		for (i=0; i<inputNodes; i++) {
			for (j=inputNodes; j<inputNodes+outputNodes; j++) {
				ConnectionGene newConnectionGene = new ConnectionGene(nodeGenesList.get(i), nodeGenesList.get(j), 1, true);
				connectionGenesMap.put(newConnectionGene.getInnovationNumber(), newConnectionGene);
			}
		}
	}
	
	/**
	 * Getter of fitness.
	 * @return fitness
	 */
    public double getFitness() {
        return fitness;
    }

    /**
     * Setter of fitness.
     * @param fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Getter of connectionGenesMap.
     * @return connectionGenesMap
     */
	public TreeMap<Integer, ConnectionGene> getConnectionGenesMap() {
		return connectionGenesMap;
	}
	
	/**
	 * Setter of connectionGenesMap.
	 * @param connectionGenesMap
	 */
	public void setConnectionGenesMap(TreeMap<Integer, ConnectionGene> connectionGenesMap) {
		this.connectionGenesMap = connectionGenesMap;
	}
	
	/**
	 * Getter of nodeGenesList.
	 * @return nodeGenesList
	 */
	public List<NodeGene> getNodeGenesList() {
		return nodeGenesList;
	}
	
	/**
	 * Method to add a NodeGene to the nodeGenesList of the Genome.
	 * @param nodeGene
	 */
	public void addNodeGeneToGenome(NodeGene nodeGene) {
		nodeGenesList.add(nodeGene);
	}
	
	/**
	 * Method to add a ConnectionGene to connectionGenesMap of the Genome.
	 * @param connectionGene
	 */
	public void addConnectionGeneToGenome(ConnectionGene connectionGene) {
		connectionGenesMap.put(connectionGene.getInnovationNumber(), connectionGene);
	}
	
	/**
	 * Method to mutate a Genome. The three types of mutation can be done depending
	 * on a random number.
	 * @return a new Genome with the changes of the mutation
	 */
	public Genome mutate() {
		AddNodeMutation addNode = null;
		AddConnectionMutation addConnection = null;
		
		WeightMutation weightMutation = new WeightMutation(this);
		weightMutation.mutation();
		
		if (Random.randomFloat() < NEAT_UOC.AddNodeMutationProb) {
			addNode = new AddNodeMutation(weightMutation.getGenome());
			addNode.mutate();
		} else {
			addNode = new AddNodeMutation(weightMutation.getGenome());
		}
		
		if (Random.randomFloat() < NEAT_UOC.AddConnectionMutationProb) {
			addConnection = new AddConnectionMutation(addNode.getGenome());
			addConnection.mutation();
		} else {
			addConnection = new AddConnectionMutation(addNode.getGenome());
		}
		
		return addConnection.getGenome();
	}
	
/*	public void mutate() {
		for(Map.Entry<Integer, ConnectionGene> entry : connectionGenesMap.entrySet()) {
		//    Integer key = entry.getKey();
			  ConnectionGene GeneToMutate = entry.getValue();
			  
		}
	}*/
	
	/**
	 * Method to create a new Genome using a mix of ConnectionGenes from the 2 parents
	 * directed by the innovation number.
	 * @param parent1
	 * @param parent2
	 * @return a new Genome that is a child of parent1 and parent2
	 */
	public Genome crossOver (Genome parent1, Genome parent2) {
		Genome newGenome = new Genome();
		if (parent1.getNodeGenesList().size() >= parent2.getNodeGenesList().size()) {
			for (int i=0; i < parent1.getNodeGenesList().size(); i++) {
				newGenome.addNodeGeneToGenome(new NodeGene(i, parent1.getNodeGenesList().get(i).getState()));
			}
		} else {
			for (int i=0; i < parent2.getNodeGenesList().size(); i++) {
				newGenome.addNodeGeneToGenome(new NodeGene(i, parent2.getNodeGenesList().get(i).getState()));
			}
		}
		TreeMap<Integer, ConnectionGene> offspringConnectionGenes = new TreeMap<>();
		TreeMap<Integer, ConnectionGene> connectionGenes1 = parent1.getConnectionGenesMap();
		TreeMap<Integer, ConnectionGene> connectionGenes2 = parent2.getConnectionGenesMap();
		
		int innovationNumber1 = connectionGenes1.lastKey();
		int innovationNumber2 = connectionGenes2.lastKey();

		int maxInnovationNumber = Math.max(innovationNumber1, innovationNumber2);

		for (int i=1; i <= maxInnovationNumber; i++) {
			if (connectionGenes1.containsKey(i) && connectionGenes2.containsKey(i)) {
				if (Random.randomBoolean()) {
					offspringConnectionGenes.put(i, connectionGenes1.get(i));
					newGenome.getNodeGenesList().get(connectionGenes1.get(i).getOutNode().getId()).getIncomingNodes().
						add(connectionGenes1.get(i).getInNode());
				} else {
					offspringConnectionGenes.put(i, connectionGenes2.get(i));
					newGenome.getNodeGenesList().get(connectionGenes2.get(i).getOutNode().getId()).getIncomingNodes().
					add(connectionGenes2.get(i).getInNode());
				}
			} else if (connectionGenes1.containsKey(i)) {
				offspringConnectionGenes.put(i, connectionGenes1.get(i));
				newGenome.getNodeGenesList().get(connectionGenes1.get(i).getOutNode().getId()).getIncomingNodes().
				add(connectionGenes1.get(i).getInNode());
			} else if (connectionGenes2.containsKey(i)) {
				offspringConnectionGenes.put(i, connectionGenes2.get(i));
				newGenome.getNodeGenesList().get(connectionGenes2.get(i).getOutNode().getId()).getIncomingNodes().
				add(connectionGenes2.get(i).getInNode());
			}
		}
		
		newGenome.setConnectionGenesMap(offspringConnectionGenes);
		return newGenome;
	}
	
	/**
	 * Method to calculate the distance between two genomes in function disjoint and excess genes
	 * and the average weight difference of its weights.
	 * @param genomeToMeasure
	 * @return distance measured between two genomes
	 */
	public double distanceGenomes(Genome genomeToMeasure) {
		double disjointGenes = 0;
		double excessGenes = 0;
		int maxInnovationNumber;
		int minInnovationNumber;
		int N;
		
		TreeMap<Integer, ConnectionGene> connectionGenes1 = this.getConnectionGenesMap();
		TreeMap<Integer, ConnectionGene> connectionGenes2 = genomeToMeasure.getConnectionGenesMap();
		
		int innovationNumber1 = connectionGenes1.lastKey();
		int innovationNumber2 = connectionGenes2.lastKey();
		
		if (innovationNumber1 >= innovationNumber2) {
			maxInnovationNumber = innovationNumber1;
			minInnovationNumber = innovationNumber2;
			N = connectionGenes1.size();
		} else {
			maxInnovationNumber = innovationNumber2;
			minInnovationNumber = innovationNumber1;
			N = connectionGenes1.size();
		}
		
		for (int i=1; i <= minInnovationNumber; i++) {
			if (!(connectionGenes1.containsKey(i) && connectionGenes2.containsKey(i))) {
				disjointGenes++;
			}
			if (!connectionGenes1.containsKey(i) && !connectionGenes2.containsKey(i)) {
				disjointGenes--;
			}
		}
		for (int i=minInnovationNumber+1; i <= maxInnovationNumber; i++) {
			if (connectionGenes1.containsKey(i) || connectionGenes1.containsKey(i)) {
				excessGenes++;
			}

		}
		return NEAT_UOC.c1*excessGenes/N + NEAT_UOC.c2*disjointGenes/N + NEAT_UOC.c3*this.avgWeightDifference(genomeToMeasure);
	}

	/**
	 * Method to calculate the average weight difference of the ConnectionGenes of two Genomes.
	 * @param genomeToWeight
	 * @return average Weight difference
	 */
	public double avgWeightDifference(Genome genomeToWeight) {
		double weightDifference = 0;
		double N = 0;
		
		TreeMap<Integer, ConnectionGene> connectionGenes1 = this.getConnectionGenesMap();
		TreeMap<Integer, ConnectionGene> connectionGenes2 = genomeToWeight.getConnectionGenesMap();
		
		int innovationNumber1 = connectionGenes1.lastKey();
		int innovationNumber2 = connectionGenes2.lastKey();
		
		int minInnovationNumber = Math.min(innovationNumber1, innovationNumber2);
		
		for (int i=1; i <= minInnovationNumber; i++) {
			if (connectionGenes1.containsKey(i) && connectionGenes2.containsKey(i)) {
				weightDifference = weightDifference + Math.abs(connectionGenes1.get(i).getWeight()-connectionGenes2.get(i).getWeight());
				N++;
			}
		}
		return weightDifference / N;		
	}
	
	/**
	 * Method that calculate the fitness of a genome after the execution of an Experiment.
	 * @param e Experiment executed for the Genome
	 */
	public void calculateFitness (Experiment e) {
		double fitness = 0;
		for (int i=0; i < e.getOutputs().size(); i++) {
			fitness = fitness + Math.pow(e.getOutputs().get(i) - e.getExpectedOutputs().get(i), 2);
		}
		setFitness(Math.sqrt(fitness));
	}
	
	/**
	 * Method to activate the output nodes for computing the output values of an Experiment.
	 * @param e Experiment that we want to do
	 * @return Output values this Genome has computed for this Experiment
	 */
	public ArrayList<Double> activate(Experiment e) {
		ArrayList<Double> realOutput = new ArrayList<>();
		Iterator<NodeGene> nodesItr = this.getNodeGenesList().iterator();
		while (nodesItr.hasNext()) {
			NodeGene activationGene = nodesItr.next();
			if (activationGene.getState() == NodeGeneState.output) {
				realOutput.add(activationGene.activateNode(e, new ArrayList<NodeGene>()));
			}
		}
		return realOutput;
	}
	
	/**
	 * Method to check if two genomes are equals.
	 * @param Genome2
	 * @return true if they are equals or false if they are not equals
	 */
	public boolean equals(Genome Genome2) {
		 if (Genome2 == null) {
			 return false;
		 }
		 if (Genome2.getFitness() == this.getFitness()) {
			 if (Genome2.getConnectionGenesMap() == this.getConnectionGenesMap()) {
			 	if (Genome2.getNodeGenesList() == this.getNodeGenesList()) {
			 		 return true;
			 	}
		 	}
		 }
		 return false;
	 }
	 
/*	 public void speciate(ArrayList<Species> speciesList) {
		 boolean found = false;
		 int i=0;
			while (i<speciesList.size() && found != true) {
				if (this.distanceGenomes(speciesList.get(i).getRepresentative())
						< NEAT_UOC.speciationThreshold) {
					found = true;
					if (!(this.getSpeciesList().get(i).getGenomesList().contains(speciatingGenome))) {
						this.getSpeciesList().get(i).getGenomesList().add(speciatingGenome);
					}
				}
				i++;
			}
			if (found != true) {
				this.getSpeciesList().add(new Species(speciatingGenome));
			}
	 }*/
}
