import java.awt.Graphics;
import java.util.HashSet;

public abstract class ChessPiece extends Piece{
    
    public ChessPiece(ChessBoard b,Location loc,boolean white)
    {
    	super(b,loc,white);
    }
    
    public abstract HashSet<Location> getMoveLocations();
    public boolean canMoveTo(Location loc)
    {
    	if(!loc.inBounds())
    		return false;
    	return !((ChessBoard) board).isKingInCheckAfterMove(isWhite,location,loc);
    }
    public abstract String pieceName();
    public void moveTo(Location loc)
    {
    	board.set(location,null);
    	setLocation(loc);
    	board.set(loc,this);
    	cancelEmpassants();
    }
    public void cancelEmpassants()
    {
    	for(int r = 0;r<8;r++)
    	{
    		for(int c = 0;c<8;c++)
    		{
    			Location loc = Location.get(r,c);
    			Piece p = board.get(loc);
    			if(p!=null&&p.pieceName().equals("Pawn"))
    				((PiecePawn)p).cancelEmpassant();
    		}
    	}
    }
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
    	return isWhite^p.isWhite();
    }
    public boolean isWhite()
    {
    	return isWhite;
    }
    public String toString()
    {
    	return (isWhite?"White ":"Black ")+pieceName()+"\nLocation: "+getLocation();
    }
    public void render(int r, int c,Graphics window)
    {
    	renderer.render(r, c, window);
    }
    
}