
public class Player extends Movement
{ public Player(Map map, int r, int c) {
		super(map, r, c);
	}
	public void PlaceBomb(Map map, int r, int c){
			new PlayerBomb(map, r, c);
			System.out.println("bomb deployed");
	}
}
