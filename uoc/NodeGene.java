/**
 * Class that defines a node or neuron of a NEAT neural network
 * @author Joan-Antoni Vilaseca
 */


package uoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeGene {

	 private int id;
	 private NodeGeneState state;
	 private double activationSum;
	 private ArrayList<NodeGene> incomingNodes = new ArrayList<>();
	 private ArrayList<ConnectionGene> incomingConnections =  new ArrayList<>();
	 
	 /**
	  * Builder of the class.
	  * @param id
	  * @param state
	  */
	 public NodeGene (int id, NodeGeneState state) {
		 this.id = id;
		 this.state = state;
		 this.activationSum = 0;
	 }
	 
	 /**
	  * Getter of id
	  * @return id
	  */
	 public int getId() {
		 return id;
		 }

	 /**
	  * Setter of id.
	  * @param id
	  */
	 public void setId(int id) {
		 this.id = id;
		 }
	 
	 /**
	  * Getter of state.
	  * @return state
	  */
	 public NodeGeneState getState() {
		 return state;
	 }
	 
	 /**
	  * Setter of state.
	  * @param state
	  */
	 public void setState(NodeGeneState state) {
		 this.state = state;
	 }
	 
	 /**
	  * Getter of activationSum.
	  * @return activationSum
	  */
	 public double getActivationSum() {
		 return this.activationSum;
	 }
	 
	 /**
	  * Setter of activationSum.
	  * @param activationValue
	  */
	 public void setActivationSum(double activationValue) {
		 this.activationSum = activationValue;
	 }
	 
	 /**
	  * Getter of incomingConnections.
	  * @return incomingConnections
	  */
	 public List<ConnectionGene> getIncomingConnections() {
		 return incomingConnections;
	 }
	 
	 /**
	  * Getter of incomingNodes.
	  * @return incomingNodes
	  */
	 public List<NodeGene> getIncomingNodes() {
		 return incomingNodes;
	 }
	 
	 /**
	  * Method to add an incoming ConnectionGene to the list of incomingConnections.
	  * @param incomingNode
	  */
	 public void addIncomingConnectionNode(ConnectionGene incomingNode) {
		 incomingConnections.add(incomingNode);
	 }
	 
	 /**
	  * Method to remove an incoming ConnectionGene from the list of incomingConnections.
	  * @param incomingNode
	  */
	 public void deleteIncomingConnectionNode(ConnectionGene incomingNode) {
		 incomingConnections.remove(incomingNode);
	 }
	 
	 /**
	  * Method to add an incoming NodeGene to the list of incomingNodes.
	  * @param newNode
	  */
	 public void addIncomingNode(NodeGene newNode) {
		 incomingNodes.add(newNode);		
	 }
	 
	 /**
	  * Method to delete an incoming NodeGene from the list of incomingNodes.
	  * @param incomingNode
	  */
	 public void deleteIncomingNode(NodeGene incomingNode) {
		 incomingNodes.remove(incomingNode);
	 }
	 
	 /**
	  * Recursive method to calculate the activationSum of a node during an experiment.
	  * The recursivity stops when the method reaches an input node or a node already visited
	  * in a cyclic path of the network. If it finds a cycle the repeated node doesn't sum up
	  * his activation values twice.
	  * @param e
	  * @param visited
	  * @return activationSum of the node
	  */
	 public double activateNode(Experiment e, ArrayList<NodeGene> visited) {
		 this.setActivationSum(0);
		 if (this.getState() != NodeGeneState.input && !this.isCyclic(visited)) {
			Iterator<ConnectionGene> conItr = this.getIncomingConnections().iterator();
			while(conItr.hasNext()) {
				ConnectionGene inConnection = conItr.next();
				visited.add(inConnection.getInNode());
				this.setActivationSum(this.getActivationSum() + inConnection.getWeight() * inConnection.getInNode().activateNode(e, visited));
			}
				
		 } else {
			if (this.getState() != NodeGeneState.input) {
				this.setActivationSum(0);
			} else {
				this.setActivationSum(e.getInputs().get(this.getId()));
				
			}
		 }
		 return  this.getActivationSum();
	 }
	 
	 /**
	  * Method to check if a list of nodes is cyclic.
	  * @param visited
	  * @return true if it's cyclic or false if it's not
	  */
	 public boolean isCyclic(ArrayList<NodeGene> visited) {
	    if (visited.contains(this)) {
	    	return true;	
	    } else {
	    	return false;
	    }
	 }
	 
	 /**
	  * Method to check if two NodeGenes are equals.
	  * @param nodeGene2
	  * @return true if they are equals or false if they are not equals
	  */
	 public boolean equals(NodeGene nodeGene2) {
		 if (nodeGene2 == null) {
			 return false;
		 }
		 if (nodeGene2.getId() == this.getId()) {
			 return true;
		 } else return false;
		 
	 }

}
