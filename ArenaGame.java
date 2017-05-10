import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
//class that runs the game
public class ArenaGame{
	private static final int StartingCreeps=1;
	private static final int minValue=1;
	public static void main (String[] args){
		int numCreatures;
		//local variable for the creatures
		JFrame frame = new JFrame ("Bomber McBombB0I");
		frame.setLayout(new FlowLayout());
		//creates the frame for the game
		if (args.length < minValue)
			numCreatures = StartingCreeps;
		else
			numCreatures = Integer.parseInt (args[0]);
		GamePanel panel = new GamePanel (numCreatures);
		JOptionPane.showMessageDialog(null, 
				"You are the McBombBOI.\nYour goal is to make it to the FLICKERING exit (TOP RIGHT).\n"
				+ "Collect ALL the keys to open the path and proceed to the next level.\n"
				+ "C ONTROLS:\n"
				+ "MOVE: arrow keys\n"
				+ "DROP BOMB: press SPACE\n"
				+ "   This is so you can kill monsters\n", 
				"How to Play", 
				JOptionPane.INFORMATION_MESSAGE, 
				null);
		frame.add (panel);
		JButton howToPlay = new JButton("?");
				
				howToPlay.addActionListener(new ActionListener()
				{
					public void actionPerformed (ActionEvent ae) {
						JOptionPane.showMessageDialog(null, 
								"You are the McBombBOI.\nYour goal is to make it to the RED square (TOP RIGHT).\n"
								+ "Collect ALL the keys to open the path and proceed to the next level.\n"
								+ "CONTROLS:\n"
								+ "MOVE: arrow keys\n"
								+ "DROP BOMB: press SPACE\n"
								+ "   This is so you can kill monsters\n", 
								"How to Play", 
								JOptionPane.INFORMATION_MESSAGE, 
								null);
					}
				});
				
				frame.add(howToPlay);
		//adds the panel to the frame
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible (true);
	}
}
