
import java.awt.Graphics;
import java.util.HashSet;




public abstract class Board {

	public Piece[][] board;
	public BoardRenderer renderer;
    public Board() {
    	board = new Piece[8][8];
    }
    
    public abstract boolean canAnyPiecesMove(boolean white);
    
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
    public abstract Board clone();
    
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

}