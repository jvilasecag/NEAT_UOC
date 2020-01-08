/**
 * Definition of a Species in the NEAT algorithm and its collection of Genomes and the fields and methods
 *  it needs to calculate fitness and classify the Genomes into different Species.
 */

package uoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Species {
	private ArrayList<Genome> genomes = new ArrayList<>();
	private Genome representative;
	private double averageFitness;
	private double topFitness;
	private int genSteps;
	
	/**
	 * Builder of the class
	 * @param representative
	 */
	public Species(Genome representative) {
		this.representative = representative;
		this.averageFitness = 0;
		this.topFitness = 0;
		this.genSteps = 0;
		
	}
	
	/**
	 * Getter of genomes.
	 * @return genomes
	 */
	public List<Genome> getGenomesList() {
		return this.genomes;
	}
	
	/**
	 * Getter of representative
	 * @return representative
	 */
	public Genome getRepresentative() {
		return this.representative;
	}
	
	/**
	 * Getter of averageFitness.
	 * @return averageFitness
	 */
	public double getAverageFitness() {
		return this.averageFitness;
	}
	
	/**
	 * Setter of averageFitness
	 * @param averageFitness
	 */
	public void setAverageFitness(double averageFitness) {
		this.averageFitness = averageFitness;
	}
	
	/**
	 * Getter of topFitness
	 * @return topFitness
	 */
	public double getTopFitness() {
		return this.topFitness;
	}
	
	/**
	 * Setter of topFitness
	 * @param fitness
	 */
	public void setTopFitness(double fitness) {
		this.topFitness = fitness;
	}
	
	/**
	 * Getter of genSteps.
	 * @return genSteps
	 */
	public int getGenSteps() {
		return this.genSteps;
	}
	
	/**
	 * Setter of genSteps
	 * @param step
	 */
	public void setGenSteps(int step) {
		this.genSteps = step;
	}
	
	/**
	 * Method to add a Genome to the genomes list of the Species.
	 * @param genome
	 */
	public void addGenome(Genome genome) {
		this.getGenomesList().add(genome);
	}
	
	/**
	 * Method to calculate and set the averageFitness of this Species.
	 */
	public void calculateAverageFitness() {
		double averageFitness = 0;
		for (int i = 0; i < this.getGenomesList().size(); i++) {
			averageFitness = averageFitness + this.getGenomesList().get(i).getFitness();
		}
		this.averageFitness = (averageFitness / this.getGenomesList().size());
	}
	
	/**
	 * Method to sortSpecies with the Genomes with best fitness values (lower) first.
	 */
	public void sortSpecies() {
		Collections.sort(this.getGenomesList(), (g1, g2) -> Double.valueOf(g1.getFitness()).compareTo(Double.valueOf(g2.getFitness())));
	}
	
	/**
	 * Method to calculate the number of Genomes to survive in a Species.
	 * @return number of Genomes to survive of this Species.
	 */
	public int GenomesToSurvive() {
		return (int)Math.ceil(NEAT_UOC.survivalThreshold * this.getGenomesList().size());
	}
	
	/**
	 * Method to prune the Genomes with worst fitness values of this Species.
	 */
	public void pruneSpecies() {
		this.sortSpecies();
		int survivalNumber = this.GenomesToSurvive();
		int actualSize = getGenomesList().size();
		for (int i = survivalNumber; i < actualSize; i++) {
			this.getGenomesList().remove(survivalNumber);
		}
	}
	
/*	public void newTopFitness() {
		Iterator itr = genomes.iterator();
		while (itr.hasNext()) {
			Genome newGenome = (Genome) itr.next();
			if (newGenome.getFitness() > this.topFitness) {
				this.topFitness = newGenome.getFitness();
				this.genSteps = 0;
			}
		}
	}*/
	
	/**
	 * Auxiliar method to list the fitness values of the genomes of this Species.
	 */
	public void listSortedSpecies() {
		this.sortSpecies();
		Iterator<Genome> itr = this.getGenomesList().iterator();
		while(itr.hasNext()) {
			Genome genomeToPrint = itr.next();
			System.out.println(genomeToPrint.getFitness());
		}
	}
	
	/**
	 * Method to set the fitness and the representative of this Species.
	 */
	public void newSortedTopFitness() {
		if (getGenomesList().get(0).getFitness() < this.topFitness) {
			this.topFitness = genomes.get(0).getFitness();
			this.genSteps = 0;
			this.representative = getGenomesList().get(0);
		}
	}

	
}
