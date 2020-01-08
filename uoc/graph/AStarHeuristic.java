package uoc.graph;

import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;

public class AStarHeuristic implements AStarAdmissibleHeuristic<Integer>{

	@Override
	public double getCostEstimate(Integer sourceVertex, Integer targetVertex) {

		return (double)Math.abs(sourceVertex - targetVertex);
	}

}
