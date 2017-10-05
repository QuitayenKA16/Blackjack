package gui_programs;
import java.util.Random;
import java.util.Vector;

class Card
{
	private int suit;
	private int value;
	
	Card(int userSuit, int userVal)
	{
		suit = userSuit;
		value = userVal;
	}
	
	public int getSuit(){
		return suit;
	}
	public int getVal(){
		return value;
	}
	public String getString()
	{
		String str = value + " ";
		switch (suit)
		{
			case 1: str += "Hearts"; break;
			case 2: str += "Diamonds"; break;
			case 3: str += "Spades"; break;
			case 4: str += "Clubs"; break;
		}
		return str;
	}
	public String getImage(int status)//1=normal, 2=back, 3=selected
	{
		String file = "";
		if (status == 2)
		{
			file += "back.gif";
		}
		else
		{
			switch (suit)
			{
				case 1: file += "Hearts/"; break;
				case 2: file += "Diamonds/"; break;
				case 3: file += "Spades/"; break;
				case 4: file += "Clubs/"; break;
			}
			switch (value)
			{
				case 1: file += "Ace"; break;
				case 2: file += "Two"; break;
				case 3: file += "Three"; break;
				case 4: file += "Four"; break;
				case 5: file += "Five"; break;
				case 6: file += "Six"; break;
				case 7: file += "Seven"; break;
				case 8: file += "Eight"; break;
				case 9: file += "Nine"; break;
				case 10: file += "Ten"; break;
				case 11: file += "Jack"; break;
				case 12: file += "Queen"; break;
				case 13: file += "King"; break;
			}
			if (status == 3)
				file += "Border.gif";
			else
				file += ".gif";
		}
		return file;
	}
}

class Deck
{
	private Vector<Card> d;
	
	Deck()
	{
		d = new Vector<Card>();
		for (int suit = 1; suit <= 4; suit++)
			for (int val = 1; val <= 13; val++ )
			{
				Card n = new Card(suit, val);
				d.addElement(n);
			}
	}
	
	public void shuffle()
	{
		Random randNumGen = new Random();
		int index1, index2;
		Card temp = new Card(1,1);
		
		for (int i = 0; i < 1000; i++)
		{
			index1 = randNumGen.nextInt(52);
			index2 = randNumGen.nextInt(52);
			
			temp = d.elementAt(index1);
			d.setElementAt(d.elementAt(index2), index1);
			d.setElementAt(temp, index2);
		}
	}
	
	public Vector<Card> getDeck(){
		return d;
	}
}

class Player
{
	private String name;
	private int points;
	private int handTotal;
	private Vector<Card> hand;
	
	//Constructors
	Player()
	{
		name = "Joe";
		points = 0;
		handTotal = 0;
		hand = new Vector<Card>();
	}
	Player(String userName)
	{
		points = 0;
		name = userName;
		hand = new Vector<Card>();
	}
	
	public void setName(String userName){
		name = userName;
	}
	public void setPoints(int userPoints){
		points = userPoints;
	}
	public void setTotal()
	{
		handTotal = 0;
		for (int i = 0; i < hand.size(); i++)
			handTotal += hand.elementAt(i).getVal();
	}
	
	public String getName(){
		return name;
	}
	public int getTotal(){
		return handTotal;
	}
	public int getPoints(){
		return points;
	}
	public Vector<Card> getHand(){
		return hand;
	}
	public String getImage(int index, int status)
	{
		Card c = hand.elementAt(index);
		return c.getImage(status);
	}
	public int getSuit(int index){
		return hand.elementAt(index).getSuit();
	}
	public int getVal(int index){
		return hand.elementAt(index).getVal();
	}
	public void remove(int index){
		hand.remove(index);
	}
	
	public void addCard(Card newCard){
		hand.addElement(newCard);
	}
	public void addHand(Vector<Card> userHand)
	{
		hand.removeAllElements();
		for (int i = 0; i < userHand.size(); i++)
			hand.addElement(userHand.elementAt(i));
		sortHand();
	}
	
	public void sortHand()
	{
		//loop control and temp variable
		int i, j;
		Card temp = new Card(1,1);
				
		for (i = 0; i < hand.size(); i++) 
		{
			for (j = 1; j < (hand.size()- i); j++) 
			{
				if (hand.elementAt(j - 1).getVal() > hand.elementAt(j).getVal())
				{
					temp = hand.elementAt(j - 1);
					hand.setElementAt(hand.elementAt(j), j-1);
	        		hand.setElementAt(temp, j);
				}
				else if (hand.elementAt(j - 1).getVal() == hand.elementAt(j).getVal()) //sort by suit
				{
					if (hand.elementAt(j - 1).getSuit() > hand.elementAt(j).getSuit())
					{
						temp = hand.elementAt(j - 1);
						hand.setElementAt(hand.elementAt(j), j-1);
		        		hand.setElementAt(temp, j);
					}
				}
			} 
		}
	}
}

class Dealer extends Player
{
	private Deck d;
	
	Dealer()
	{
		d = new Deck();
		setName("Dealer");
		d.shuffle();
	}
	
	public void newDeck()
	{
		d = new Deck();
		d.shuffle();
	}
	
	public Card dealCard()
	{
		Random randNumGen = new Random();
		int index;
		Card c = new Card(1,1);
		
		index = randNumGen.nextInt(d.getDeck().size());
		c = d.getDeck().elementAt(index);
		d.getDeck().remove(index);
		
		return c;
	}
}