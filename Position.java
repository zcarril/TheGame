
//basic class used throughout project to clarify positioning
public class Position
{//initial integers for row(r) and column(c)
	protected int r;
	protected int c;

	public Position (Position p) {
		this (p.r, p.c);
	}

	public Position (int r, int c) {
		this.r = r;
		this.c = c;
	}
	//pinpoint position
	public boolean equals (Position pos)
	{
		if ((r == pos.r) && (c == pos.c))
			return true;
		else
			return false;
	}
	//used for offsetting position by desired position
	public Position offset (Position pos) {
		return new Position (r - pos.r, c - pos.c);
	}
}

