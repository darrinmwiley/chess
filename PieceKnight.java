import java.util.HashSet;

public class PieceKnight extends ChessPiece{

    public PieceKnight(ChessBoard b,Location loc,Boolean w)
    {
    	super(b,loc,w);
    }
    
    public String pieceName()
    {
    	return "Knight";
    }
    
    public HashSet<Location> getMoveLocations()
    {
    	HashSet<Location> set = new HashSet<Location>();
    	int[][] m = new int[][]{{-2,-2,-1,-1,1,1,2,2},{-1,1,-2,2,-2,2,-1,1}};
    	for(int i = 0;i<8;i++)
    	{
    		Location l = Location.get(location.row+m[0][i],location.col+m[1][i]);
    		if(l.inBounds())
    		{
    			Piece p = board.get(l);
    			if(p==null||p.isEnemy(this))
    				set.add(l);
    		}
    	}
    	HashSet<Location> rem = new HashSet<Location>();
    	for(Location loc:set)
    		if(!canMoveTo(loc))
    			rem.add(loc);
    	set.removeAll(rem);
    	return set;
    }
    
}