/*
	Sidd Lakshman
	Autocomplete test version 3 
	In this final version I have opted to implement array lists from an online suggestion
*/

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class ac_test {
	
	//Initial values that only refresh when program restarts fully therefore static
	static char userLetter;
	static int numOfRuns = 0;
	static double startTime, endTime, totalTime;
	static String userWord, listOfPredictions = "";
	static DLB dictionaryDLB = new DLB();
	static DLB userHistoryDLB = new DLB();
	static ArrayList<String> dictionaryPredictions, pastUserPredictions = new ArrayList<String>(), oldPredictions = new ArrayList<String>(), currentPredictions = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		
		//fill dictionary trie and file trie with words/past history
		File dictionary = new File("dictionary.txt");
		File userHistory = new File("user_history.txt");
		
		Scanner dictionaryScanner = new Scanner(dictionary);
		while (dictionaryScanner.hasNextLine()) 
			dictionaryDLB.add(dictionaryScanner.nextLine());
		
		try {
			Scanner userScanner = new Scanner(userHistory);
			while (userScanner.hasNextLine())
				userHistoryDLB.add(userScanner.nextLine());
		} catch (FileNotFoundException e) {
			try {
				FileWriter fileWriter = new FileWriter(userHistory);
				fileWriter.close();
			} catch (IOException e2) {}			//e2 is placeholder
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter your first character: ");
		
		//chose a do-while loop over a while so that the code runs once regardless of 
		//the initial condition requirement from a regular while loop
		do {
			userWord = "";
			if (numOfRuns > 0) 
				System.out.print("Enter first character of the next word: ");
			
			userLetter = scanner.nextLine().charAt(0);
			oldPredictions.clear();
			dictionaryDLB.clearPredictions();
			userHistoryDLB.clearPredictions();			//reset at top of loop
			
			while (userLetter >= 'A') {
				
				//initial conditions for time and iterators used later
				currentPredictions.clear();
				userWord += userLetter;
				int j = 0;		
				listOfPredictions = "";
				startTime = System.nanoTime();
				
				//obtain predictions from dictionary and user history DLB's
				dictionaryPredictions = dictionaryDLB.Predictions(userLetter);
				pastUserPredictions = userHistoryDLB.Predictions(userLetter);
				
				//fill listOfPredictions string with words from user past
				for (int i = 0; i < pastUserPredictions.size(); i++) {
					currentPredictions.add(pastUserPredictions.get(i));
					listOfPredictions += "(" + (i+1) + ")" + pastUserPredictions.get(i) + "\t";
				}
				
				//fill remaining words from listOfPredictions from dictionary
				for (int i = pastUserPredictions.size(); i < 5; i++) {
					while (dictionaryPredictions.size() > j && (oldPredictions.contains(dictionaryPredictions.get(j)) || pastUserPredictions.contains(dictionaryPredictions.get(j))))
						j++;
					if (dictionaryPredictions.size() > j) {
						oldPredictions.add(dictionaryPredictions.get(j));
						currentPredictions.add(dictionaryPredictions.get(j));
						listOfPredictions += "(" + (i+1) + ")" + dictionaryPredictions.get(j) + "\t";
						j++;
					}
				}
				
				//if listOfPredictions is empty then no prediction found => new word
				endTime = System.nanoTime();
				if (listOfPredictions.equals(""))
					listOfPredictions += "No predictions found, continue typing until finished then enter '$'";
				
				System.out.println((endTime - startTime)/1000000000 + " s");
				System.out.print(listOfPredictions);
				totalTime += (endTime - startTime)/1000000000;
				System.out.print("\nEnter the next character: ");			//takes next input
				userLetter = scanner.nextLine().charAt(0);
				numOfRuns++;
			}
			
			//if they choose the word from prediction
			if (userLetter > '0' && userLetter < '6') {
				System.out.println("\nWORD COMPLETED:\t" + currentPredictions.get(userLetter - '1') + "\n");
				if (!pastUserPredictions.contains(currentPredictions.get(userLetter - '1'))) {
					userHistoryDLB.add(currentPredictions.get(userLetter - '1'));
					FileWriter fw = new FileWriter(userHistory);
					fw.write(currentPredictions.get(userLetter - '1') + "\n");
					fw.close();					//write into file, we do -1 since it is one less than what they choose
				}
			} else if (userLetter == '$') {									//if they finished word 
				System.out.println("\nWORD COMPLETED:\t" + userWord + "\n");
				if (!pastUserPredictions.contains(userWord)) {
					userHistoryDLB.add(userWord);
					FileWriter fw = new FileWriter(userHistory);
					fw.write(userWord + "\n");
					fw.close();
				}
			}
		} while (userLetter != '!');
		System.out.print("\nAverage time:\t" + totalTime/numOfRuns + " s\nBye!");
		
	}
	
}