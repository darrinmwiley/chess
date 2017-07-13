
import java.util.HashSet;

public class Disk extends Piece{

	public Disk(Board b, Location loc, boolean white) {
		super(b, loc, white);
	}

	@Override
	public HashSet<Location> getMoveLocations() {
		return null;
	}

	@Override
	public boolean canMoveTo(Location loc) {
		return false;
	}

	@Override
	public String pieceName() {
		return isWhite?"White Disk":"Black Disk";
	}
	
	public void flip()
	{
		isWhite = !isWhite;
	}

	@Override
	public void moveTo(Location loc) {}

	@Override
	public String toString() {
		return pieceName();
	}
}
