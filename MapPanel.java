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
	private LevelKey[] keys;
	private Slender[] slenders;
	private Diags[] diags;
	
	private Player player;
	private PlayerBomb bomb;
	private int width, height, bSize;
	private Timer timer;
	private int wait = 500; //wait for creatures to move
	private static int gameState;
	private static int keyCount;

	public MapPanel (Map map, Player player, Creature[] creatures, Slender[] slenders, Diags[] diags, LevelKey[] keys, PlayerBomb bomb, int squareSize){
		this.map = map;
		this.player = player;
		this.creatures = creatures;
		this.slenders = slenders;
		this.diags=diags;
		this.keys=keys;
		//sets border size to the size of a square, gets Width and Height
		bSize = squareSize;
		width = map.getWidth();
		height = map.getHeight();
		//sets the gameState to zero(playable)
		gameState = 0;
		keyCount=1;

		//sets the timer to the initial value(1000), fills the field for the Listener, and starts the timer
		this.timer = new Timer (wait, null);
		timer.addActionListener(new CreatureTimer (timer, player, bomb, creatures, slenders, diags));
		timer.start();
	}
	//sets the game back to a playable state
	public void redoGameState(){
		gameState=0;
	}
	public int getKeyCount(){
		keyCount=1;
		for(LevelKey key: keys){
			keyCount++;
		}
		return keyCount;
	}
	public int resetKeyCount(){
		return keyCount=1;
	}
	//sets the map to a new state, from initial values
	public void setNewMap(Map x){
		this.map=x;
	}

	
	public void resetChasers(Creature[] c){
		this.creatures=c;
		this.timer = new Timer (wait, null);
		timer.addActionListener(new CreatureTimer (timer, player, bomb, creatures, slenders, diags));
		timer.start();
	}
	public void resetSlenders(Slender[] s){
		this.slenders=s;
		this.timer = new Timer (wait, null);
		timer.addActionListener(new CreatureTimer (timer, player, bomb, creatures, slenders, diags));
		timer.start();
	}
	public void resetDiags(Diags[] d){
		this.diags=d;;
		this.timer = new Timer (wait, null);
		timer.addActionListener(new CreatureTimer (timer, player, bomb, creatures, slenders, diags));
		timer.start();
	}
	public void resetKeys(LevelKey[] k){
		this.keys=k;
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
	public void placeBomb(Map m, int r, int c){
		this.map=m;
		bomb=new PlayerBomb(map,r,c);
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
		for (Slender slender: slenders){
			if(slender.getPos().c==player.getPos().c && slender.getPos().r==player.getPos().r)
				gameState=-1;
		}
		for (Diags diag: diags){
			if(diag.getPos().c==player.getPos().c && diag.getPos().r==player.getPos().r)
				gameState=-1;
			}
		
		
				
		//check if player eaten by creature type BuffCreature (lose)
		return gameState;
	}
	//checks position of player and keys, if same takes key off the map
	protected int checkKeys(){
		for (LevelKey key : keys){
			if(key.getPos().c==player.getPos().c && key.getPos().r==player.getPos().r){
				keyCount-=1;
				map.clearFinish(keyCount);
				System.out.println("current key count at" + keyCount);
				key.changePosition(width-1, height-1);
			}
			
		}
		
		return keyCount;
	}
	//checks position of enemies and bomb objects, if same, takes both from the map
	protected void checkBombs(){
		for (Creature creature : creatures){
			if (bomb!=null)
			if (creature.getPos().c == bomb.getPos().c && creature.getPos().r == bomb.getPos().r){
				creature.changePosition(width-1, height-1);
				bomb.changePosition(width-1, height-1);
			}

		}
		for (Slender slender:slenders){
			if (bomb!=null)
			if(slender.getPos().c==bomb.getPos().c && slender.getPos().r == bomb.getPos().r){
				slender.changePosition(width-1, height-1);
				bomb.changePosition(width-1, height-1);
			}
		}
		for (Diags diag:diags){
			if (bomb!=null)
			if(diag.getPos().c==bomb.getPos().c && diag.getPos().r == bomb.getPos().r){
				diag.changePosition(width-1, height-1);
				bomb.changePosition(width-1, height-1);
			}
		}
	}
	

	//paints the components to the specified color
	protected void paintComponent (Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent (g2);
		Random temp=new Random();
		int x=temp.nextInt(200);
		int y=temp.nextInt(200);
		int z=temp.nextInt(200);
		int i = 0;
		for (int r = i; r < height; r++)
			for (int c = i; c < width; c++){
				Rectangle2D.Double bkgnd =
						new Rectangle2D.Double (c * bSize, r * bSize, bSize, bSize);
				if (map.getSquare(r,c) == Map.OPEN_SPACE){

							g2.setColor (new Color(150,150,150));	
				}
				else if (map.getSquare(r, c)== Map.ROOM_SPACE){
					
					if (r%2==i || c%2==i){
						g2.setColor(new Color(80,80,80));
					}
					else{
						g2.setColor(new Color(90,80,80));
					}
				}
				
				else if (map.getSquare(r,c) == Map.WALL_SPACE){
					if (c%2==i){
						g2.setColor(new Color(60,30,3));
					}
					else{
						g2.setColor(new Color(80,34,3));
					}
			}
				else if (map.getSquare(r,c) == Map.FINISH_SPACE)
					g2.setColor (new Color(x,y,z));
				else
					g2.setColor (Color.WHITE);
				g2.fill (bkgnd);
			}
		//player
		ImageIcon personIcon = new ImageIcon("heroIcon.png");
		Image personImage = personIcon.getImage(); //to transform it
		Image newPerson = personImage.getScaledInstance(bSize, bSize, java.awt.Image.SCALE_SMOOTH);
		personIcon = new ImageIcon(newPerson);
		personIcon.paintIcon(null, g2,player.getPos().c * bSize, player.getPos().r * bSize);
		//Slender Creature

		for (Slender slender: slenders){
				ImageIcon slenderIcon = new ImageIcon("slenderEnemy.png");
				Image slenderImage = slenderIcon.getImage(); //to transform it
				Image newSlender = slenderImage.getScaledInstance(bSize, bSize, java.awt.Image.SCALE_SMOOTH);
				slenderIcon = new ImageIcon(newSlender);
				slenderIcon.paintIcon(null, g2,slender.getPos().c * bSize, slender.getPos().r * bSize);
			}
		//Diags
		for (Diags diag: diags){
			ImageIcon diagIcon = new ImageIcon("LSP.png");
			Image diagImage = diagIcon.getImage(); //to transform it
			Image newDiag = diagImage.getScaledInstance(bSize, bSize, java.awt.Image.SCALE_SMOOTH);
			diagIcon = new ImageIcon(newDiag);
			diagIcon.paintIcon(null, g2,diag.getPos().c * bSize, diag.getPos().r * bSize);
		}
		//standard Mikes
		for (Creature creature: creatures){
			ImageIcon creatureIcon = new ImageIcon("mike.png");
			Image creatureImage = creatureIcon.getImage(); //to transform it
			Image newCreature = creatureImage.getScaledInstance(bSize, bSize, java.awt.Image.SCALE_SMOOTH);
			creatureIcon = new ImageIcon(newCreature);
			creatureIcon.paintIcon(null, g2,creature.getPos().c * bSize, creature.getPos().r * bSize);
		}
		//for keys
		for (LevelKey key: keys) {
			ImageIcon keyIcon = new ImageIcon("key.png");
			Image keyImage = keyIcon.getImage(); //to transform it
			Image newKey = keyImage.getScaledInstance(bSize, bSize, java.awt.Image.SCALE_SMOOTH);
			keyIcon = new ImageIcon(newKey);
			keyIcon.paintIcon(null, g2,key.getPos().c * bSize, key.getPos().r * bSize);
		
		}
		//for bombs
		if (bomb!=null){
			ImageIcon bombIcon = new ImageIcon("bomb.png");
			Image bombImage = bombIcon.getImage(); //to transform it
			Image newBomb = bombImage.getScaledInstance(bSize, bSize, java.awt.Image.SCALE_SMOOTH);
			bombIcon = new ImageIcon(newBomb);
			bombIcon.paintIcon(null, g2,bomb.getPos().c * bSize, bomb.getPos().r * bSize);
		}
	}


	
	//controller of the timing of the creatures
	private class CreatureTimer implements ActionListener{
		private Player player;
		private PlayerBomb bomb;
		private Creature[] chasers;
		private Slender[] sChasers;
		private Diags[] dChasers;
		private Timer timer;
		private double k= 0.0001; //constant that sets the delay decay
		
		protected CreatureTimer (Timer t, Player player, PlayerBomb bomb, Creature[] chasers, Slender[] sChasers, Diags[] dChasers){
			this.bomb=bomb;
			this.timer = t;
			this.player = player;
			this.chasers = chasers;
			this.sChasers=sChasers;
			this.dChasers=dChasers;
		}
		//tells creatures to chase player, and is the timing
		//	delay decay that happens every delay (decay multiplier over time)
		//takes the current delay, and subtracts a fraction of itself
		//	and then sets that that number as the new delay
		public void actionPerformed (ActionEvent e){
		//	timer.setDelay((int)(timer.getDelay()-timer.getDelay()*k));
			for (Creature chaser : chasers){
				double a= (player.getPos().r-chaser.getPos().r);
				double b= (player.getPos().c-chaser.getPos().c);
				if (Math.abs(a)<=5 || Math.abs(b)<5 ){
					chaser.chase (player);
				}
				else{
					chaser.wander();
				}

			}
			for (Slender slender: slenders){
				double a= (player.getPos().r - slender.getPos().r);
				double b= (player.getPos().c - slender.getPos().c);
				if (Math.abs(a)<=5 || Math.abs(b)<5){
					slender.chase(player);
				}
				else{
					slender.teleport();
				}
			}
			for (Diags diag: diags){
				double a= (player.getPos().r - diag.getPos().r);
				double b= (player.getPos().c - diag.getPos().c);
				if (Math.abs(a)<=5 || Math.abs(b)<5){
					diag.diagChase(player);
				}
				else{
					diag.wander();
				}
			}
		}
	}


}



