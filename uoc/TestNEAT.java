/**
 * Class that implements the JUnit tests to check the correct operation of the builders of the classes
 * and its methods.
 * @author Joan-Antoni Vilaseca
 */

package uoc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import uoc.graph.GraphNEAT;
import uoc.graph.ShortestPathNEAT;
import uoc.recursiveNEAT.NEAT_Recursive;

import org.jgrapht.Graph;


public class TestNEAT {
	@Before
	public void setUp() {
		ConnectionGene.innovationCount = 0;
	}

	@Test
	public void createNodeGene() {
		NodeGene nodeGeneToTest = new NodeGene(1, NodeGeneState.hidden);
		assertEquals(nodeGeneToTest.getId(), 1);
		assertEquals(nodeGeneToTest.getState(), NodeGeneState.hidden);
		assertTrue(nodeGeneToTest.getIncomingNodes().isEmpty());
		assertTrue(nodeGeneToTest.getIncomingConnections().isEmpty());
	}
	
	@Test
	public void createConnectionGene() {
		NodeGene nodeGeneToTest1 = new NodeGene(1, NodeGeneState.hidden);
		NodeGene nodeGeneToTest2 = new NodeGene(2, NodeGeneState.hidden);
		NodeGene nodeGeneToTest3 = new NodeGene(3, NodeGeneState.hidden);
		NodeGene nodeGeneToTest4 = new NodeGene(4, NodeGeneState.hidden);
		ConnectionGene connectionGeneToTest = new ConnectionGene(nodeGeneToTest1 , nodeGeneToTest2, 0.1, true);
		ConnectionGene connectionGeneToTest2 = new ConnectionGene(nodeGeneToTest2, nodeGeneToTest3, 0.2, false);
		ConnectionGene connectionGeneToTest3 = new ConnectionGene(nodeGeneToTest4, nodeGeneToTest3, 0.3, true);
		
		assertEquals(connectionGeneToTest.getInnovationNumber(), 1);
		assertEquals(connectionGeneToTest2.getInnovationNumber(), 2);
		assertEquals(connectionGeneToTest3.getInnovationNumber(), 3);
		
		assertEquals(connectionGeneToTest.getWeight(), 0.1, 0.001);
		assertTrue(connectionGeneToTest.isEnabled());
		assertFalse(connectionGeneToTest2.isEnabled());
		assertEquals(connectionGeneToTest.getOutNode().getIncomingNodes().size(), 1);

	}
	
	@Test
	public void incomingNodesLists() {
		NodeGene nodeGeneToTest1 = new NodeGene(1, NodeGeneState.hidden);
		NodeGene nodeGeneToTest2 = new NodeGene(2, NodeGeneState.hidden);
		NodeGene nodeGeneToTest3 = new NodeGene(3, NodeGeneState.hidden);

	//	ConnectionGene connectionGeneToTest = new ConnectionGene(nodeGeneToTest1 , nodeGeneToTest2, 0.1, true);
		ConnectionGene connectionGeneToTest2 = new ConnectionGene(nodeGeneToTest2, nodeGeneToTest3, 0.2, false);
		ConnectionGene connectionGeneToTest3 = new ConnectionGene(nodeGeneToTest1, nodeGeneToTest3, 0.3, true);
		
		assertEquals(connectionGeneToTest3.getOutNode().getIncomingNodes().size(), 2);
		assertEquals(connectionGeneToTest3.getOutNode().getIncomingConnections().size(), 2);
		
		assertEquals(nodeGeneToTest3.getIncomingNodes().get(0), nodeGeneToTest2);
		assertEquals(nodeGeneToTest3.getIncomingConnections().get(0), connectionGeneToTest2);

		assertEquals(nodeGeneToTest3.getIncomingNodes().get(1), nodeGeneToTest1);
		assertEquals(nodeGeneToTest3.getIncomingConnections().get(1), connectionGeneToTest3);
	}
	
	@Test
	public void createGenome() {
		Genome genomeToTest = new Genome(5,2);
		
		assertEquals(genomeToTest.getFitness(), 0, 0.1);
		
		assertEquals(genomeToTest.getNodeGenesList().size(),7);
		assertEquals(genomeToTest.getConnectionGenesMap().size(), 10);
		
		assertEquals(genomeToTest.getNodeGenesList().get(0).getId(), 0);
		assertEquals(genomeToTest.getNodeGenesList().get(3).getId(), 3);
		assertEquals(genomeToTest.getNodeGenesList().get(4).getId(), 4);
		assertEquals(genomeToTest.getNodeGenesList().get(6).getId(), 6);
		assertEquals(genomeToTest.getConnectionGenesMap().get(1).getInnovationNumber(), 1);
		assertEquals(genomeToTest.getConnectionGenesMap().get(10).getInnovationNumber(), 10);
		assertEquals(genomeToTest.getConnectionGenesMap().get(5).getInNode().getId(), 2);
		assertEquals(genomeToTest.getConnectionGenesMap().get(5).getOutNode().getId(), 5);
		
		assertEquals(genomeToTest.getConnectionGenesMap().get(10).getWeight(), 1, 0.1);
		assertTrue(genomeToTest.getConnectionGenesMap().get(10).isEnabled());
		assertEquals(genomeToTest.getConnectionGenesMap().get(6).getOutNode().getIncomingNodes().get(2).getId(), 2);
		assertEquals(genomeToTest.getConnectionGenesMap().get(6).getOutNode().getIncomingNodes().size(), 5);
		assertEquals(genomeToTest.getConnectionGenesMap().get(6).getOutNode().getIncomingConnections().get(2).getInnovationNumber(), 6);
		assertEquals(genomeToTest.getConnectionGenesMap().get(6).getOutNode().getIncomingConnections().size(), 5);

	}
	

	@Test
	public void addNodeMutation() {
		Genome genomeToTest = new Genome (1,1);
		
		AddNodeMutation mutationToTest = new AddNodeMutation(genomeToTest);
		mutationToTest.mutate();
		
		assertEquals(genomeToTest.getNodeGenesList().size(), 3);
		assertEquals(genomeToTest.getConnectionGenesMap().size(), 3);
		assertEquals(genomeToTest.getNodeGenesList().get(2).getId(),2);
		assertEquals(genomeToTest.getNodeGenesList().get(2).getIncomingConnections().size(), 1);
		assertEquals(genomeToTest.getNodeGenesList().get(2).getIncomingConnections().get(0).getInnovationNumber(), 2);
		assertEquals(genomeToTest.getNodeGenesList().get(2).getIncomingConnections().get(0).getInNode().getId(), 0);
		assertEquals(genomeToTest.getNodeGenesList().get(2).getIncomingConnections().get(0).getOutNode().getId(), 2);
		assertTrue(genomeToTest.getNodeGenesList().get(2).getIncomingConnections().get(0).isEnabled());
		assertEquals(genomeToTest.getNodeGenesList().get(2).getIncomingConnections().get(0).getWeight(), 1, 0.1);
		
	}
 
	@Test
	public void addNodeMutationShouldRemoveIncomingNodesOfPost() {
		Genome genomeToTest = new Genome (1,1);
		
		AddNodeMutation mutationToTest = new AddNodeMutation(genomeToTest);
		mutationToTest.mutate();
		
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingNodes().size(), 1);
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingNodes().get(0).getId(),
				genomeToTest.getNodeGenesList().get(2).getId())
		;
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().size(), 2);
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(0).getInNode().getId(), 0);
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(0).getOutNode().getId(), 1);
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(0).getInnovationNumber(), 1);
		assertFalse(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(0).isEnabled());
		
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(1).getInNode().getId(), 2);
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(1).getOutNode().getId(), 1);
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(1).getInnovationNumber(), 3);
		assertTrue(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(1).isEnabled());
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(1).getWeight(),
				genomeToTest.getNodeGenesList().get(1).getIncomingConnections().get(1).getWeight(), 0.1);
				
		assertEquals(genomeToTest.getNodeGenesList().get(1).getIncomingNodes().get(0),
				genomeToTest.getNodeGenesList().get(2));
	}
	
	@Test
	public void weightMutation() {
		Genome genomeToTest = new Genome (3,1);
				
		WeightMutation mutationToTest = new WeightMutation(genomeToTest);
		mutationToTest.mutation();
		
		assertNotEquals(genomeToTest.getConnectionGenesMap().get(1).getWeight(), 1, 0.00001);
		assertNotEquals(genomeToTest.getConnectionGenesMap().get(2).getWeight(), 1, 0.00001);
	}
	
	@Test
	public void addConnectionMutation() {
		Genome genomeToTest = new Genome (1,1);
		AddNodeMutation mutationToPrepare = new AddNodeMutation(genomeToTest);
		mutationToPrepare.mutate();
		AddConnectionMutation mutationToTest = new AddConnectionMutation(mutationToPrepare.getGenome());
		mutationToTest.mutation();

		assertEquals(genomeToTest.getConnectionGenesMap().lastKey().intValue(), 4);
		assertEquals(genomeToTest.getConnectionGenesMap().get(4).getInNode().getId(), 0);
		assertEquals(genomeToTest.getConnectionGenesMap().get(4).getOutNode().getId(), 1);
		assertTrue(genomeToTest.getConnectionGenesMap().get(4).isEnabled());
		assertEquals(genomeToTest.getConnectionGenesMap().get(4).getOutNode().getIncomingConnections().size(), 3);
	}
	
	@Test
	public void addConnectionMutation2() {
		Genome genomeToTest = new Genome (2,1);
		AddNodeMutation mutationToPrepare = new AddNodeMutation(genomeToTest);
		mutationToPrepare.mutate();
		AddConnectionMutation mutationToTest = new AddConnectionMutation(mutationToPrepare.getGenome());
		mutationToTest.mutation();
		
		assertEquals(genomeToTest.getConnectionGenesMap().lastKey().intValue(), 5);
		assertTrue(genomeToTest.getConnectionGenesMap().get(5).getInNode().getId()== 0
				|| genomeToTest.getConnectionGenesMap().get(5).getInNode().getId()== 1);
		assertTrue(genomeToTest.getConnectionGenesMap().get(5).getOutNode().getId() == 2
				|| genomeToTest.getConnectionGenesMap().get(5).getOutNode().getId() == 3);
		assertTrue(genomeToTest.getConnectionGenesMap().get(4).isEnabled());		
	}
	
	@Test
	public void genomeMutateShouldReturnAGenome() {
		Genome genomeToTest = new Genome (5, 2);
		Genome genomeMutated = genomeToTest.mutate();

		assertNotEquals(genomeMutated.equals(genomeToTest), 0);
		assertTrue(genomeMutated instanceof Genome);
	}
	
	@Test
	public void crossOverShouldReturnAGenome() {
		Genome genomeToTest1 = new Genome (5, 2);
		Genome genomeToTest2 = new Genome (5, 2);
		Genome genomeToTest3 = new Genome (2, 1);
		Genome child1 = genomeToTest1.crossOver(genomeToTest1, genomeToTest2);
		Genome child2 = genomeToTest1.crossOver(genomeToTest1, genomeToTest3);

		assertTrue(child1 instanceof Genome);
		assertTrue(child2 instanceof Genome);
		assertEquals(child1.getConnectionGenesMap().get(child1.getConnectionGenesMap().lastKey()).getInnovationNumber(), 10);
		assertEquals(child2.getConnectionGenesMap().get(child1.getConnectionGenesMap().lastKey()).getInnovationNumber(), 10);
	}
	
	@Test
	public void speciesWithTwoGenomes() {
		Genome genomeToTest1 = new Genome (5, 2);
		Genome genomeToTest2 = new Genome (5, 2);
		
		Species speciesToTest = new Species(genomeToTest1);
		speciesToTest.addGenome(genomeToTest1);
		speciesToTest.addGenome(genomeToTest2);
		
		genomeToTest1.setFitness(0.8);
		genomeToTest2.setFitness(0.2);
		speciesToTest.calculateAverageFitness();
		
		assertEquals(speciesToTest.getGenomesList().size(), 2);
		assertEquals(speciesToTest.getAverageFitness(), 0.5, 0.0001);
	}
	
	@Test
	public void sortSpeciesShouldGoFirstTopGenomes() {
		Genome genomeToTest1 = new Genome (5, 2);
		Genome genomeToTest2 = new Genome (5, 2);
		Genome genomeToTest3 = new Genome (5, 2);
		Genome genomeToTest4 = new Genome (5, 2);
		
		Species speciesToTest = new Species(genomeToTest1);
		speciesToTest.addGenome(genomeToTest1);
		speciesToTest.addGenome(genomeToTest2);
		speciesToTest.addGenome(genomeToTest3);
		speciesToTest.addGenome(genomeToTest4);
		
		genomeToTest1.setFitness(0.6);
		genomeToTest2.setFitness(0.2);
		genomeToTest3.setFitness(0.8);
		genomeToTest4.setFitness(0.4);
		
		speciesToTest.sortSpecies();
		
		assertEquals(speciesToTest.getGenomesList().get(0), genomeToTest2);
		assertEquals(speciesToTest.getGenomesList().get(1), genomeToTest4);
		assertEquals(speciesToTest.getGenomesList().get(2), genomeToTest1);
		assertEquals(speciesToTest.getGenomesList().get(3), genomeToTest3);
	}
	
	@Test
	public void pruneSpeciesShouldReturnTopGenomes() {
		Genome genomeToTest1 = new Genome (5, 2);
		Genome genomeToTest2 = new Genome (5, 2);
		Genome genomeToTest3 = new Genome (5, 2);
		Genome genomeToTest4 = new Genome (5, 2);
		
		Species speciesToTest = new Species(genomeToTest1);
		speciesToTest.addGenome(genomeToTest1);
		speciesToTest.addGenome(genomeToTest2);
		speciesToTest.addGenome(genomeToTest3);
		speciesToTest.addGenome(genomeToTest4);
		
		genomeToTest1.setFitness(0.6);
		genomeToTest2.setFitness(0.2);
		genomeToTest3.setFitness(0.8);
		genomeToTest4.setFitness(0.4);
		
		speciesToTest.pruneSpecies();
		
		assertEquals(speciesToTest.getGenomesList().size(), 2);
		assertEquals(speciesToTest.getGenomesList().get(0), genomeToTest2);
		assertEquals(speciesToTest.getGenomesList().get(1), genomeToTest4);
	}
	
	@Test
	public void pruneSpeciesOddShouldReturnTopGenomes() {
		Genome genomeToTest1 = new Genome (5, 2);
		Genome genomeToTest2 = new Genome (5, 2);
		Genome genomeToTest3 = new Genome (5, 2);
		Genome genomeToTest4 = new Genome (5, 2);
		Genome genomeToTest5 = new Genome (5, 2);
		
		Species speciesToTest = new Species(genomeToTest1);
		speciesToTest.addGenome(genomeToTest1);
		speciesToTest.addGenome(genomeToTest2);
		speciesToTest.addGenome(genomeToTest3);
		speciesToTest.addGenome(genomeToTest4);
		speciesToTest.addGenome(genomeToTest5);
		
		genomeToTest1.setFitness(0.6);
		genomeToTest2.setFitness(0.2);
		genomeToTest3.setFitness(0.8);
		genomeToTest4.setFitness(0.4);
		genomeToTest5.setFitness(0.5);
		
		speciesToTest.pruneSpecies();
		
		assertEquals(speciesToTest.getGenomesList().size(), 3);
		assertEquals(speciesToTest.getGenomesList().get(0), genomeToTest2);
		assertEquals(speciesToTest.getGenomesList().get(1), genomeToTest4);
	}
	
	@Test
	public void newSortedTopFitnessShouldReturnNewTopFitness() {
		Genome genomeToTest1 = new Genome (5, 2);
		Genome genomeToTest2 = new Genome (5, 2);
		Genome genomeToTest3 = new Genome (5, 2);
		Genome genomeToTest4 = new Genome (5, 2);
		Genome genomeToTest5 = new Genome (5, 2);
		
		Species speciesToTest = new Species(genomeToTest1);
		speciesToTest.addGenome(genomeToTest1);
		speciesToTest.addGenome(genomeToTest2);
		speciesToTest.addGenome(genomeToTest3);
		speciesToTest.addGenome(genomeToTest4);
		speciesToTest.addGenome(genomeToTest5);
		
		genomeToTest1.setFitness(0.6);
		genomeToTest2.setFitness(0.2);
		genomeToTest3.setFitness(0.8);
		genomeToTest4.setFitness(0.4);
		genomeToTest5.setFitness(0.5);
		
		speciesToTest.calculateAverageFitness();
		speciesToTest.setTopFitness(speciesToTest.getAverageFitness());
		speciesToTest.sortSpecies();
		speciesToTest.newSortedTopFitness();
		
		assertEquals(speciesToTest.getTopFitness(), 0.2, 0.0001);
	}
	
	@Test
	public void newPopulationShouldFillSpecies() {
		Population populationToTest = new Population(4, 1);
		
		assertEquals(populationToTest.getSpeciesList().size(), 10);
		assertEquals(populationToTest.getSpeciesList().get(0).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(1).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(2).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(3).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(4).getGenomesList().size(), 5);
	}
	
	@Test
	public void populationMutateShouldMutate() {
		Population populationToTest = new Population(4, 1);
		Genome genomeToTestPrevious = populationToTest.getSpeciesList().get(0).getGenomesList().get(0);
		populationToTest.mutate();
		Genome genomeToTestPost = populationToTest.getSpeciesList().get(0).getGenomesList().get(0);
		
		assertEquals(populationToTest.getSpeciesList().size(), 10);
		assertEquals(populationToTest.getSpeciesList().get(0).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(1).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(2).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(3).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(4).getGenomesList().size(), 5);
		assertNotEquals(genomeToTestPrevious.equals(genomeToTestPost), 0);
	}
	
	@Test
	public void reproduceShouldReturnSameNumberOfSpecies() {
		Population populationToTest = new Population(4, 1);
		Genome genomeToTestPrevious = populationToTest.getSpeciesList().get(0).getGenomesList().get(0);
		populationToTest.mutate();
		populationToTest.reproduce();
		Genome genomeToTestPost = populationToTest.getSpeciesList().get(0).getGenomesList().get(0);
		
		assertEquals(populationToTest.getSpeciesList().size(), 10);
		assertEquals(populationToTest.getSpeciesList().get(0).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(1).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(2).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(3).getGenomesList().size(), 5);
		assertEquals(populationToTest.getSpeciesList().get(4).getGenomesList().size(), 5);
		assertNotEquals(genomeToTestPrevious.equals(genomeToTestPost), 0);
	}
	
	@Test
	public void speciationShouldReturnSameNumberOfGenome() {
		Population populationToTest = new Population(4, 1);
		populationToTest.mutate();
		populationToTest.reproduce();
		populationToTest.speciation();
		int genomesCount = 0;
		Iterator<Species> itr = populationToTest.getSpeciesList().iterator();
		while (itr.hasNext()) {
			Species speciesToTest = itr.next();
			Iterator<Genome> itrGen = speciesToTest.getGenomesList().iterator();
			while (itrGen.hasNext()) {
				itrGen.next();
				genomesCount++;
			}
		}
		assertEquals(genomesCount, NEAT_UOC.maxGenomes);
	}
	
	@Test
	public void trainShouldReturnACloseOutput() {
		Population populationToTest = new Population(2, 1);
		ArrayList<Double> inputs = new ArrayList<Double>();
		ArrayList<Double> expectedOutputs = new ArrayList<Double>();
		inputs.add(2.0);
		inputs.add(3.0);
		expectedOutputs.add(5.0);
		Experiment sumFive = new Experiment(inputs, expectedOutputs);
		ArrayList<Double> inputs10 = new ArrayList<Double>();
		ArrayList<Double> expectedOutputs10 = new ArrayList<Double>();
		populationToTest.mutate();
		populationToTest.reproduce();
		populationToTest.speciation();
		populationToTest.train(sumFive);
		populationToTest.getSpeciesList().get(0).listSortedSpecies();
		System.out.println(sumFive.getOutputs());
		System.out.println(populationToTest.predict(sumFive).get(0));
		System.out.println(populationToTest.predict(sumFive).get(0));
		System.out.println(populationToTest.bestGenome(sumFive).getFitness());
		assertEquals(populationToTest.predict(sumFive).get(0), 5, 1);
	}
	
	@Test
	public void graphGenerator() {
		GraphNEAT newGraph = new GraphNEAT(10,3);
		newGraph.Dijkstra(2, 8);
		newGraph.AStar(2, 8);
	}
	
	@Test
	public void recursiveNEAT() {
		GraphNEAT newGraph = new GraphNEAT(30, 3);
		ShortestPathNEAT sPN = new ShortestPathNEAT(newGraph, 1, 8);
		NEAT_Recursive recursiveSPN = new NEAT_Recursive(sPN.createNEATPopulation());
		recursiveSPN.NEAT_Algorithm(sPN);
		Genome bestGenome = recursiveSPN.bestGenome();
		ArrayList<Integer> prediction = recursiveSPN.predictRecursive(sPN, bestGenome);
		sPN.getGraph().printGraph();
		System.out.println("El millor camí trobat per recursive NEAT és: " + prediction);
		sPN.getGraph().Dijkstra(sPN.getStartVertex(), sPN.getTargetVertex());
	}
}

