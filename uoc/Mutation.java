/**
 * Abstract class that all mutations must inherit
 * @author Joan-Antoni Vilaseca
 */

package uoc;

public abstract class Mutation {

	protected final Genome genome;

	/**
	 * Builder method of the class.
	 * @param genome
	 */
	public Mutation(Genome genome) {
		this.genome = genome;
	}

	/**
	 * Getter method of Genome.
	 * @return genome
	 */
	public Genome getGenome() {
		return this.genome;
	}

}
