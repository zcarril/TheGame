import java.util.Random;

public class Map
{	//creating necessary items to build map
	protected final static int INVALID_SQUARE = -1;
	protected final static int OPEN_SPACE = 0;
	protected final static int WALL_SPACE = 1;
	protected final static int START_SPACE = 2;
	protected final static int FINISH_SPACE = 3;
	protected final static int ROOM_SPACE = 4;
	
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
		int x1=0;
		int y1=0;
		int c1=0;
		int c2=0;
		int c3=0;
		int c4=0;
		//if square is on the panel, makes it a WALL_SPACE


		
		
		for (int r = 0; r < height; r++){
			for (int c = 0; c < width; c++){
				 if (r< 1 || r > 4){	
						squares[r][c] = WALL_SPACE;
				}//if not wall then it is OPEN_SPACE
			}
		}
		for(int r =0; r<height; r++){
			x1=temp.nextInt((45 -5)+5);
			y1=temp.nextInt((80-5)+5);	
				if (y1%7==0|| x1%11==0){
					for (int rr=5;rr<25;rr++){
						for (int cc=y1;cc<y1+2;cc++){
							 if(cc>3)squares[rr][cc]=OPEN_SPACE;
							 c1+=1;
							 if(c1==2)break;
						}						 
					}
				}
				if (x1%9==0|| y1%13==0){
					for (int rr=20;rr<47;rr++){
						for (int cc=y1;cc<y1+2;cc++){
							if(cc>3)squares[rr][cc]=OPEN_SPACE;
							c2+=1;
							if(c2==2)break;
						}						 
					}
				}
				if (y1%6==0|| x1%17==0){
					for (int cc=5;cc<50;cc++){
						for (int rr=x1;rr<x1+2;rr++){
							squares[rr][cc]=OPEN_SPACE;
							c3+=1;	 
							if(c3==2)break;
						}						
					}
				}
				if (x1%8==0|| y1%15==0){
					for (int cc=40;cc<85;cc++){
						for (int rr=x1;rr<x1+2;rr++){
							squares[rr][cc]=OPEN_SPACE;
							c4+=1;
							if(c4==2)break;
						}						
					}
				}
		}
		for (int r= 5; r< 25;r++)
			for (int c=10; c<13;c++)
				squares[r][c]=OPEN_SPACE;
		for (int r= 5; r<25;r++)
			for (int c=80;c<82;c++)
				squares[r][c]=OPEN_SPACE;
		for (int r = 10; r < height; r++){
			x=temp.nextInt((35 -10)+10);
			y=temp.nextInt((75-10)+10);
			for (int c = 10; c < width; c++){
				if ((x%2==0 && x%3==0)){
					for (int rr=x;rr<x+15;rr++){
						for (int cc=y;cc<y+15;cc++){
							if (x>10 && y>10) squares[rr][cc]=ROOM_SPACE;
						}
					}
				}	
			}			
		}	
		for (int r=0; r<1;r++)
			for (int c=0; c<width-1;c++)
				squares[r][c]=WALL_SPACE;
		for (int r=0; r<5;r++)
			for (int c=30; c<70;c++)
				squares[r][c]=WALL_SPACE;

		//designating Start and Finish using methods below
		for (int c=1; c<5;c++){
			setStart (c, 0);
		}
		setFinish(4,width-1);
		for (int c =1; c<5; c++ )
			squares[c][width-2]=WALL_SPACE;
		
	//		for (int c =1; c<5; c++ ){
//				setFinish (c, width-1);	
//		}
	}
	public void clearFinish(int x){		
		if (x==0){
			for (int c =3; c<5; c++ ){
				squares[c][width-2]=OPEN_SPACE;	
			}
			squares[2][width-3]=WALL_SPACE;
			squares[2][width-4]=WALL_SPACE;
			System.out.println("Finish OPEN");
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
