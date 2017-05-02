import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.Random;

public class MapPanel extends JPanel
{//necessary items for MapPanel
	private Map map;
	private Creature[] creatures;

	private Player player;
	private int width, height, bSize;
	private Timer timer;
	private int wait = 400; //wait for creatures to move
	private static int gameState;

	public MapPanel (Map map, Player player, Creature[] creatures, int squareSize){
		this.map = map;
		this.player = player;
		this.creatures = creatures;
		//sets border size to the size of a square, gets Width and Height
		bSize = squareSize;
		width = map.getWidth();
		height = map.getHeight();
		//sets the gameState to zero(playable)
		gameState = 0;
		//sets the timer to the initial value(1000), fills the field for the Listener, and starts the timer
		this.timer = new Timer (wait, null);
		timer.addActionListener(new CreatureTimer (timer, player, creatures));
		timer.start();
	}
	//sets the game back to a playable state
	public void redoGameState(){
		gameState=0;
	}
	//sets the map to a new state, from initial values
	public void setNewMap(Map x){
		this.map=x;
	}
	
	public void resetChasers(Creature[] c){
		this.creatures=c;
		this.timer = new Timer (wait, null);
		timer.addActionListener(new CreatureTimer (timer, player, creatures));
		timer.start();
	}
	
	public boolean checkRoomSpace(int r,int c){
		if (map.getSquare(r,c)==Map.ROOM_SPACE)
			return true;
		else return false;
	}

	//sets the timer back to 1000 delay
	public void refreshTimer(){
		this.timer.setDelay(wait);
	}
	//initial delay shortens by fraction of time, used upon clearing level
	public void shortenDelay(int x){
		this.timer.setDelay((int)(wait-x*10));
	}
	//checker if won or lost (win==1, lose==-1 (gameState))
	protected int checkWinLoss(){
		//check if player at finish(win)
		if (player.getPos().c == map.getFinish().c
				&& player.getPos().r == map.getFinish().r)
			gameState = 1;
		//check if player eaten by creature type Creature (lose)
		for (Creature creature : creatures)
			if (creature.getPos().c == player.getPos().c 
					&& creature.getPos().r == player.getPos().r)
				gameState = -1;
		//check if player eaten by creature type BuffCreature (lose)
		return gameState;
	}
	//paints the components to the specified color
	protected void paintComponent (Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent (g2);
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++){
				Rectangle2D.Double bkgnd =
						new Rectangle2D.Double (c * bSize, r * bSize, bSize, bSize);
				if (map.getSquare(r,c) == Map.OPEN_SPACE)
					g2.setColor (Color.CYAN);
				else if (map.getSquare(r, c)== Map.ROOM_SPACE)
					g2.setColor(Color.DARK_GRAY);
				else if (map.getSquare(r,c) == Map.WALL_SPACE)
					g2.setColor (Color.BLACK);
				else if (map.getSquare(r,c) == Map.FINISH_SPACE)
					g2.setColor (Color.RED);
				else
					g2.setColor (Color.WHITE);
				g2.fill (bkgnd);
			}
		//player
		g2.setColor (Color.RED);
		g2.fillOval (player.getPos().c * bSize, player.getPos().r * bSize, bSize, bSize);
		//standard Creature
		g2.setColor (Color.MAGENTA);
		for (Creature creature: creatures)
			g2.fillOval (creature.getPos().c * bSize,
						creature.getPos().r * bSize, bSize, bSize);

	}
	//controller of the timing of the creatures
	private class CreatureTimer implements ActionListener{
		private Player player;
		private Creature[] chasers;
		private Timer timer;
		private double k= 0.0001; //constant that sets the delay decay
		
		protected CreatureTimer (Timer t, Player player, Creature[] chasers){	
			this.timer = t;
			this.player = player;
			this.chasers = chasers;	
		}
		//tells creatures to chase player, and is the timing
		//	delay decay that happens every delay (decay multiplier over time)
		//takes the current delay, and subtracts a fraction of itself
		//	and then sets that that number as the new delay
		public void actionPerformed (ActionEvent e){
			for (Creature chaser : chasers){
				chaser.wander();
				double a= (player.getPos().r-chaser.getPos().r);
				double b= (player.getPos().c-chaser.getPos().c);
				if (Math.abs(a)<=10 || Math.abs(b)<10 ){
					chaser.chase (player);
				}
					//timer.setDelay((int)(timer.getDelay()-timer.getDelay()*k));
				//	
				//	chaser.Gravity();
				//	player.Gravity();
			}

		}
	}
}



