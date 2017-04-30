import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.*;

public class GamePanel extends JPanel{	
	//variables for everything that goes in the game, because this is panel for it
	private Map map;
	private static Player player;
	private static MapPanel mapPanel;
	private static Creature[] chasers;
	private static Timer timer;
	public int numLvl = 0;//numbers clicks every level past
	public GamePanel (int numCreatures){
		// create a 70x50 map w/ 10x10-pixel blocks
	//	this.creatureCount=numCreatures;
		int width = 140;
		int height = 100;
		int bSize = 10;
		map = new Map (width, height);
		//starts player out in the top left corner of the map
		player = new Player (map, 1, 0);
		player.setNewMap(map);
		chasers = new Creature[numCreatures];

		
		for (int i = 0; i < numCreatures+(numLvl); i++){
			//temporary variable for randomly placing the Creature types
			Random temp = new Random();
			chasers[i] = new Creature (map, temp.nextInt(height-2)+1,
										temp.nextInt(width-2)+1);

			}
		
		//making new MapPanel object with everything it need to meet object type qualifications
		//setting proper size,adding listeners, setting layout to flow layout, and finally adding
		//	the mapPanel
		mapPanel = new MapPanel (map, player, chasers, bSize);
		mapPanel.setPreferredSize (new Dimension ((width+2) * bSize, (height+1) * bSize));
		mapPanel.addKeyListener (new PlayerController (player));
		mapPanel.setFocusable (true);
		setLayout (new FlowLayout());
		add (mapPanel);
		//making the delay and setting timer, then starting timer
		int delay = 5;
		this.timer = new Timer (delay, new TimerListener());
		timer.start();
	
	}
	 // check the game status every X milliseconds
	 //		(as defined upon Timer creation). 
	private class TimerListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			mapPanel.repaint();
			//using the checkWinLoss here as conditions
			if (mapPanel.checkWinLoss() != 0)
				timer.stop();
			//for winning conditions
			if (mapPanel.checkWinLoss() == 1){
			   	int answer = JOptionPane.showConfirmDialog //winner dialog, with option pane, and further conditionals
			   			(null, "WINRAR, you cleared the Level " + (numLvl+1)+"!"+"\nNext Level?", "Winner!",
			   					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
			   	if (answer==JOptionPane.NO_OPTION){
			   		System.exit(0);//simple close if no longer answer is no
			   	}
			   	else{//on to next stage
			   		numLvl += 1;				//clicker for levels cleared total
			   		int creatureCount=2;
			   		creatureCount= numLvl*creatureCount;			//creature multiplier
			   				   	
			   	//	chasers = new Creature[creatureCount];			
			   	//	buffchasers= new BuffCreature[creatureCount];	<------	attempt at addition of creatures as you hit new levels.
			   	//	cChasers= new CrookedCreature[creatureCount];			   
			    
			   		player.Reset();				//resets to origin position	
			   		mapPanel.redoGameState();	//gameState in checkWinLoss is back to zero
					map = new Map (map.getWidth(), map.getHeight());//a new Map object, gets proper Width and Height from class
					mapPanel.setNewMap(map);	//fetching mapPanel's method of setting up fresh mapPanel for game
					player.setNewMap(map);		//sends new map to players, for new walls
					mapPanel.refreshTimer();	//sets the delay to the original every new map 
					mapPanel.shortenDelay(numLvl);//increases delay multiplier if onto next level
					//randomly sets resets all Creature types
			   		Random temp = new Random();
			   		for (int i = 0; i < (chasers.length); i++){
			   			chasers[i].setNewMap(map);	//resets map for creatures
			   			chasers[i].shuffle(temp.nextInt(50-4)+1,temp.nextInt(70-4)+1);
			   		}

			   		mapPanel.repaint();
			   		timer.start();//starts the timer again, timer decay active
			   	}
					
			}// will do the same thing as winner dialog, except there is no decay on initial delay
				// when you restart and the level clicker back to zero
			if (mapPanel.checkWinLoss()==-1){
			int answer = JOptionPane.showConfirmDialog
		   			(null, "BUMMER, you died on level " + (numLvl+1)+",\nNew Game?", "YOU DIED.", 
		   					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
		   	if (answer==JOptionPane.NO_OPTION){
		   		System.exit(0);
		   		}
		   	else{
		   		numLvl=0;
		   		player.Reset();
		   		mapPanel.redoGameState();
		   		Random temp = new Random();
				map = new Map (map.getWidth(), map.getHeight());
				mapPanel.setNewMap(map);
				player.setNewMap(map);
				
		   		for (int i = 0; i < chasers.length; i++){
		   			chasers[i].setNewMap(map);
		   			chasers[i].shuffle(temp.nextInt(50-2)+1,temp.nextInt(70-2)+1);
		   		}

		   		

		   		mapPanel.refreshTimer();
		   		mapPanel.repaint();
		   		timer.start();
		   		}
			}
		}
	}
   //PlayerController is a listener that is for the keyboard
	private class PlayerController extends KeyAdapter{
		private Player player;

		public PlayerController (Player player) {
			this.player = player;
		}
			//this defines the keystrokes used, using the move method we can
			// tell it which direction we want each key to be
		public void keyPressed (KeyEvent e){
			switch (e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				player.move(1);
				break;
			case KeyEvent.VK_RIGHT:
				player.move(2);
				break;
			case KeyEvent.VK_UP:
				player.move(3);
				break;
			case KeyEvent.VK_DOWN:
				player.move(4);
				break;
				}
		}
	}
}


   		



	

