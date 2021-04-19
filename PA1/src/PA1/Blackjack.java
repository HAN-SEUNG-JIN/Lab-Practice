package PA1;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
	public static void main(String[] args) {
		int seed=Integer.parseInt(args[0]);
		int num=Integer.parseInt(args[1]);
		
		Deck deck=new Deck(); 
		deck.shuffle(seed); 
		 
		Hand[] players=new Hand[num+1];
		for(int i=0;i<num+1;i++) {
			if(i==0) 
				players[i]=new Player();
			else if(i==num)
				players[i]=new House();
			else
				players[i]=new Computer();
		}
		
		for(int i=0;i<2*(num+1);i++) {
			players[i%(num+1)].set_card(deck.dealCard());
		}
		
		((House)players[num]).init_state(1);
		for(int i=0;i<num;i++) {
			if(i==0)
				((Player)players[i]).init_state(0);
			else
				((Computer)players[i]).init_state(i+1);
		}
		
		if(players[num].get_sum()==21) {
			System.out.print("\n--- Game Results ---\n");
			((House)players[num]).init_state(0);
			for(int i=0;i<num;i++) {
				if(i==0) {
					((Player)players[i]).result(players[num].get_sum());
					((Player)players[i]).init_state(0);
				}
				else {
					((Computer)players[i]).result(players[num].get_sum());
					((Computer)players[i]).init_state(i+1);
				}
			}
			return;
		}
		
		for(int i=0;i<num+1;i++) {
			if(i==0) {
				System.out.print("\n--- Player1 turn ---\n");
				((Player)players[i]).init_state(0);
				while(players[i].get_sum()<=21) {
					Scanner sc= new Scanner(System.in);
					String input=sc.nextLine();
					if(input.compareTo("Hit")==0) {
						players[i].set_card(deck.dealCard());
						((Player)players[i]).init_state(0);
					}
					else {
						((Player)players[i]).init_state(0);
						break;
					}
				}
				continue;
			}
			else if(i==num) {
				System.out.print("\n--- House turn ---\n");
				((House)players[i]).init_state(0);
				while(players[i].get_sum()<=21) {
					if(players[i].get_sum()<=16) {
						System.out.print("Hit\n");
						players[i].set_card(deck.dealCard());
						((House)players[i]).init_state(0);
					}
					else {
						System.out.print("Stand\n");
						((House)players[i]).init_state(0);
						break;
					}
				}
				continue;
			}
			else {
				System.out.print("\n--- Player"+(i+1)+" turn ---\n");
				((Computer)players[i]).init_state(i+1);
				while(players[i].get_sum()<=21) {
					if(players[i].get_sum()<14) {
						System.out.print("Hit\n");
						players[i].set_card(deck.dealCard());
						((Computer)players[i]).init_state(i+1);
					}
					else if(players[i].get_sum()>17) {
						System.out.print("Stand\n");
						((Computer)players[i]).init_state(i+1);
						break;
					}
					else {
						Random random=new Random();
						int is_hit=(int)(random.nextInt(2));
						if(is_hit==1) {
							System.out.print("Hit\n");
							players[i].set_card(deck.dealCard());
							((Computer)players[i]).init_state(i+1);
						}
						else {
						System.out.print("Stand\n");
							((Computer)players[i]).init_state(i+1);
							break;
						}
					}
				}
			}
		}
		
		System.out.print("\n--- Game Results ---\n");
		((House)players[num]).init_state(0);
		for(int i=0;i<num;i++) {
			if(i==0) {
				((Player)players[i]).result(players[num].get_sum());
				((Player)players[i]).init_state(0);
			}
			else {
				((Computer)players[i]).result(players[num].get_sum());
				((Computer)players[i]).init_state(i+1);
			}
		}
		return;
	}
}

class Card {
	int value;
	int suit;
	
	public Card(int theValue,int theSuit) {
		value=theValue;
		suit=theSuit;
	}
}

class Deck {
	private Card[] deck;
	private int cardsUsed;
	
	public Deck(){
		deck=new Card[52];
		for(int i=0;i<13;i++) {
			for(int j=0;j<4;j++) {
				deck[4*i+j]=new Card(i+1,j);
			}
		}
	}
	
	public void shuffle(int seed) {
		Random random=new Random(seed);
		for(int i=deck.length-1;i>0;i--) {
			int rand=(int)(random.nextInt(i+1));
			Card temp=deck[i];
			deck[i]=deck[rand];
			deck[rand]=temp;
		}
		cardsUsed=0;
	}
	public Card dealCard() {
		if(cardsUsed==deck.length)
			throw new IllegalStateException("No cards are left in the deck.");
		cardsUsed++;
		return deck[cardsUsed-1];
	}
}

class Hand {
	Card[] hand=new Card[52];
	int cards_in_hand;
	
	public Hand() {
		cards_in_hand=0;
	}
	
	public void set_card(Card card) {
		hand[cards_in_hand]=card;
		cards_in_hand++;
	}
	
	public void get_card(int idx) {
		for(int i=idx;i<cards_in_hand;i++) {
			if(i!=0)
				System.out.print(",");
			int val=hand[i].value;
			int sui=hand[i].suit;
			
			switch(sui) {
			case 0: sui='c';
			break;
			case 1: sui='h';
			break;
			case 2: sui='d';
			break;
			case 3: sui='s';
			break;
			}
			
			switch(val) {
			case 1: System.out.print(" A"+(char)sui);
			break;
			case 11: System.out.print(" J"+(char)sui);
			break;
			case 12: System.out.print(" Q"+(char)sui);
			break;
			case 13: System.out.printf(" K"+(char)sui);
			break;
			default: System.out.printf(" "+val+(char)sui);
			}	
		}
	}
	
	public int get_sum() {
		int A_cnt=0;
		int sum=0;
		for(int i=0;i<cards_in_hand;i++) {
			if(hand[i].value==1) {
				sum+=11;
				A_cnt++;
			}
			else if(hand[i].value>=10)
				sum+=10;
			else 
				sum+=hand[i].value;
		}
		while(A_cnt!=0&&sum>21) {
			sum-=10;
			A_cnt--;
		}
		return sum;
	}
} 
class Computer extends Hand{
	void init_state(int n) {
		System.out.print("player"+n+":");
		get_card(0);
		System.out.print(" ("+get_sum()+")");
		if(get_sum()>21)
			System.out.print(" - Bust!");
		System.out.println();
	}
	
	void result(int house) {
		if(house>21) {
			if(get_sum()>21)
				System.out.print("[Lose] ");
			else
				System.out.print("[Win] ");
		}
		else {
			if(get_sum()>21)
				System.out.print("[Lose] ");
			else {
				if(get_sum()==house)
					System.out.print("[Draw] ");
				else if(get_sum()<house)
					System.out.print("[Lose] ");
				else
					System.out.print("[Win] ");
			}
		
		}
	}
	
} 
class Player extends Hand{
	void init_state(int n) {
		System.out.print("Player1:");
		get_card(0);
		System.out.print(" ("+get_sum()+")");
		if(get_sum()>21)
			System.out.print(" - Bust!");
		System.out.println();
	}
	
	void result(int house) {
		if(house>21) {
			if(get_sum()>21)
				System.out.print("[Lose] ");
			else
				System.out.print("[Win] ");
		}
		else {
			if(get_sum()>21)
				System.out.print("[Lose] ");
			else {
				if(get_sum()==house)
					System.out.print("[Draw] ");
				else if(get_sum()<house)
					System.out.print("[Lose] ");
				else
					System.out.print("[Win] ");
			}
		
		}
	}
	
} 
class House extends Hand{
	void init_state(int n) {
		System.out.print("House:");
		if(n!=0)
			System.out.print(" HIDDEN");
		get_card(n);
		if(n==0)
			System.out.print(" "+"("+get_sum()+")");
		if(get_sum()>21)
			System.out.print(" - Bust!");
		System.out.println();
	}
	
}