package uoc.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.generate.SimpleWeightedGraphMatrixGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;

import uoc.Random;

public class GraphNEAT {

	private Graph<Integer, DefaultWeightedEdge> graph;
	
	public Graph<Integer, DefaultWeightedEdge> getGraph()
	{
		return graph;
	}
	
	public void setGraph(Graph<Integer, DefaultWeightedEdge> g) {
		this.graph = g;
	}
	
	public GraphNEAT (int numberNodes, int maxGrade) {
		List<Integer> vertices = new ArrayList<Integer>();
		for (int i = 0; i < numberNodes; i++) {
			vertices.add(i);
		}
	
		double[][] weights = new double[numberNodes][numberNodes];
		for (int i=0; i<numberNodes; i++) {
			for (int j=0; j<numberNodes; j++) {
				weights[i][j] = 0;
			}
		}
		for (int i = 0; i < numberNodes; i++) {
			for (int j = 0; j < Math.floorDiv(maxGrade, 2); j++) {
				int edgeToAdd = Random.randomInt(numberNodes);
				if(edgeToAdd != i) {
					double newWeight = Random.random()*100;
					weights[i][edgeToAdd] = newWeight;
					weights[edgeToAdd][i] = newWeight;
				}
			}
		}

	
		 Supplier<Integer> vSupplier = new Supplier<Integer>()
	        {
	            private int id = 0;

	            @Override
	            public Integer get()
	            {
	                return id++;
	            }
	        };
		setGraph( new SimpleWeightedGraph<>(vSupplier, SupplierUtil.createDefaultWeightedEdgeSupplier()));
		
		SimpleWeightedGraphMatrixGenerator<Integer, DefaultWeightedEdge> weightedGraphGenerator = 
				new SimpleWeightedGraphMatrixGenerator<Integer, DefaultWeightedEdge>();
		
		weightedGraphGenerator.vertices(vertices);
		
		weightedGraphGenerator.weights(weights);
		
//		weightedGraphGenerator.generateGraph(g);
        for (Integer vertex : vertices) {
        	getGraph().addVertex(vertex);
        }

        for (int i = 0; i < vertices.size(); ++i) {

           for (int j = 0; j < vertices.size(); ++j) {
                if ((i != j) && (!getGraph().containsEdge(vertices.get(i), vertices.get(j))) && (weights[i][j] != 0)) {
                	getGraph().setEdgeWeight(
                    		getGraph().addEdge(vertices.get(i), vertices.get(j)), weights[i][j]);
                }
            }
        }
		
	}
	
	public void printGraph() {
		Iterator<Integer> iter = new DepthFirstIterator<>(this.getGraph());
        while (iter.hasNext()) {
            Integer vertex = iter.next();
            System.out.println(vertex);
            System.out.println(
                "Vertex " + vertex + " is connected to: "
                    + this.getGraph().edgesOf(vertex).toString());
        }
	}
	
	public void Dijkstra (Integer start, Integer target) {
		System.out.println("Shortest path from "+ start +" to "+ target +":");
        DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(getGraph());
        SingleSourcePaths<Integer, DefaultWeightedEdge> iPaths = dijkstraAlg.getPaths(start);
        System.out.println(iPaths.getPath(target) + "\n");
	}
	
	public void AStar (Integer start, Integer target) {
		AStarHeuristic aStarHeur = new AStarHeuristic();
		System.out.println("Shortest path from "+ start +" to "+ target +":");
		AStarShortestPath<Integer, DefaultWeightedEdge> aStarAlg = new AStarShortestPath<>(getGraph(), aStarHeur);
		SingleSourcePaths<Integer, DefaultWeightedEdge> iPaths = aStarAlg.getPaths(start);
        System.out.println(iPaths.getPath(target) + "\n");
	}
}
