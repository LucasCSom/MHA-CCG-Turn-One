package pack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		
		Scanner input = new Scanner(System.in);
		File folder = new File("Decklists");
		File[] listOfFiles = folder.listFiles();
		
		System.out.println("Which deck would you like to use?");
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println((i + 1) + ": " + listOfFiles[i].getName());
			}
		}
		int deckNumber = input.nextInt() - 1;
		System.out.println(listOfFiles[deckNumber].getName());
		Deck deck = readUsingBufferedReader(listOfFiles[deckNumber].getName());
		
		boolean gameOn = true;
		while(gameOn) {
			gameOn = mainMenu(deck);
		}
	}
	
	private static boolean mainMenu(Deck deck) {
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("What do you want to do? (Use numbers to input your choice)");
		System.out.println("1: First Turn Play");
		System.out.println("2: First Turn (Going Second) Play");
		System.out.println("3: Deck Stats");
		System.out.println("0: Quit");
		int userChoice = input.nextInt();
		
		if (userChoice == 1) {
			playFirstTurn(deck);
			return true;
		} else if (userChoice == 2) {
			playFirstTurnGoingSecond(deck);
			return true;
		} else if (userChoice == 3) {
			deckStats(deck);
			return true;
		} else if (userChoice == 0) {
			System.out.println("Thank you!");
			return false;
		} else if (userChoice == 999) {
			System.out.println("Debug Stuff: ");
			for (int i = 0; i < deck.characters.size(); i++) {
				System.out.println(" " + deck.characters.get(i).name);
			}
			return true;
		} else {
			System.out.println("Sorry I didn't understand that!");
			return true;
		}
		
	}
	
	private static Deck readUsingBufferedReader(String deckName) throws IOException {
		Deck deck = new Deck();
		File file = new File("Decklists/" + deckName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			String name = line.substring(2, line.length());
			Card card = new Card(name);
			int i = Character.getNumericValue(line.charAt(0));
			for (int tracker = 0; tracker < i; tracker++) {
				//System.out.println(card.name + " " + card.cost);
				deck.addCard(card);
			}
		}
		br.close();
		fr.close();
		populateCardData(deck);
		deck.shuffle();
		return deck;
	}
	
	private static void populateCardData(Deck deck) throws IOException {
		File file = new File("Database/MHA CCG Data.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			String[] stats = line.split(",");
			for (int i = 0; i < deck.deckList.size(); i++) {
				
				if (stats[0].compareToIgnoreCase(deck.deckList.get(i).name) == 0) {
					deck.deckList.get(i).check = Integer.parseInt(stats[2]);
					deck.deckList.get(i).cost = Integer.parseInt(stats[3]);
					if (stats[1].equalsIgnoreCase("Foundation") || 
							stats[1].equalsIgnoreCase("Action") ||
							stats[1].equalsIgnoreCase("Asset") ||
							stats[1].equalsIgnoreCase("Character")) {
						deck.deckList.get(i).playable = true;
					}
					if (stats[1].equalsIgnoreCase("Foundation")) {
						deck.deckList.get(i).type = "Foundation";
					}
					if (stats[1].equalsIgnoreCase("Action")) {
						deck.deckList.get(i).type = "Action";
					}
					if (stats[1].equalsIgnoreCase("Attack")) {
						deck.deckList.get(i).type = "Attack";
					}
					if (stats[1].equalsIgnoreCase("Character")) {
						deck.deckList.get(i).type = "Character";
						
						deck.deckList.get(i).handSize = Integer.parseInt(stats[4]);
						if (!deck.characterListIteration(deck.deckList.get(i).name)) {
							deck.characters.add(deck.deckList.get(i));
						}
					}
					if (stats[1].equalsIgnoreCase("Asset")) {
						deck.deckList.get(i).type = "Asset";
					}
				}
				
			}
		}
		br.close();
		fr.close();
		deck.removeMainCharacterFromDeck();
	}
	
	private static void playFirstTurn(Deck deck) {
		
		Scanner input = new Scanner(System.in);
		Deck playDeck = deck;
		
		if (playDeck.characters.size() == 0) {
			System.out.println("No hero detected in your deck! What hand size would you like to use?");
			int userHandSize = input.nextInt();
			Card testCharacter = new Card("Stand in!", userHandSize);
			
			playDeck.characters.add(testCharacter);
		} if (playDeck.characters.size() == 1) {
			System.out.println("I've detected " + playDeck.characters.get(0).name + " as the only character in your deck. I'll use their hand size of " + playDeck.characters.get(0).handSize);
		} if (playDeck.characters.size() > 1) {
			System.out.println("I've detected more than one character in your deck! Which one would you like to use for hand size?");
			for (int i = 0; i < playDeck.characters.size(); i++) {
				System.out.println((i+1) + ": " + playDeck.characters.get(i).name); 
			}
			int userchoice = input.nextInt();
			Card neededCard = playDeck.characters.get(userchoice);
			playDeck.characters.clear();
			playDeck.characters.add(neededCard);
		}
		
		playDeck.setHandSize(playDeck.characters.get(0).handSize);
		playDeck.refillHand();
		playDeck.hand.sort((o1, o2) -> o1.name.compareTo(o2.name));
		
		boolean gameOn = true;
		boolean checkFailed = false;
		ArrayList<Card> playedCards = new ArrayList<>();
		
		while(gameOn) {
			playDeck.printHand();
			System.out.println("Which card would you like to play? If you want to pass type 0");
			int cardPlayed = input.nextInt() - 1;
			
			if (cardPlayed >= playDeck.hand.size() || cardPlayed < -1) {
				System.out.println("Please input a number between 1 and " + playDeck.hand.size());
				continue;
			} if (cardPlayed == -1) {
				checkFailed = true;
			}
			
			if (checkFailed == false && playDeck.hand.get(cardPlayed).playable == true) {
				
				Card checkCard = deck.deckList.get(0);
				playDeck.deckList.remove(0);
				System.out.println("Check card is: " + checkCard.name + " for " + checkCard.check);
				if (playDeck.hand.get(cardPlayed).cost + playedCards.size() <= checkCard.check) {
					System.out.println("Check passed!");
					
					playedCards.add(deck.hand.get(cardPlayed));
				} else if (playDeck.hand.get(cardPlayed).cost + playedCards.size() > checkCard.check) {
					System.out.println("Check failed!");
					checkFailed = true;
				} 
				playDeck.hand.remove(cardPlayed);
				
			} else if (checkFailed == false && playDeck.hand.get(cardPlayed).playable == false) {
				System.out.print("You can't play that card this turn!");
			}
			
			if (playDeck.hand.size() == 0 || checkFailed == true) {
				System.out.println("This ends your turn. Your cards played successfully were: ");
				if (playedCards.size() == 0) {
					System.out.println("No cards were played!");
				} else if (playedCards.size() > 0) {
					for (int i = 0; i < playedCards.size(); i++) {
						if (i == 0) {
							System.out.print(playedCards.get(i).name);
						} else {
							System.out.print(", " + playedCards.get(i).name);
						}
					}
					System.out.println();
				}
				break;
			}
		}
	}
	
	public static void playFirstTurnGoingSecond(Deck deck) {
		
		Scanner input = new Scanner(System.in);
		Deck playDeck = deck;
		Deck initialDeck = deck;
		playDeck.stage.add(playDeck.characters.get(0));
		
		if (playDeck.characters.size() == 0) {
			System.out.println("No hero detected in your deck! What hand size would you like to use?");
			int userHandSize = input.nextInt();
			Card testCharacter = new Card("Stand in!", userHandSize);
			
			playDeck.characters.add(testCharacter);
		} if (playDeck.characters.size() == 1) {
			System.out.println("I've detected " + playDeck.characters.get(0).name + " as the only character in your deck. I'll use their hand size of " + playDeck.characters.get(0).handSize);
		} if (playDeck.characters.size() > 1) {
			System.out.println("I've detected more than one character in your deck! Which one would you like to use for hand size?");
			for (int i = 0; i < playDeck.characters.size(); i++) {
				System.out.println((i+1) + ": " + playDeck.characters.get(i).name); 
			}
			int userchoice = input.nextInt();
			Card neededCard = playDeck.characters.get(userchoice);
			playDeck.characters.clear();
			playDeck.characters.add(neededCard);
		}
		
		playDeck.setHandSize(playDeck.characters.get(0).handSize);
		playDeck.refillHand();
		
		boolean gameOn = true;
		boolean mulliganing = true;
		boolean checkFailed = false;
		int tracker = 0;
		ArrayList<Card> playedCards = new ArrayList<>();
		
		System.out.println("Would you like to guarantee Ready Set GO! activation if it's in your starting hand?");
		System.out.println("1: Yes");
		System.out.println("2: No");
		int userChoice = input.nextInt();
		int oppFoundations;
		if (userChoice == 1) {
			oppFoundations = 4;
		} else {
			oppFoundations = (int)(Math.random()*4) + 3;
		}
		
		System.out.println("Would you like to guarantee Ready Get Set GO! is in your starting hand?");
		System.out.println("1: Yes");
		System.out.println("2: No");
		int rsg = input.nextInt();
		if (rsg == 1) {
			for (int i = 0; i < playDeck.deckList.size(); i++) {
				if (playDeck.deckList.get(i).name.compareToIgnoreCase("Ready Get Set GO!") == 0) {
					playDeck.hand.add(playDeck.deckList.get(i));
					break;
				}
			}
		}
		
		playDeck.refillHand();
		playDeck.sortHand();

		while(mulliganing) {
			playDeck.printHand();
			System.out.println("Which cards would you like to mulligan from this hand? Enter 0 if done and 10 for all");
			int mullChoice = input.nextInt() - 1;
			if (mullChoice == 9) {
				for (int i = 0; i < playDeck.hand.size(); i++) {
					playDeck.mulligan.add(playDeck.hand.get(mullChoice));
					playDeck.hand.remove(mullChoice);
					tracker++;
				}
			} else if (mullChoice == -1) {
				break;
			} else if (mullChoice <= playDeck.hand.size() || mullChoice > -1) {
				playDeck.mulligan.add(playDeck.hand.get(mullChoice));
				playDeck.hand.remove(mullChoice);
				tracker++;
			}
			
		}
		
		if (tracker > 0) {
			for (int i = 0; i < playDeck.mulligan.size(); i++) {
				playDeck.hand.add(playDeck.deckList.get(0));
				playDeck.deckList.remove(0);
			} for (int i = 0; i < playDeck.mulligan.size(); i++) {
				playDeck.deckList.add(playDeck.mulligan.get(0));
				playDeck.mulligan.remove(0);
			}
			
			playDeck.shuffle();
			
		}
		
		for (int i = 0; i < playDeck.hand.size(); i++) {
			if (playDeck.hand.get(i).name.equalsIgnoreCase("Ready Get Set GO!")  && oppFoundations >= 4) {
				System.out.println("Ready Get Set GO! has activated! Would you like to play it?");
				System.out.println("1: Yes");
				System.out.println("2: No");
				int yesNo = input.nextInt();
				//Oh fucking christ what happens when you have 2 ready get set go's and they both activate
				if (yesNo == 1) {
					System.out.println("You check: " + playDeck.deckList.get(0).name + " for " + playDeck.deckList.get(0).check);
					if (playDeck.deckList.get(0).check <= 2) {
						System.out.println("Check failed sorry...");
					} else if (playDeck.deckList.get(0).check == 3) {
						System.out.println("You checked a 3! I will tap your character to pay for Ready Get Set GO!");
						for (int j = 0; j < 2; j++) {
							if (playDeck.numOfFoundationsInHand() > 0) {
								System.out.println("Which foundation would you like to build?");
								playDeck.printHand("Foundation");
								playDeck.playCardFree(input.nextInt() - 1);
							}
						}
					} else if (playDeck.deckList.get(0).check >= 4) {
						System.out.println("You passed the check!");
						for (int j = 0; j < 2; j++) {
							if (playDeck.numOfFoundationsInHand() > 0) {
								System.out.println("Which foundation would you like to build?");
								playDeck.printHand("Foundation");
								playDeck.playCardFree(input.nextInt() - 1);
							}
						}
					}
				} else {
					break;
				}
			}
		}
		
		playDeck.sortHand();
		playDeck.refillHand();
		
		while(gameOn) {
			//playDeck.hand.add(playDeck.deckList.get(0));
			//playDeck.deckList.remove(0);
			playDeck.printHand();
			System.out.println("Foundations in stage: " + playDeck.stage.get(0).name + playDeck.stage.get(1).name + playDeck.stage.get(2));
			
			int playerChoice = input.nextInt();
		}
	}
	
	private static void deckStats(Deck deck) {
		Scanner input = new Scanner(System.in);
		boolean checkingDeck = true;
		
		while (checkingDeck) {
			System.out.println("What do you want to check about your deck?");
			System.out.println("1: Card Type breakdown");
			System.out.println("2: Check curve");
			System.out.println("3: Difficulty curve");
			System.out.println("4: Leave to Main Menu");
			int userChoice = input.nextInt();
			if (userChoice == 1) {
				
			} if (userChoice == 2) {
				
			} if (userChoice == 3) {
				
			} if (userChoice == 4) {
				checkingDeck = false;
			}
		}
	}
	
}
