/**
 * Class that represents a collection of Genomes split into Species
 * @author Joan-Antoni Vilaseca
 */

package uoc;

import java.util.ArrayList;
import java.util.Iterator;


public class Population {
	private int generation;
	private ArrayList<Species> speciesList = new ArrayList<>();
	
	/**
	 * Builder of the class.
	 * @param inputNodes
	 * @param outputNodes
	 */
	public Population(int inputNodes, int outputNodes) {
		this.generation = 0;
		for (int i = 0; i < NEAT_UOC.maxGenomes; i++) {
			Genome newGenome = new Genome(inputNodes, outputNodes);
			if (i < NEAT_UOC.maxInitialSpecies) {
				this.speciesList.add(new Species (newGenome));
				this.speciesList.get(i).getGenomesList().add(newGenome);
			} else {
				this.speciesList.get(i % NEAT_UOC.maxInitialSpecies).getGenomesList().add(newGenome);
			}
		}
		this.generation = 0;
	}
	
	/**
	 * Getter of speciesList.
	 * @return speciesList
	 */
	public ArrayList<Species> getSpeciesList() {
		return this.speciesList;
	}
	
	/**
	 * Setter of speciesList.
	 * @param speciesList
	 */
	public void setSpeciesList(ArrayList<Species> speciesList) {
		this.speciesList = speciesList;
	}
	
	/**
	 * Getter of generation.
	 * @return generation
	 */
	public int getGeneration() {
		return this.generation;
	}
	
	/**
	 * Setter of generation.
	 * @param newGeneration
	 */
	public void setGeneration(int newGeneration) {
		this.generation = newGeneration;
	}
	
	/**
	 * Method to mutate all the Genomes of this Population.
	 */
	public void mutate() {
		for (int i = 0; i < speciesList.size(); i++) {
			for (int j=0; j < speciesList.get(i).getGenomesList().size(); j++) {
				Genome genomeMutated = speciesList.get(i).getGenomesList().get(j).mutate();
				speciesList.get(i).getGenomesList().remove(j);
				speciesList.get(i).getGenomesList().add(j, genomeMutated);
			}
		}
	}
	
	/**
	 * Method that reproduce the Population prunning the worst fitness of each Species and
	 * completing theses Species with new Genomes that are child of random parent of this Species.
	 */
	public void reproduce() {
		Iterator<Species> itr = speciesList.iterator();
		while (itr.hasNext()) {
			Species oldSpecies = itr.next();
			ArrayList<Genome> newGenomesList = new ArrayList<>();
			int survivorsNumber = oldSpecies.GenomesToSurvive();
			int genomesSpeciesNumber = oldSpecies.getGenomesList().size();
			oldSpecies.pruneSpecies();
			for (int i =  survivorsNumber; i < genomesSpeciesNumber; i++) {
				Genome g1 = Random.random(new ArrayList<>(oldSpecies.getGenomesList()));
				Genome g2 = Random.random(new ArrayList<>(oldSpecies.getGenomesList()));
				Genome newGenome = g1.crossOver(g1, g2);
				newGenomesList.add(newGenome);
	//			oldSpecies.getGenomesList().add(newGenome);
			}
			for (int j=0; j<newGenomesList.size(); j++) {
				oldSpecies.getGenomesList().add(newGenomesList.get(j));
			}
		}
		
	}	
	
	/**
	 * Method to classify each Genome in a Species if the distance with its representative
	 * is inside a speciationThreshold or creating a new Species it's not.
	 */
	public void speciation() {
		this.setGeneration(this.getGeneration()+1);
		ArrayList<Species> newSpeciesList = new ArrayList<>();
		Iterator<Species> itr0 = getSpeciesList().iterator();
		while (itr0.hasNext()) {
			Species speciesToCopy = itr0.next();
			newSpeciesList.add(new Species(speciesToCopy.getRepresentative()));
		}
		Iterator<Species> itr = getSpeciesList().iterator();
		while (itr.hasNext()) {
			Species seqSpecies = itr.next();
			Iterator<Genome> itrGen = seqSpecies.getGenomesList().iterator();
			while (itrGen.hasNext()) {
				boolean found = false;
				int i=0;
				Genome speciatingGenome = itrGen.next();
		//		speciatingGenome.speciate()
				while (i<this.getSpeciesList().size() && found != true) {
					if (speciatingGenome.distanceGenomes(this.getSpeciesList().get(i).getRepresentative())
							< NEAT_UOC.speciationThreshold) {
						newSpeciesList.get(i).getGenomesList().add(speciatingGenome);
						found = true;
					}
					i++;
				}
				if (!found) {
					newSpeciesList.add(new Species(speciatingGenome));
					newSpeciesList.get(newSpeciesList.size()-1).addGenome(speciatingGenome);
				}
	/*			Iterator<Species> itrSpec = speciesList.iterator();
				while (itrSpec.hasNext() && !found) {
					Species calculateDistanceToSpecies = itr.next();
					if (speciatingGenome.distanceGenomes(calculateDistanceToSpecies.getRepresentative()) < NEAT_UOC.speciationThreshold) {
						calculateDistanceToSpecies.getGenomesList().add(speciatingGenome);
						seqSpecies.getGenomesList().remove(speciatingGenome);
						found = true;
					}*/
				}
	/*			if(!found) {
					this.getSpeciesList().add(new Species(speciatingGenome));
				}
			}*/

		}
		
	// Delete empty Species
	Iterator<Species> itrRemove = newSpeciesList.iterator();
	while (itrRemove.hasNext()) {
			Species speciesToRemove = itrRemove.next();
			if(speciesToRemove.getGenomesList().size() == 0) {
				itrRemove.remove();
			}
	}
	// Return newSpeciesList
	this.setSpeciesList(newSpeciesList);
	}
	
	/**
	 * Method to train all the Genomes of the Population with the values of an Experiment.
	 * @param e
	 */
	public void train(Experiment e) {
		Iterator<Species> itr = getSpeciesList().iterator();
		while (itr.hasNext()) {
			Species seqSpecies = itr.next();
			Iterator<Genome> itrGen = seqSpecies.getGenomesList().iterator();
			while (itrGen.hasNext()) {
				Genome activatingGenome = itrGen.next();
				e.setOutputs(activatingGenome.activate(e));
				activatingGenome.calculateFitness(e);
			}
		}
	}
	
	/** 
	 * Method to find the Genome with the best fitness value for an Experiment.
	 * @param e
	 * @return Genome with the best fitness values for an Experiment
	 */
	public Genome bestGenome(Experiment e) {
		Genome bestGenome = null;
		double bestFitness = 0;
		for (int i=0; i < e.getOutputs().size(); i++) {
			bestFitness = bestFitness + Math.pow(e.getExpectedOutputs().get(i), 2);
		}
		bestFitness = (Math.sqrt(bestFitness));
		Iterator<Species> itr = getSpeciesList().iterator();
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
	 * Method to predict an output value for a trained network.
	 * @param e
	 * @return predicted output
	 */
	public ArrayList<Double> predict(Experiment e) {
		ArrayList<Double> predictionOutputs = new ArrayList<>();
		predictionOutputs = this.bestGenome(e).activate(e);
		return predictionOutputs;
	}
	
	/**
	 * Auxiliar method to count the number of Genomes of a Population.
	 * @return number of Genomes of the population
	 */
	public int countGenomes() {
		int i = 0;
		Iterator<Species> itr = getSpeciesList().iterator();
		while (itr.hasNext()) {
			Species seqSpecies = itr.next();
			Iterator<Genome> itrGen = seqSpecies.getGenomesList().iterator();
			while (itrGen.hasNext()) {
				i++;
				itrGen.next();
			}
		}
		return i;
	}
	
	/**
	 * Auxiliar method to count the number of Genomes of a list of Species.
	 * @param speciesToCount
	 * @return number of Genomes of a list of Species
	 */
	public int countGenomes(ArrayList<Species> speciesToCount) {
		int i = 0;
		Iterator<Species> itr = speciesToCount.iterator();
		while (itr.hasNext()) {
			Species seqSpecies = itr.next();
			Iterator<Genome> itrGen = seqSpecies.getGenomesList().iterator();
			while (itrGen.hasNext()) {
				i++;
				itrGen.next();
			}
		}
		return i;
	}
}
