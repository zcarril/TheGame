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
	private static Slender[] sChasers;
	private static Diags[] dChasers;
	private static PlayerBomb bomb;
	private static LevelKey[] keys;
	private static Timer timer;
	private int numLvl=0;//numbers clicks every level past
	protected int creatureCount;
	protected int keyCount;
	
	public GamePanel (int numCreatures){
		// create a 90x50 map w/ 17x17-pixel blocks
		this.creatureCount=numCreatures;
		this.keyCount=1;
		int width = 90;
		int height = 50;
		int bSize = 20;
		map = new Map (width, height);
		//starts player out in the top left corner of the map
		player = new Player (map, 1, 0);
		player.setNewMap(map);
		chasers = new Creature[creatureCount];
		sChasers= new Slender[creatureCount];
		dChasers= new Diags[creatureCount];
		keys=new LevelKey[keyCount];
		

		Random temp = new Random();
		int j=0;
   		while (j < keyCount) {
   			int r = temp.nextInt(50-4)+1;
   			int c = temp.nextInt(90-4)+1;
   			
   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
   			{
   				System.out.println("trying for key in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
	   			keys[j] = new LevelKey(map, r, c);
	   			j++;
   			}
   		}
		
   		int i = 0;
   		while (i < creatureCount) {
   			int r = temp.nextInt(50-4)+1;
   			int c = temp.nextInt(90-4)+1;
   			
   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
   			{
   				System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
	   			chasers[i] = new Creature(map, r, c);
	   			i++;
   			}
   		}
   		int k = 0;
   		while (k < creatureCount) {
   			int r = temp.nextInt(90-4)+1;
   			int c = temp.nextInt(130-4)+1;
   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
   			{
	   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
	   			sChasers[k]= new Slender(map,r,c);
	   			k++;
   			}
   		}
   		int l = 0;
   		while (l < creatureCount) {
   			int r = temp.nextInt(90-4)+1;
   			int c = temp.nextInt(130-4)+1;
   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
   			{
	   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
	   			dChasers[l]= new Diags(map,r,c);
	   			l++;
   			}
   		}
   		if (bomb!=null){
   			bomb=new PlayerBomb(map,player.getPos().r, player.getPos().c);
   		}
   			

		//making new MapPanel object with everything it need to meet object type qualifications
		//setting proper size,adding listeners, setting layout to flow layout, and finally adding
		//	the mapPanel
		mapPanel = new MapPanel (map, player, chasers, sChasers, dChasers, keys, bomb, bSize);
		mapPanel.setPreferredSize (new Dimension ((width+2) * bSize, (height+1) * bSize));
		mapPanel.addKeyListener (new PlayerController (player));
		mapPanel.setFocusable (true);
		setLayout (new FlowLayout());
		add (mapPanel);
		//making the delay and setting timer, then starting timer
		int delay = 100;
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
			mapPanel.checkKeys();
			mapPanel.checkBombs();
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
			   		numLvl = numLvl+1;				//clicker for levels cleared total
			   		creatureCount= creatureCount + numLvl;			//creature multiplier
			   		keyCount=numLvl+1;
			   		System.out.println("new creature count "+creatureCount+" for level "+numLvl);

			   		player.Reset();				//resets to origin position	
			   		mapPanel.getKeyCount();
			   		mapPanel.redoGameState();	//gameState in checkWinLoss is back to zero
					map = new Map (map.getWidth(), map.getHeight());//a new Map object, gets proper Width and Height from class
					mapPanel.setNewMap(map);	//fetching mapPanel's method of setting up fresh mapPanel for game
					player.setNewMap(map);		//sends new map to players, for new walls
					mapPanel.refreshTimer();	//sets the delay to the original every new map 
					mapPanel.shortenDelay(numLvl);//increases delay multiplier if onto next level
					chasers = new Creature[creatureCount];
					sChasers= new Slender[creatureCount];
					dChasers= new Diags[creatureCount];
					keys=new LevelKey[keyCount];
					mapPanel.resetChasers(chasers);
					mapPanel.resetSlenders(sChasers);
					mapPanel.resetDiags(dChasers);
					mapPanel.resetKeys(keys);
					
					//randomly sets resets all Creature types and Key Objects

			   		Random temp = new Random();
					int j=0;
			   		while (j < keyCount) {
			   			int r = temp.nextInt(50-4)+1;
			   			int c = temp.nextInt(90-4)+1;
			   			
			   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
			   			{
			   				System.out.println("trying for key in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
				   			keys[j] = new LevelKey(map, r, c);
				   			j++;
			   			}
			   		}
			   		int i = 0;
			   		while (i < creatureCount) {
			   			int r = temp.nextInt(90-4)+1;
			   			int c = temp.nextInt(130-4)+1;
			   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
			   			{
				   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
				   			chasers[i] = new Creature(map, r, c);
				   			i++;
			   			}
			   		}
			   		int k = 0;
			   		while (k < creatureCount) {
			   			int r = temp.nextInt(90-4)+1;
			   			int c = temp.nextInt(130-4)+1;
			   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
			   			{
				   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
				   			sChasers[k]= new Slender(map,r,c);
				   			k++;
			   			}
			   		}
			   		int l = 0;
			   		while (l < creatureCount) {
			   			int r = temp.nextInt(90-4)+1;
			   			int c = temp.nextInt(130-4)+1;
			   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
			   			{
				   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
				   			dChasers[l]= new Diags(map,r,c);
				   			l++;
			   			}
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
		   		numLvl = 0;				//clicker for levels cleared total
		   		creatureCount= 1;			//creature multiplier
		   		keyCount=1;
		   		System.out.println("new creature count "+creatureCount+" for level "+numLvl);
		   		System.out.println("new key count "+keyCount+" for level "+numLvl);

		   		player.Reset();				//resets to origin position	
		   		mapPanel.resetKeyCount();
		   		mapPanel.redoGameState();	//gameState in checkWinLoss is back to zero
				map = new Map (map.getWidth(), map.getHeight());//a new Map object, gets proper Width and Height from class
				mapPanel.setNewMap(map);	//fetching mapPanel's method of setting up fresh mapPanel for game
				player.setNewMap(map);		//sends new map to players, for new walls
				mapPanel.refreshTimer();	//sets the delay to the original every new map 
				mapPanel.shortenDelay(numLvl);//increases delay multiplier if onto next level
				//randomly sets resets all Creature types
				chasers = new Creature[creatureCount];
				sChasers= new Slender[creatureCount];
				dChasers= new Diags[creatureCount];
				keys=new LevelKey[keyCount];
				mapPanel.resetChasers(chasers);
				mapPanel.resetSlenders(sChasers);
				mapPanel.resetDiags(dChasers);
				mapPanel.resetKeys(keys);
		   		Random temp = new Random();
				int j=0;
		   		while (j < keyCount) {
		   			int r = temp.nextInt(50-4)+1;
		   			int c = temp.nextInt(90-4)+1;
		   			
		   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
		   			{
		   				System.out.println("trying for key in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
			   			keys[j] = new LevelKey(map, r, c);
			   			j++;
		   			}
		   		}
		   		int i = 0;
		   		while (i < creatureCount) {
		   			int r = temp.nextInt(50-4)+1;
		   			int c = temp.nextInt(90-4)+1;
		   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
		   			{
			   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
			   			chasers[i] = new Creature(map, r, c);
			   			
			   			i++;
		   			}
		   		}
		   		int k = 0;
		   		while (k < creatureCount) {
		   			int r = temp.nextInt(90-4)+1;
		   			int c = temp.nextInt(130-4)+1;
		   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
		   			{
			   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
			   			sChasers[k]= new Slender(map,r,c);
			   			k++;
		   			}
		   		}
		   		int l = 0;
		   		while (l < creatureCount) {
		   			int r = temp.nextInt(90-4)+1;
		   			int c = temp.nextInt(130-4)+1;
		   			if (map.getSquare(r,c) == 4) //4 == ROOM_SPACE
		   			{
			   			System.out.println("trying for creature in "+r+" "+c+" with attribute number "+map.getSquare(r, c)); //error checking space spawned in
			   			dChasers[l]= new Diags(map,r,c);
			   			l++;
		   			}
		   		}
		   		mapPanel.checkKeys();
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
			case KeyEvent.VK_SPACE:
			//	player.PlaceBomb(map, player.getPos().r, player.getPos().c);
				mapPanel.placeBomb(map,player.getPos().r,player.getPos().c);
				System.out.println("trying for bomb at "+ player.getPos().r+" and "+player.getPos().c);
				break;	
			}

		}
	}

}


   		



	

