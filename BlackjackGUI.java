package gui_programs;

//Karamel Quitayen
//G Period Java
//Program 27: Blackjack GUI
//=====================================
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Vector;

public class BlackjackGUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Dealer dealer;
	private Player player;
	
	//buttons
	private JButton hit = new JButton("Hit");
	private JButton stay = new JButton("Stay");
	private JButton deal = new JButton("Deal");
	
	//panels
	private JPanel buttons = new JPanel();
	private JPanel pHand = new JPanel();
	private JPanel dHand = new JPanel();
	private JPanel result = new JPanel();
	
	private static final Color feltGreen = new Color(0,82,0);
	private static final Font font = new Font("Consolas", Font.PLAIN, 14);
	private String pPoints;
	
	BlackjackGUI()
	{
		//set up frame
		setTitle("Blackjack");
		setLocation(300,100);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//set up panels
		buttons.setSize(500,50);
		pHand.setSize(500,150);
		dHand.setSize(500,150);
		result.setSize(500,50);
		buttons.setBackground(feltGreen);
		pHand.setBackground(feltGreen);
		dHand.setBackground(feltGreen);
		result.setBackground(feltGreen);
		result.setLocation(0,350);

		//set up buttons
		hit.addActionListener(this);
		stay.addActionListener(this);
		deal.addActionListener(this);
		buttons.add(hit);
		buttons.add(stay);
		buttons.add(deal);
		
		Deal();
		getContentPane().setLayout(null);
		getContentPane().add(buttons);
		setVisible(true);
	}
	
	public void Deal() //new hands
	{
		hit.setEnabled(true);
		stay.setEnabled(true);
		
		player = new Player();
		dealer = new Dealer();
		
		//both start with 2 cards
		for (int i = 0; i < 2; i++)
		{
			player.addCard(dealer.dealCard());
			dealer.addCard(dealer.dealCard());
		}
		updateGUI(false);
	}

	public void updateGUI (boolean showAll)
	{
		//setSize(505,377);
		setSize(500,372);
		getContentPane().remove(pHand);
		getContentPane().remove(dHand);
		getContentPane().remove(result);
		pHand.removeAll();
		dHand.removeAll();
		playerPanel();
		dealerPanel(showAll);
		getTotal(player.getHand(), true);
		pHand.setBorder(BorderFactory.createTitledBorder(null, pPoints, TitledBorder.LEFT, TitledBorder.TOP, font, Color.WHITE));
		dHand.setBorder(BorderFactory.createTitledBorder(null, "Dealer's Hand", TitledBorder.LEFT, TitledBorder.TOP, font, Color.WHITE));
		getContentPane().add(pHand);
		getContentPane().add(dHand);
		pHand.setLocation(0,50);
		dHand.setLocation(0,200);
		getContentPane().repaint();
		getContentPane().validate();
	}
	
	public void playerPanel()
	{
		for (int i = 0; i < player.getHand().size(); i++)
		{
			Image img = null;
			String path = player.getHand().elementAt(i).getImage(1);
			try {
				img = ImageIO.read(ResourceLoader.load(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
			pHand.add(new JLabel(new ImageIcon(img)));
		}
	}
	
	public void dealerPanel(boolean showAll)
	{
		String path;
		for (int i = 0; i < dealer.getHand().size(); i++)
		{
			if (showAll)
				path = dealer.getHand().elementAt(i).getImage(1);
			else
				if (i == dealer.getHand().size() - 1) //last card is faced back
					path = dealer.getHand().elementAt(i).getImage(2);
				else
					path = dealer.getHand().elementAt(i).getImage(1);
			
			Image img = null;
			try {
				img = ImageIO.read(ResourceLoader.load(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
			dHand.add(new JLabel(new ImageIcon(img)));
		}
	}
	
	public void Hit()
	{
		player.addCard(dealer.dealCard());
		if (player.getHand().size() == 5)
		{
			dealerTurn();
			getWinner(); //can only get 5 cards
		}
		else if (getTotal(player.getHand(), true) > 21)
			getWinner();
		else
		{
			dealerTurn();
			if(getTotal(dealer.getHand(), true) > 21)
				getWinner();
			else
				updateGUI(false);
		}
	}
	
	public void dealerTurn()
	{
		//add current points
		int points = getTotal(dealer.getHand(), false);
		if (points <= 16) //must get card if tot <= 16
			dealer.addCard(dealer.dealCard());
		dealer.setPoints(getTotal(dealer.getHand(), false));
	}
	
	public int getTotal(Vector<Card> hand, boolean p)
	{
		int total = 0;
		boolean oneAce = false, twoAce = false;
		
		for (int i = 0; i < hand.size(); i++)
		{
			//face cards are = 10
			if (hand.elementAt(i).getVal() >= 10)
				total += 10;
			else if (hand.elementAt(i).getVal() == 1){ //ace == 1 or 11
				total += 11;
				if (oneAce)
					twoAce = true;
				else
					oneAce = true;
			}
			else
				total += hand.elementAt(i).getVal();
		}
		
		pPoints = "Player's Hand: ";
		if (total > 21 && oneAce){
			total -= 10; //ace = 1 instead of 11
			pPoints += total;
			return total;
		}
		else if (total > 21 && twoAce){
			total -= 10;
			if (total > 21){
				total -= 10;
				pPoints += total;
				return total;
			}
			else{
				pPoints += (total-10) + " or " + total;
				return total;
			}
		}
		else if (total <= 21 && oneAce){
			pPoints += (total-10) + " or " + total;
			return total;
		}
		else if (total <= 21 && twoAce){
			pPoints += (total-20) + " or " + (total-10) + " or " + total;
			return total;
		}
		else{
			pPoints += total;
			return total;
		}
	}
	
	public void getWinner()
	{
		hit.setEnabled(false);
		stay.setEnabled(false);
		
		player.setPoints(getTotal(player.getHand(), true));
		while (dealer.getPoints() <= 16)
		{
			dealerTurn();
		}
		dealer.setPoints(getTotal(dealer.getHand(), false));
		updateGUI(true);
		dHand.setBorder(BorderFactory.createTitledBorder(null, "Dealer's Hand: " + dealer.getPoints(), TitledBorder.LEFT, TitledBorder.TOP, font, Color.WHITE));
		
		String message = "<html><font color = 'white'>";
		if (player.getPoints() > 21)
			message += "Player went over 21. DEALER WINS.";
		else
		{
			if (dealer.getPoints() > 21)
				message += "Dealer went over 21. PLAYER WINS.";
			else
			{
				if (player.getPoints() > dealer.getPoints())
					message += "PLAYER WINS.";
				else if (player.getPoints() < dealer.getPoints())
					message += "DEALER WINS.";
				else if (player.getPoints() == dealer.getPoints())
					message += "PLAYER AND DEALER TIED.";
			}
		}
		
		//Create label of result
		JLabel label = new JLabel(message);
		label.setFont(new Font("Consolas", Font.PLAIN, 18));
		
		//Display results and winner
		//setSize(505,425);
		setSize(500,420);
		result.removeAll();
		result.add(label);
		getContentPane().add(result);
		getContentPane().repaint();
		getContentPane().validate();
	}
	
	//Get button click
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == hit)
			Hit();
		else if (e.getSource() == stay)
			getWinner();
		else if (e.getSource() == deal)
			Deal();
	}
	
	public static void main(String[] args)
	{
		new BlackjackGUI();
	}
}