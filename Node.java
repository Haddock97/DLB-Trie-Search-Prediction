/*
	Sidd Lakshman
	Node Class
*/

import java.util.ArrayList;

public class Node {
	
	char data;
	Node siblingNode = null;
	Node childNode = null;
	String word;
	
	public Node(char letter, String word) {
		data = letter;
		this.word = word.substring(0, word.length() - 1);
	}
	
	//recursive prediction call from going through children or sibling nodes
	public ArrayList<String> Predict() {
		
		ArrayList<String> prediction = new ArrayList<String>();
		
		if (data == '^') 
			prediction.add(word);
		
		if (childNode != null)
			prediction.addAll(childNode.Predict());
		
		if (siblingNode != null)
			prediction.addAll(siblingNode.Predict());
		
		return prediction;
	}
}