package uoc.recursiveNEAT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import uoc.ConnectionGene;
import uoc.Genome;
import uoc.NEAT_UOC;
import uoc.NodeGene;
import uoc.NodeGeneState;
import uoc.Population;
import uoc.Species;
import uoc.graph.ShortestPathNEAT;
import uoc.graph.GraphNEAT;

public class NEAT_Recursive {
	
	public static int maxGenomes = 50;
	public static int maxInitialSpecies = 10;
	public static int maxGenerations = 200;
	public static double survivalThreshold = 0.5;
	public static double WeightMutationProb = 1;
	public static double AddNodeMutationProb = 0.5;
	public static double AddConnectionMutationProb = 0.5;
	public static double c1 = 1.0;
	public static double c2 = 1.0;
	public static double c3 = 0.4;
	public static double speciationThreshold = 0.5;
	public static int countAddConnectionMutation = 50;
	
	private Population population;

	public NEAT_Recursive(Population population) {
		this.population = population;
	}
	
	public Population getPopulation() {
		return this.population;
	}

	public void NEAT_Algorithm(ShortestPathNEAT shortestPath) {
		while (population.getGeneration() < NEAT_UOC.maxGenerations) {
			population.mutate();
			population.reproduce();
			population.speciation();
			this.train(shortestPath);
		}
	}
	
	/**
	 * Method to train all the Genomes of the Population.
	 * @param inputs
	 */
	public void train(ShortestPathNEAT sPN) {

		Iterator<Species> itr = getPopulation().getSpeciesList().iterator();
		while (itr.hasNext()) {
			Species seqSpecies = itr.next();
			Iterator<Genome> itrGen = seqSpecies.getGenomesList().iterator();
			while (itrGen.hasNext()) {
				Genome activatingGenome = itrGen.next();
				trainRecursive(sPN, activatingGenome);
			}
		}
	}
	
	public void trainRecursive(ShortestPathNEAT sPN,  Genome activatingGenome) {
		ArrayList<Double> outputs = new ArrayList<Double>();
		ArrayList<Integer> vertexsPath = new ArrayList<Integer>();
		
		GraphNEAT g = sPN.getGraph();
		Graph<Integer, DefaultWeightedEdge> graphToTrain = g.getGraph();
		Integer nodeToTrain = sPN.getStartVertex();
		Integer actualNode = nodeToTrain;
		Integer lastNode = null;
		
		
		ArrayList<Double> inputs = sPN.getWeightEdges(nodeToTrain);
		if (inputs.size() < sPN.getMaxGrade()) {
			for(int i=inputs.size(); i < sPN.getMaxGrade(); i++) {
				inputs.add(i, (double)sPN.getGraph().getGraph().edgeSet().size() * 1000);
			}
		}
		
		while ((nodeToTrain != sPN.getTargetVertex()) && !(isCyclic(nodeToTrain, vertexsPath))) {
			lastNode = actualNode;
			actualNode = nodeToTrain;
			vertexsPath.add(nodeToTrain);
			Set<DefaultWeightedEdge> edges = graphToTrain.edgesOf(nodeToTrain);
			ArrayList<DefaultWeightedEdge> outputsEdges = new ArrayList<DefaultWeightedEdge>(edges);

			outputs = activateGenome(activatingGenome, inputs);
			
			DefaultWeightedEdge nextEdge = outputsEdges.get(0);
			if(sPN.getGraph().getGraph().getEdgeTarget(nextEdge) == actualNode) {
				nodeToTrain = sPN.getGraph().getGraph().getEdgeSource(nextEdge);
			} else {
				nodeToTrain = sPN.getGraph().getGraph().getEdgeTarget(nextEdge);
			}
			
			Double bestOutput = null;
			Integer nextNodeIndex = null;
			
			if(nodeToTrain != lastNode) {
				bestOutput = outputs.get(0);
				nextNodeIndex = 0;
			} else {
				bestOutput = outputs.get(1);
				nextNodeIndex = 1;
			}

			for(int i=0; i<outputs.size(); i++) {
				if(i < outputsEdges.size()) {
					nextEdge = outputsEdges.get(i);
					if(sPN.getGraph().getGraph().getEdgeTarget(nextEdge) == actualNode) {
						nodeToTrain = sPN.getGraph().getGraph().getEdgeSource(nextEdge);
					} else {
						nodeToTrain = sPN.getGraph().getGraph().getEdgeTarget(nextEdge);
					}
					if((outputs.get(i) < bestOutput) && (nodeToTrain != lastNode) ) {
						bestOutput = outputs.get(i);
						nextNodeIndex = i;
					}
				}
			}
			
			if(nextNodeIndex < outputsEdges.size()) {
				nextEdge = outputsEdges.get(nextNodeIndex);
				activatingGenome.setFitness(activatingGenome.getFitness()+graphToTrain.getEdgeWeight(nextEdge));

				if(sPN.getGraph().getGraph().getEdgeTarget(nextEdge) == actualNode) {
					nodeToTrain = sPN.getGraph().getGraph().getEdgeSource(nextEdge);
				} else {
					nodeToTrain = sPN.getGraph().getGraph().getEdgeTarget(nextEdge);
				}
				inputs = sPN.getWeightEdges(nodeToTrain);
				if (inputs.size() < sPN.getMaxGrade()) {
					for(int i=inputs.size(); i < sPN.getMaxGrade(); i++) {
						inputs.add(i, (double)sPN.getGraph().getGraph().edgeSet().size() * 1000);
					}
				}
			} else {
				activatingGenome.setFitness(activatingGenome.getFitness() + graphToTrain.edgeSet().size() * 1000);
				break;
			}
		}
		if (isCyclic(nodeToTrain, vertexsPath)) {
			activatingGenome.setFitness(activatingGenome.getFitness() + graphToTrain.edgeSet().size() * 1000);
		}
	}
	
	public ArrayList<Integer> predictRecursive(ShortestPathNEAT sPN,  Genome bestGenome) {
		ArrayList<Double> outputs = new ArrayList<Double>();
		ArrayList<Integer> vertexsPath = new ArrayList<Integer>();
		
		GraphNEAT g = sPN.getGraph();
		Graph<Integer, DefaultWeightedEdge> graphToPredict = g.getGraph();
		Integer predictionNode = sPN.getStartVertex();
		Integer actualNode = predictionNode;
		Integer lastNode = null;
		
		ArrayList<Double> inputs = sPN.getWeightEdges(predictionNode);
		if (inputs.size() < sPN.getMaxGrade()) {
			for(int i=inputs.size(); i < sPN.getMaxGrade(); i++) {
				inputs.add(i, (double)sPN.getGraph().getGraph().edgeSet().size() * 1000);
			}
		}
		
		while ((predictionNode != sPN.getTargetVertex()) && !(isCyclic(predictionNode, vertexsPath))) {
			lastNode = actualNode;
			actualNode = predictionNode;
			vertexsPath.add(predictionNode);
			Set<DefaultWeightedEdge> edges = graphToPredict.edgesOf(predictionNode);
			ArrayList<DefaultWeightedEdge> outputsEdges = new ArrayList<DefaultWeightedEdge>(edges);
			
			outputs = activateGenome(bestGenome, inputs);
			
			DefaultWeightedEdge nextEdge = outputsEdges.get(0);
			if(sPN.getGraph().getGraph().getEdgeTarget(nextEdge) == actualNode) {
				predictionNode = sPN.getGraph().getGraph().getEdgeSource(nextEdge);
			} else {
				predictionNode = sPN.getGraph().getGraph().getEdgeTarget(nextEdge);
			}
			
			Double bestOutput = null;
			Integer nextNodeIndex = null;
			
			if(predictionNode != lastNode) {
				bestOutput = outputs.get(0);
				nextNodeIndex = 0;
			} else {
				bestOutput = outputs.get(1);
				nextNodeIndex = 1;
			}
	
			for(int i=0; i<outputs.size(); i++) {
				if(i < outputsEdges.size()) {
					nextEdge = outputsEdges.get(i);
					if(sPN.getGraph().getGraph().getEdgeTarget(nextEdge) == actualNode) {
						predictionNode = sPN.getGraph().getGraph().getEdgeSource(nextEdge);
					} else {
						predictionNode = sPN.getGraph().getGraph().getEdgeTarget(nextEdge);
					}
					if((outputs.get(i) < bestOutput) && (predictionNode != lastNode) ) {
						bestOutput = outputs.get(i);
						nextNodeIndex = i;
					}
				}
			}
			
			if(nextNodeIndex < outputsEdges.size()) {
				nextEdge = outputsEdges.get(nextNodeIndex);
				bestGenome.setFitness(bestGenome.getFitness()+graphToPredict.getEdgeWeight(nextEdge));

				if(sPN.getGraph().getGraph().getEdgeTarget(nextEdge) == actualNode) {
					predictionNode = sPN.getGraph().getGraph().getEdgeSource(nextEdge);
				} else {
					predictionNode = sPN.getGraph().getGraph().getEdgeTarget(nextEdge);
				}
				inputs = sPN.getWeightEdges(predictionNode);
				if (inputs.size() < sPN.getMaxGrade()) {
					for(int i=inputs.size(); i < sPN.getMaxGrade(); i++) {
						inputs.add(i, (double)sPN.getGraph().getGraph().edgeSet().size() * 1000);
					}
				}
			} else {
				bestGenome.setFitness(bestGenome.getFitness() + graphToPredict.edgeSet().size() * 1000);
				break;
			}
		}
		if (predictionNode == sPN.getTargetVertex()) {
			vertexsPath.add(predictionNode);
		}
		if (isCyclic(predictionNode, vertexsPath)) {
			vertexsPath.add(predictionNode);
			bestGenome.setFitness(bestGenome.getFitness() + graphToPredict.edgeSet().size() * 1000);
		}
		return vertexsPath;
	}
			
	
	/** 
	 * Method to find the Genome with the best fitness value
	 * @return Genome with the best fitness value
	 */
	public Genome bestGenome() {
		Genome bestGenome = this.getPopulation().getSpeciesList().get(0).getGenomesList().get(0);
		double bestFitness = 0;

		Iterator<Species> itr = this.getPopulation().getSpeciesList().iterator();
		while (itr.hasNext()) {
			Species seqSpecies = itr.next();
			if (seqSpecies.getGenomesList().get(0).getFitness() <= bestFitness) {
				bestFitness = seqSpecies.getGenomesList().get(0).getFitness();
				bestGenome = seqSpecies.getGenomesList().get(0);
			}
		}
		return bestGenome;
	}

	/**
	 * Method to activate the output nodes for computing the output values of a Genome.
	 * @param genome
	 * @param inputs Array list with the inputs for the Genome
	 * @return Output values for this Genome
	 */
	public ArrayList<Double> activateGenome(Genome genome, ArrayList<Double> inputs) {
		ArrayList<Double> realOutput = new ArrayList<>();
		Iterator<NodeGene> nodesItr = genome.getNodeGenesList().iterator();
		while (nodesItr.hasNext()) {
			NodeGene activationGene = nodesItr.next();
			if (activationGene.getState() == NodeGeneState.output) {
				realOutput.add(activateNode(activationGene, inputs, new ArrayList<NodeGene>()));
			}
		}
		return realOutput;
	}

	 /**
	  * Recursive method to calculate the activationSum of a node during a Recursive NEAT.
	  * The recursivity of the activation stops when the method reaches an input node or a node already visited
	  * in a cyclic path of the network. If it finds a cycle the repeated node doesn't sum up
	  * his activation values twice.
	  * @param node
	  * @param inputs
	  * @param visited
	  * @return activationSum of the node
	  */
	 public double activateNode(NodeGene node, ArrayList<Double> inputs, ArrayList<NodeGene> visited) {
		 node.setActivationSum(0);
		 if ((node.getState() != NodeGeneState.input) && !(node.isCyclic(visited))) {
			Iterator<ConnectionGene> conItr = node.getIncomingConnections().iterator();
			while(conItr.hasNext()) {
				ConnectionGene inConnection = conItr.next();
				visited.add(inConnection.getInNode());
				node.setActivationSum(node.getActivationSum() + inConnection.getWeight() * activateNode(inConnection.getInNode(), inputs,visited));
			}
				
		 } else {
			if (node.getState() != NodeGeneState.input) {
				node.setActivationSum(0);
			} else {
				node.setActivationSum(inputs.get(node.getId()));				
			}
		 }
		 return  node.getActivationSum();
	 }
	 
	 /**
	  * Method to check if a list of nodes is cyclic.
	  * @param node
	  * @param visited
	  * @return true if it's cyclic or false if it's not
	  */
	 private boolean isCyclic(Integer node, ArrayList<Integer> visited) {
	    if (visited.contains(node)) {
	    	return true;	
	    } else {
	    	return false;
	    }
	 }
	
	
}
