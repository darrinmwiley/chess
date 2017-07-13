import java.awt.Graphics;
import java.util.HashSet;

public class ChessBoard extends Board{

	public BoardRenderer renderer;
    public ChessBoard() {
    	super();
    	renderer = new BoardRenderer(this,ImageData.chessBoard);
    }
    
    public boolean inCheck(boolean white,Location location)
    {
    	return searchDir(white,"QueenBishop",location.row+1,location.col-1,1,-1)||
    	searchDir(white,"QueenBishop",location.row+1,location.col+1,1,1)||
    	searchDir(white,"QueenBishop",location.row-1,location.col-1,-1,-1)||    	
    	searchDir(white,"QueenBishop",location.row-1,location.col+1,-1,1)||
    	searchDir(white,"QueenRook",location.row-1,location.col,-1,0)||
    	searchDir(white,"QueenRook",location.row+1,location.col,1,0)||
    	searchDir(white,"QueenRook",location.row,location.col-1,0,-1)||
    	searchDir(white,"QueenRook",location.row,location.col+1,0,1)||
    	searchKnight(white,location.row,location.col)||
    	searchPawn(white,location.row,location.col)||
    	searchKing(white,location.row,location.col);
    }
    
    public boolean canAnyPiecesMove(boolean white)
    {
    	for(int i = 0;i<8;i++)
    	{
    		for(int j = 0;j<8;j++)
    		{
    			Piece p = get(i,j);
    			if(p!=null&&p.isWhite()==white&&!p.getMoveLocations().isEmpty())
    				return true;
    		}
    	}
    	return false;
    }
    
    public boolean isStalemate(boolean white)
    {
    	return !canAnyPiecesMove(white)&&!inCheck(white,locateKing(white));
    }
    
    public boolean isCheckmate(boolean white)
    {
    	return !canAnyPiecesMove(white)&&inCheck(white,locateKing(white));
    }
    
    public boolean searchKnight(boolean white,int r, int c)
    {
    	int[][] m = new int[][]{{-2,-2,-1,-1,1,1,2,2},{-1,1,-2,2,-2,2,-1,1}};
    	for(int i = 0;i<8;i++)
    	{
    		Location l = Location.get(r+m[0][i],c+m[1][i]);
    		if(l.inBounds())
    		{
    			Piece p = get(l);
    			if(p!=null&&p.pieceName().equals("Knight")&&p.isWhite()^white)
    				return true;
    		}
    	}
    	return false;
    }
    
    public boolean searchPawn(boolean white,int r, int c)
    {
    	Location loc = Location.get(r-1,c-1);
    	if(loc.inBounds())
    	{
    		Piece p = get(loc);
    		if(p!=null&&p.pieceName().equals("Pawn")&&p.isWhite()^white)
    			return true;
    	}
 	  	loc = Location.get(r-1,c+1);
 	  	if(loc.inBounds())
 	  	{
 	  		Piece p = get(loc);
    		if(p!=null&&p.pieceName().equals("Pawn")&&p.isWhite()^white)
    			return true;
 	  	}
 	  	return false;
    }
    
    public boolean searchKing(boolean white,int r, int c)
    {
    	int[][] m = new int[][]{{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
    	for(int i = 0;i<8;i++)
    	{
    		Location l = Location.get(r+m[0][i],c+m[1][i]);
    		if(l.inBounds())
    		{
    			Piece p = get(l);
    			if(p!=null&&p.pieceName().equals("King")&&p.isWhite()^white)
    				return true;
    		}
    	}
    	return false;
    }
    
    public boolean searchDir(boolean white,String type,int r, int c,int dr, int dc)
    {
    	Location loc = Location.get(r,c);
    	if(loc.inBounds())
    	{
    		Piece p = get(loc);
    		if(p!=null)
    		{
    			if(p.isWhite()^white&&type.contains(p.pieceName()))
    				return true;
    			else if(p.isWhite()==white||!type.contains(p.pieceName()))
    				return false;
    		}
    		return searchDir(white,type,r+dr,c+dc,dr,dc);
    	}
    	return false;
    	
    }
    
    public void set(Location loc,Piece p)
    {
    	board[loc.row][loc.col] = p;
    }
    
    public Piece get(Location loc)
    {
    	return get(loc.row,loc.col);
    }
    
    public Piece get(int r, int c)
    {
    	return board[r][c];
    }
    public void render(Location loc,HashSet<Location> set,Graphics g)
    {
    	renderer.render(loc,set,g);
    }
    public Board clone()
    {
    	Piece[][] p = new Piece[8][8];
    	for(int r = 0;r<8;r++)
    	{
    		for(int c = 0;c<8;c++)
    		{
    			p[r][c] = board[r][c];
    		}
    	}
    	ChessBoard b = new ChessBoard();
    	b.board = p;
		return b;
    }
    public Location locateKing(boolean white)
    {
    	for(int r = 0;r<8;r++)
    	{
    		for(int c = 0;c<8;c++)
    		{
    			Piece p = board[r][c];
    			if(p!=null&&p.pieceName().equals("King")&&p.isWhite()==white)
    				return Location.get(r,c);
    		}
    	}
    	return null;
    }
    public String toString()
    {
    	String s = "";
    	for(int r = 0;r<8;r++)
    	{
    		for(int c = 0;c<8;c++)
    		{
    			Piece p = board[r][c];
    			if(p!=null)
    				s+=p+"\n";
    		}
    	}
    	return s;
    }
    public boolean isKingInCheckAfterMove(boolean white,Location l,Location m)
    {
    	ChessBoard clone = (ChessBoard) clone();
    	clone.set(m,clone.get(l));
    	clone.set(l,null);
    	Location king = clone.locateKing(white);
    	return clone.inCheck(white,king);
    }
}