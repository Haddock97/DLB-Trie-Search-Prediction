/*
	Sidd Lakshman
	DLB class attempt 2
*/

import java.util.ArrayList;

public class DLB {
	
	private final char terminate = '^';		//this is our character to indicate value in our DLB
	Node root;
	Node search;
	
	//constructor - empty since we will read from file using add
	public DLB() {
	}
	
	
	//adds a string to DLB
	public void add(String word) {
		
		word += terminate;		//add $ to word - our terminating character
		
		if (root == null) {
			this.addRootWord(word);
		} else {
			
			Node current = root;
			for (int i = 0; i < word.length(); i++) {
				if (current.data == word.charAt(i) || (current.childNode == null && current.data != terminate)) {
					if (current.childNode == null) {
						current.childNode = new Node(word.charAt(i), word);		//add node with letter
						current = current.childNode;
					} else {
						current = current.childNode;
					}
				} else {
					while(current.siblingNode != null && current.data != word.charAt(i)) 
						current = current.siblingNode;		//shift to null sibling 
					
					if (current.data == word.charAt(i)) {
						current = current.childNode;		//shift to child node if letter doesnt match current
					} else if (current.siblingNode == null) {
						current.siblingNode = new Node(word.charAt(i), word);
						current = current.siblingNode;		//add letter to and shift to sibling
					}
				}
			}
		}
	}
	
	//need this in case root is null to add an initial word
	public void addRootWord(String initialWord) {
		root = new Node(initialWord.charAt(0), initialWord);	
		Node current = root;
		for (int i = 1; i < initialWord.length(); i++) {
			current.childNode = new Node(initialWord.charAt(i), initialWord);
			current = current.childNode;
		}
	}
	
	
	//obtain predictions through a call to predict from the node class to enter a recursive method
	public ArrayList<String> Predictions(char letter) {
		
		if (root == null)							//we will send an empty array list if its null root
			return new ArrayList<String>();
		
		while (search.siblingNode != null  && search.data != letter)
			search = search.siblingNode;		//search until null sibling is discovered
		
		if (search.data == letter) {
			search = search.childNode;			//if letter matches then send the child.predict
		} else if (search.data != letter) {
			return new ArrayList<String>();
		}
		return search.Predict();
		
	}
	
	//clear predictions
	public void clearPredictions() {
		search = root;
	}
}