package uoc.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import uoc.recursiveNEAT.NEAT_Recursive;
import uoc.Population;

public class ShortestPathNEAT {

	private GraphNEAT g;
	private Integer startVertex;
	private Integer targetVertex;
	
	public ShortestPathNEAT(GraphNEAT g, Integer start, Integer target) {
		this.g = g;
		startVertex = start;
		targetVertex = target;		
	}
	
	public GraphNEAT getGraph() {
		return g;
	}
	
	public Integer getStartVertex() {
		return startVertex;
	}
	
	public Integer getTargetVertex() {
		return targetVertex;
	}
	
	public int getMaxGrade() {
		int maxGrade = 0;
		Set<Integer> allVertexs = g.getGraph().vertexSet();
		Iterator<Integer> vertexsItr = allVertexs.iterator();
		while(vertexsItr.hasNext()) {
			Integer vertex = vertexsItr.next();
			if (g.getGraph().outDegreeOf(vertex) > maxGrade) {
				maxGrade = g.getGraph().outDegreeOf(vertex);
			}
		}
		return maxGrade;
	}
	
	public ArrayList<Double> getWeightEdges(Integer vertex) {
		ArrayList<Double> edges = new ArrayList<Double>();
		Set<DefaultWeightedEdge> edgesOfVertex = g.getGraph().edgesOf(vertex);
		Iterator<DefaultWeightedEdge> edgesItr = edgesOfVertex.iterator();
		while(edgesItr.hasNext()) {
			DefaultWeightedEdge edge = edgesItr.next();
			edges.add(g.getGraph().getEdgeWeight(edge));
		}
		return edges;
	}
	
	public Population createNEATPopulation() {
		Population populationToTest = null;
		if (g.getGraph().containsVertex(startVertex) && g.getGraph().containsVertex(targetVertex)) {
			populationToTest = new Population(this.getMaxGrade(), this.getMaxGrade());
		}		
		//	NEAT_Recursive AlgorithmNEAT_R = new NEAT_Recursive(populationToTest);
		//	Set<Integer> allVertexs = g.vertexSet();			
		return populationToTest;

	}

}
