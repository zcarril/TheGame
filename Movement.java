import java.util.Random;

//template for Player,Creature, CrookedCreature, and BuffCreature
//	to use. can modify specifics of the the abstract in specific
//	class if needed. e.g jumping spaces, diagonal

public abstract class Movement 
{
	private Position pos;
	private Map map;
	private int originR;
	private int originC;
	private int OneStep=1;
	private int LongStep=5;
	private int jump=15;
	//creates map and position variables for Player, and all
	// creature types
	
	public Movement (Map map, int r, int c) {
		originR=r;//save initial positions to be used
		originC=c;  // when moving player back to origin
		this.map = map;
		pos = new Position (r, c);
	}//sets the position and map to "row"(r) and "column"(c)

	protected Position getPos() {
		return pos; //to get position of player and creature types
	}
	public void setOneStep(int x){
		this.OneStep=x; //set the value of a single step
	}
	public void setNewMap(Map x){
		this.map=x; //this will let player and creatures relay the new map,
	}					// by "Map x" variable
	
	//used by creature types when map refreshes. randomly generated numbers are
	// put it at map change, used in GamePanel
	public void changePosition(int r, int c){
		Position replacement=new Position(r,c);
		pos=replacement;
	}
	

	protected boolean validMove (int r, int c){
		if ((r >= 0 && r < map.getHeight()) && (c >= 0 && c < map.getWidth()))
			if ((map.getSquare (r, c) != Map.WALL_SPACE))
				return true;
			
		//creates a valid move by checking if the row of possible move is greater or equal to zero 
		//	and less than the map height, and if the column of possible move is greater than or equal to zero,
		//	and less than the map width.
		//	the second conditional checks to see if the space moving to is a wall by means of the 
		//		wall checker conditional in the Map.java class. 
		//  if returned true, the move is valid
		return false;
	}
	
	//resets the position to the original start position. used by player type
	public Position Reset(){
		Position initPosition= new Position(originR, originC);
		pos=initPosition;
		return pos;
	}
	
	// these cover all the bases in the left, right, up, down movements
	protected void move (int dir)
	{
		switch (dir)
		{
		case 1:
			if (validMove(pos.r,pos.c-OneStep))
				pos.c -= OneStep;
			break;
		case 2:
			if (validMove(pos.r,pos.c+OneStep))
				pos.c += OneStep;
			break;					
		case 3:
			if (validMove(pos.r-(OneStep),pos.c))
				pos.r -= (OneStep);
			break;
		case 4:
			if (validMove(pos.r+OneStep,pos.c))
				pos.r += OneStep;
			break;
		}
	}
	protected void diagMove(int dir){
		switch (dir){
		case 1:
			move(1);
			move(3);
		case 2:
			move(1);
			move(4);
			
		case 3:
			move(2);
			move(4);
		case 4:
			move(2);
			move(3);
		}
		
	}
	
	protected void jumpMove (int dir)
	{
		switch (dir)
		{
		case 1:
			if (validMove(pos.r,pos.c-jump))
				pos.c -= jump;
			break;
		case 2:
			if (validMove(pos.r,pos.c+jump))
				pos.c += jump;
			break;					
		case 3:
			if (validMove(pos.r-(jump),pos.c))
				pos.r -= (jump);
			break;
		case 4:
			if (validMove(pos.r+jump,pos.c))
				pos.r += jump;
			break;
		}
	}
	protected void chase (Player player)
	{//method to chase Player, by first creating a variable that colDelta and rowDelta
	 //   these variables hold the creature's position minus the players position
		int colDelta = getPos().c - player.getPos().c;
		int rowDelta = getPos().r - player.getPos().r;

		boolean horzMove = (Math.abs(colDelta) > Math.abs(rowDelta));
		// the actual move, which is dependent on the variables just assigned
		//	each move (1,2,3, or 4) is referred to below and will move it on step toward the Player position
		if (horzMove)
		{
			if (colDelta > 0)
				move(1);
			else
				move(2);
		}
		else
		{
			if (rowDelta > 0)
				move (3);
			else	
				move (4);
				
		}
	}
	protected void diagChase (Player player)
	{//method to chase Player, by first creating a variable that colDelta and rowDelta
	 //   these variables hold the creature's position minus the players position
		int colDelta = getPos().c - player.getPos().c;
		int rowDelta = getPos().r - player.getPos().r;

		boolean horzMove = (Math.abs(colDelta) > Math.abs(rowDelta));
		// the actual move, which is dependent on the variables just assigned
		//	each move (1,2,3, or 4) is referred to below and will move it on step toward the Player position
		if (horzMove)
		{
			if (colDelta > 0 && rowDelta > 0 ){
				move (1);
				move (3);
			}
			else if (colDelta < 0 && rowDelta < 0){
				move (2);
				move (4);	
			}
		}
		else
		{
			if (colDelta > 0 && rowDelta < 0){
				move (1);
				move (4);
			}
			else if (colDelta < 0 && rowDelta > 0 ){
				move (2);
				move (3);
			}
		}
	}
	
	protected void wander(){
		Random temp = new Random();
		int x=temp.nextInt(5);
		move(x);
	}
	protected void teleport(){
		Random temp = new Random();
		int x=temp.nextInt(5);
		int rand=temp.nextInt(100);
		if (rand%13==0){
			jumpMove(x);
		}
	}
	protected void diagJump(){
		Random temp=new Random();
		int x=temp.nextInt(5);
		diagMove(x);
		
	}
}	

