package pack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Deck deck = readUsingBufferedReader();
		Scanner input = new Scanner(System.in);
		
		System.out.println("What is your character's hand size?");
		int handSize = input.nextInt();
		ArrayList<Card> hand = deck.drawHand(handSize);
		hand.sort((o1 , o2) -> o1.name.compareTo(o2.name));
		System.out.println("Your hand is: ");
		for (int i = 0; i < handSize; i++) {
			System.out.println(i + ": " + hand.get(i).name);
		}
		
		boolean gameOn = true;
		while(gameOn) {
			System.out.print("Which card would you like to play?");
			gameOn = false;
		}
	}
	
	private static Deck readUsingBufferedReader() throws IOException {
		Deck deck = new Deck();
	    File file = new File("Decklist/Deck.txt");
	    FileReader fr = new FileReader(file);
	    BufferedReader br = new BufferedReader(fr);
	    String line;
	    while ((line = br.readLine()) != null) {
	        String[] parts = line.split(";");
	        Card card = new Card(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
	        int i = Character.getNumericValue(parts[0].charAt(0));
	        for (int tracker = 0 ; tracker < i; tracker++) {
	        	//System.out.println(card.name + " " + card.cost + " " + card.check);
	        	deck.addCard(card);
	        }
	    }
	    br.close();
	    fr.close();
	    deck.shuffle();
	    return deck;
	}

	
}
