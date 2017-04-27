import java.util.Random;

public class Map
{	//creating necessary items to build map
	protected final static int INVALID_SQUARE = -1;
	protected final static int OPEN_SPACE = 0;
	protected final static int WALL_SPACE = 1;
	protected final static int START_SPACE = 2;
	protected final static int FINISH_SPACE = 3;
	
	private int[][] squares;
	private int width, height;
	private Position start = null;
	private Position finish = null;

	public Map (int width, int height)
	{//this is sets the width and height
		this.width = width;
		this.height = height;
		squares = new int[height][width];
		Random temp = new Random();
		int x=0;//x & y for creating and placing long walls
		int y=0;
		//if square is on the panel, makes it a WALL_SPACE
		for (int r = 0; r < height; r++){
			for (int c = 0; c < width; c++)
				if (r == 0 || r == height-1 || c == 0 || c == width-1){
					squares[r][c] = WALL_SPACE;
				}//if not wall then it is OPEN_SPACE
				else
					squares[r][c] = OPEN_SPACE;
		}
		//this loop creates random vertical walls
		//		for the size of the map
	/*	for(int r = 0; r < 68; r ++){
			x=(temp.nextInt(45));
			if (r>2){
				if(x>1){
					squares[x][r]=WALL_SPACE;
					squares[x+1][r]=WALL_SPACE;
					squares[x+2][r]=WALL_SPACE;
					squares[x+3][r]=WALL_SPACE;
					squares[x+4][r]=WALL_SPACE;
					squares[x+5][r]=WALL_SPACE;
				}
			}
		}*/
		//this loop creates random 6-square horizontal walls
		//	for the size of the map
		for( int c= 0; c< 98; c ++ ){
			y=(temp.nextInt(130));
			if (c%2==0){
				
					if (c>2){
						if(y>2){
							squares[c][y]=WALL_SPACE;
							squares[c][y+1]=WALL_SPACE;
							squares[c][y+2]=WALL_SPACE;
							squares[c][y+3]=WALL_SPACE;
							squares[c][y+4]=WALL_SPACE;
							squares[c][y+5]=WALL_SPACE;
							
							if(y % 2==0){
								squares[c][y+6]=WALL_SPACE;
								squares[c][y+7]=WALL_SPACE;
								squares[c][y+8]=WALL_SPACE;
								squares[c][y+9]=WALL_SPACE;
								squares[c][y+10]=WALL_SPACE;
							
						}
					}
				}				
			}
		}	
		for( int c =100; c< 140;c++){
			squares[10][c]=WALL_SPACE;
			}
		//designating Start and Finish using methods below
		setStart (98, 1);
		for (int c =5; c<10; c++ ){
			setFinish (c, width-2);
		}
	}
	//checks if a valid position,if so can be set as start
	public void setStart (int r, int c){
		Position p = new Position (r, c);
		if (validPosition (p)){
			squares[p.r][p.c] = START_SPACE;
			start = p;
		}
		else
			start = null;
	}

	protected Position getStart () {
		return start;
	}
	//checks if a valid position, if so can be set as finish
	public void setFinish (int r, int c){
		Position p = new Position (r, c);
		if (validPosition (p)){
			squares[p.r][p.c] = FINISH_SPACE;
			finish = p;
		}
		else
			finish = null;
	}
	//gets Finish 
	protected Position getFinish () {
		return finish;
	}
	//gets Width
	public int getWidth() {
		return width;
	}
	//gets Height
	public int getHeight(){
		return height;
	}
	//if not a valid position, the square position cannot be used
	protected int getSquare (int row, int col){
		if (validPosition (row, col))
			return squares[row][col];
		else
			return INVALID_SQUARE;
	}
	//gets the square's position
	protected int getSquare (Position pos) {
		return getSquare (pos.r, pos.c);
	}
	//if the row or column is negative or equal-to-or-greater-than
	//	the actual width or height, the position is NOT valid
	protected boolean validPosition (int row, int col){
		if (row < 0)
			return false;
		else if (row >= height)
			return false;
		if (col < 0)
			return false;
		else if (col >= width)
			return false;

		return true;
	}
	//boolean for validPosition
	protected boolean validPosition (Position pos) {
		return validPosition (pos.r, pos.c);
	}
}
