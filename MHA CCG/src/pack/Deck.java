package pack;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	ArrayList<Card> deckList = new ArrayList<>();
	ArrayList<Card> hand = new ArrayList<>();
	ArrayList<Card> characters = new ArrayList<>();
	ArrayList<Card> mulligan = new ArrayList<>();
	ArrayList<Card> stage = new ArrayList<>();
	int handSize;
	
	public Deck() {
		
	}
	
	public void shuffle() {
		Collections.shuffle(deckList);
	}
	
	public void addCard(Card card) {
		deckList.add(card);
	}
	
	public void refillHand() {
		int refill = this.handSize - this.hand.size();
		for (int i = 0; i < refill; i++) {
			this.hand.add(this.deckList.get(0));
			this.deckList.remove(0);
		}
	}
	
	public void printHand() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println((i+1) + ": " + "Cost: " + hand.get(i).cost + " " + hand.get(i).name);
		}
	}
	
	public void printHand(String type) {
		for (int i = 0; i < hand.size(); i++) {
			if (this.hand.get(i).type.compareToIgnoreCase(type) == 0) {
				System.out.println((i+1) + ": " + "Cost: " + hand.get(i).cost + " " + hand.get(i).name);
			}	
		}
	}
	
	public void sortHand() {
		this.hand.sort((o1, o2) -> o1.name.compareTo(o2.name));
	}
	
	public boolean characterListIteration(String name) {
		for (int i = 0; i < this.characters.size(); i++) {
			if (this.characters.get(i).name.compareTo(name) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public void removeMainCharacterFromDeck() {
		int tracker = 0;
		for (int i = 0; i < this.deckList.size(); i++) {
			if (this.deckList.get(i).type.compareTo("Character") == 0) {
				tracker++;
				if (tracker == 1) {
					this.deckList.remove(i);
					break;
				}
			}
		}
	}
	
	public void playCard(int num) {
		
	}
	
	public void playCard(String name) {
		for (int i = 0; i < this.hand.size(); i++) {
			if (this.hand.get(i).name.compareToIgnoreCase(name) == 0) {
				if (this.hand.get(i).type.compareToIgnoreCase("Foundation") == 0) {
					this.stage.add(this.hand.get(i));
				}
				this.hand.remove(i);
				break;
			}
		}
	}
	
	public void playCardFree(int num) {
		if (this.hand.get(num).type.compareToIgnoreCase("Foundation") == 0) {
			this.stage.add(this.hand.get(num));
		}
		this.hand.remove(num);
	}
	
	
	public int numOfFoundationsInHand() {
		int tracker = 0;
		for (int i = 0; i < this.hand.size(); i++) {
			if (this.hand.get(i).type.compareToIgnoreCase("Foundation") == 0) {
				tracker++;
			}
		}
		return tracker;
	}
	
	public void setHandSize(int size) {
		this.handSize = size;
	}
}
