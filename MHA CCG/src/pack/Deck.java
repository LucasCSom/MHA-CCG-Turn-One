package pack;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	ArrayList<Card> deckList = new ArrayList<>();
	
	public Deck() {
		
	}
	
	public void shuffle() {
		Collections.shuffle(deckList);
	}
	
	public void addCard(Card card) {
		deckList.add(card);
	}
	
	public ArrayList<Card> drawHand(int handSize) {
		ArrayList<Card> hand = new ArrayList<>();
		for (int i = 0; i < handSize; i++) {
			hand.add(this.deckList.get(0));
			this.deckList.remove(0);
		}
		return hand;
	}
}
