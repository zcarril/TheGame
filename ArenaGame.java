import javax.swing.*;
//class that runs the game
public class ArenaGame{
	private static final int StartingCreeps=3;
	private static final int minValue=1;
	public static void main (String[] args){
		int numCreatures;
		//local variable for the creatures
		JFrame frame = new JFrame ("The Game");
		//creates the frame for the game
		if (args.length < minValue)
			numCreatures = StartingCreeps;
		else
			numCreatures = Integer.parseInt (args[0]);
		GamePanel panel = new GamePanel (numCreatures);
		frame.add (panel);
		//adds the panel to the frame
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible (true);
	}
}
