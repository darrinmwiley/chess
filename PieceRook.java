import java.util.HashSet;

public class PieceRook extends ChessPiece{
	
	private boolean hasMoved;
	
    public PieceRook(ChessBoard b,Location loc,Boolean w)
    {
    	super(b,loc,w);
    }
    
   	public boolean hasMoved()
   	{
   		return hasMoved;
   	}
    
    public void moveTo(Location loc)
    {
    	if(!hasMoved)
    		hasMoved = true;
    	super.moveTo(loc);
    }
    
    public String pieceName()
    {
    	return "Rook";
    }
    
    public HashSet<Location> getMoveLocations()
    {
    	HashSet<Location> set = new HashSet<Location>();
    	fillDir(set,location.row-1,location.col,-1,0);
    	fillDir(set,location.row+1,location.col,1,0);
    	fillDir(set,location.row,location.col-1,0,-1);
    	fillDir(set,location.row,location.col+1,0,1);
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