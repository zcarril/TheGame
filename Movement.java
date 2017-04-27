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
	public Position shuffle(int r, int c){
		Position shuff=new Position(r,c);
		pos=shuff;
		return shuff;
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
	public void Gravity(){
		move(4);
	}
	protected void monsterMove (int dir)
	{
		switch (dir)
		{
		case 1:
			if (validMove(pos.r,pos.c-OneStep))
				pos.c -= (0.5*OneStep);
			break;
		case 2:
			if (validMove(pos.r,pos.c+OneStep))
				pos.c += (0.5*OneStep);
			break;					
		case 3:
			if (validMove(pos.r-(OneStep),pos.c))
				pos.r -= (OneStep);
			break;
		case 4:
			if (validMove(pos.r+OneStep,pos.c))
				pos.r += (0.5*OneStep);
			break;
		}
	}
}


