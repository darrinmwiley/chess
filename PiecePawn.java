import java.util.HashSet;

public class PiecePawn extends ChessPiece{

	private boolean hasMoved,empassant;

    public PiecePawn(ChessBoard b,Location loc,boolean w)
    {
    	super(b,loc,w);
    }
    
    public String pieceName()
    {
    	return "Pawn";
    }
    
    public void moveTo(Location loc)
    {
    	boolean flag = false;
    	if(loc.col!=location.col&&board.get(loc)==null)
    		board.set(Location.get(location.row,loc.col),null);
    	if(Math.abs(loc.row-location.row)==2)
    	{
    		flag = true;
    	}
    	if(!hasMoved)
    		hasMoved = true;
    	super.moveTo(loc);
    	if(flag)
    		empassant = true;
    }
    
    public HashSet<Location> getMoveLocations()
    {
    	HashSet<Location> set = new HashSet<Location>();
    	
    	Location loc = Location.get(location.row-1,location.col);
    	if(loc.inBounds())
    	{
    		if(board.get(loc)==null)
    			set.add(loc);
    	}
    	if(!hasMoved&&board.get(Location.get(location.row-2,location.col))==null&&!set.isEmpty())
    		set.add(Location.get(location.row-2,location.col));
    	loc = Location.get(location.row-1,location.col-1);
    	Piece p;
    	if(loc.inBounds())
    	{
	    	p = board.get(loc);
	    	if(p!=null&&p.isEnemy(this))
	    		set.add(loc);	
    	}
    	loc = Location.get(location.row-1,location.col+1);
    	if(loc.inBounds())
    	{
    		p = board.get(loc);
    		if(p!=null&&p.isEnemy(this))
    			set.add(loc);
    	}
    	loc = Location.get(location.row,location.col+1);
    	if(loc.inBounds())
    	{
    		p = board.get(loc);
    		if(p!=null&&p.isEnemy(this)&&p.pieceName().equals("Pawn")&&((PiecePawn)p).empassant)
    		{
    			set.add(Location.get(location.row-1,location.col+1));
    		}
    	}	
    	loc = Location.get(location.row,location.col-1);	
    	if(loc.inBounds())
    	{
    		p = board.get(loc);
    		if(p!=null&&p.isEnemy(this)&&p.pieceName().equals("Pawn")&&((PiecePawn)p).empassant)
    		{
    			set.add(Location.get(location.row-1,location.col-1));
    		}
    	}
    	HashSet<Location> rem = new HashSet<Location>();
    	for(Location loca:set)
    		if(!canMoveTo(loca))
    			rem.add(loca);
    	set.removeAll(rem);
    	return set;
    }

	public void cancelEmpassant()
	{
		if(empassant)
			empassant = false;
	}
    
}