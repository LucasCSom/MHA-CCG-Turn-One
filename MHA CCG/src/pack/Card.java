package pack;

public class Card {
	String name;
	String type;
	boolean playable;
	int cost;
	int check;
	int handSize;
	boolean tapped = false;
	
	public Card(String name) {
		this.name = name;
	}
	
	public Card(String name, int handSize) {
		this.name = name;
		this.handSize = handSize;
	}
}
