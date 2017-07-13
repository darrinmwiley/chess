import java.util.HashSet;

public class PieceBishop extends ChessPiece{
	
    public PieceBishop(ChessBoard b,Location loc,Boolean w)
    {
    	super(b,loc,w);
    }
    
    public String pieceName()
    {
    	return "Bishop";
    }
    
    public HashSet<Location> getMoveLocations()
    {
    	HashSet<Location> set = new HashSet<Location>();
    	fillDir(set,location.row+1,location.col-1,1,-1);
    	fillDir(set,location.row+1,location.col+1,1,1);
    	fillDir(set,location.row-1,location.col-1,-1,-1);
    	fillDir(set,location.row-1,location.col+1,-1,1);
    	HashSet<Location> rem = new HashSet<Location>();
    	for(Location loc:set)
    		if(!canMoveTo(loc))
    			rem.add(loc);
    	set.removeAll(rem);
    	return set;
    }
    
    public void fillDir(HashSet<Location> set,int r, int c,int dr, int dc)
    {
    	Location loc = Location.get(r,c);
    	if(loc.inBounds())
    	{
    		Piece p = board.get(loc);
    		if(p==null)
    		{
    			set.add(loc);
    			fillDir(set,r+dr,c+dc,dr,dc);
    		}
    		else if(p.isEnemy(this))
    			set.add(loc);
    	}
    }
    
}