import java.util.HashSet;

public class PieceKing extends ChessPiece{
	
	private boolean hasMoved;
	
    public PieceKing(ChessBoard b,Location loc,Boolean w)
    {
    	super(b,loc,w);
    }
    
    public void moveTo(Location loc)
    {
    	int dif = loc.col-location.col;
    	if(Math.abs(dif)==2)
    	{
    		int i = (dif+2)*7/4;
    		Piece p = board.get(location.row,i);
    		super.moveTo(loc);
    		p.moveTo(Location.get(location.row,location.col+(dif==2?-1:1)));
    	}else
    		super.moveTo(loc);
    	if(!hasMoved)
    		hasMoved = true;
    	
    }
    
    public String pieceName()
    {
    	return "King";
    }
    
    public HashSet<Location> getMoveLocations()
    {
    	HashSet<Location> set = new HashSet<Location>();
    	int r = location.row;
    	int c = location.col;
    	int[][] m = new int[][]{{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
    	for(int i = 0;i<8;i++)
    	{
    		Location l = Location.get(r+m[0][i],c+m[1][i]);
    		if(canMoveTo(l)&&l.inBounds()&&!((ChessBoard) board).inCheck(isWhite,l)&&(board.get(l)==null||board.get(l).isEnemy(this)))
    		{
    			set.add(l);
    		}
    	}
    	if(canRightCastle())
    	{
    		set.add(Location.get(location.row,location.col+2));
    	}
    	if(canLeftCastle())
    	{
    		set.add(Location.get(location.row,location.col-2));
    	}
    	return set;
    }
    
    public boolean canRightCastle()
    {
    	if(hasMoved)
    		return false;
    	Piece p = board.get(location.row,7);
    	if(p!=null&&p.pieceName().equals("Rook")&&!((PieceRook)p).hasMoved())
    	{
    		for(int i = 0;i<3;i++)
    		{
    			Location loc = Location.get(location.row,location.col+i);
    			if(((ChessBoard) board).inCheck(isWhite,loc)||(board.get(loc)!=null&&board.get(loc)!=this))
    			{
    			    return false;	
    			}
    		}	
    		return true;		
    	}
    	return false;	
    }
    
    public boolean canLeftCastle()
    {
    	if(hasMoved)
    		return false;
    	Piece p = board.get(location.row,0);
    	if(p!=null&&p.pieceName().equals("Rook")&&!((PieceRook)p).hasMoved())
    	{
    		for(int i = 0;i>-3;i--)
    		{
    			Location loc = Location.get(location.row,location.col+i);
    			if(((ChessBoard) board).inCheck(isWhite,loc)||(board.get(loc)!=null&&board.get(loc)!=this))
    				return false;
    		}	
    		return true;		
    	}
    	return false;
    }
    
}