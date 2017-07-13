import java.awt.Graphics;
import java.util.HashSet;

public abstract class Piece {
    
    protected Board board;
    protected Location location;
    protected boolean isWhite;
    protected PieceRenderer renderer;
    public Piece(Board b,Location loc,boolean white)
    {
    	board = b;
    	location = loc;
    	isWhite = white;
    	renderer = new PieceRenderer(this);
    }
    
    public abstract HashSet<Location> getMoveLocations();
    public abstract boolean canMoveTo(Location loc);
    public abstract String pieceName();
    public abstract void moveTo(Location loc);
    public Board getBoard()
    {
    	return board;
    }
    public Location getLocation()
    {
    	return location;
    }
    public void setLocation(Location loc)
    {
    	location = loc;
    }
    public boolean isEnemy(Piece p)
    {
    	return isWhite^p.isWhite;
    }
    public boolean isWhite()
    {
    	return isWhite;
    }
    public abstract String toString();
    
    public void render(int r, int c,Graphics window)
    {
    	renderer.render(r, c, window);
    }
}