/**
 * Class that represents a Connection between two nodes. In the NEAT algorithm is considered as a Gene
 * and it is in charge of the management of historical marks through the innovation number
 * @author Joan-Antoni Vilaseca
 */

package uoc;

public class ConnectionGene {

		private int innovationNumber;
		private NodeGene inNode;
		private NodeGene outNode;
		private double weight;
		private boolean enableBit;
		static int innovationCount = 0;
		
		/**
		 * Builder of the class
		 * @param in
		 * @param out
		 * @param weight
		 * @param enabled
		 */
		public ConnectionGene (NodeGene in, NodeGene out, double weight, boolean enabled) {
			innovationCount++;
			this.innovationNumber = innovationCount;
			this.inNode = in;
			this.outNode = out;
			this.weight = weight;
			this.enableBit = enabled;
			out.getIncomingNodes().add(in);
			out.getIncomingConnections().add(this);
		}
		
		/**
		 * Getter of innovationNumber.
		 * @return innovationNumber
		 */
		public int getInnovationNumber() {
			return innovationNumber;
		}

		/**
		 * Setter of innovationNumber.
		 * @param innovationNumber
		 */
		public void setInnovationNumber(int innovationNumber) {
			this.innovationNumber = innovationNumber;
		}

		/**
		 * Getter of the inNode.
		 * @return inNode
		 */
		public NodeGene getInNode() {
			return inNode;
		}

		/**
		 * Getter of the outNode.
		 * @return outNode
		 */
		public NodeGene getOutNode() {
			return outNode;
		}

		/**
		 * Getter of weight.
		 * @return weight
		 */
		public double getWeight() {
			return weight;
		}

		/**
		 * Setter of weight
		 * @param weight
		 */
		public void setWeight(double weight) {
			this.weight = weight;
		}

		/**
		 * Method to know if the ConnectionGene is enabled.
		 * @return boolean that indicates if the ConnectionGene is enabled
		 */
		public boolean isEnabled() {
			return enableBit;
		}

		/**
		 * Setter of enableBit.
		 * @param enabled
		 */
		public void setEnableBit(boolean enabled) {
			this.enableBit = enabled;
		}
}
