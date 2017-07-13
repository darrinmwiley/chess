import java.awt.Graphics;
import java.util.HashSet;

public class CheckerBoard extends Board {

	public CheckerBoard()
	{
		super();
		renderer = new BoardRendererCheckers(this,ImageData.checkersBoard);
	}
	
	@Override
	public boolean canAnyPiecesMove(boolean white) {
		for(int i = 0;i<8;i++)
		{
			for(int j = 0;j<8;j++)
			{
				CheckerPiece p = (CheckerPiece) get(i,j);
				if(p==null)
					continue;
				if(!p.getMoveLocations().isEmpty())
					return true;
				if(p.getJumpLocations().size()>1)
					return true;
			}
		}
		return false;
	}
	
	public boolean isOutOfPieces(boolean white)
	{
		for(int i =0;i<8;i++)
		{
			for(int j = 0;j<8;j++)
			{
				CheckerPiece p = (CheckerPiece) get(i,j);
				if(p==null)
					continue;
				if(p.isWhite()==white)
					return false;
			}
		}
		return true;
	}

	@Override
	public Board clone() {
	   	Piece[][] p = new Piece[8][8];
	    for(int r = 0;r<8;r++)
	    {
	    	for(int c = 0;c<8;c++)
	    	{
	    		p[r][c] = board[r][c];
	    	}
	    }
	    CheckerBoard b = new CheckerBoard();
	    b.board = p;
		return b;
	}
	
	public void render(String s,HashSet<Location>loc,Graphics g)
	{
		((BoardRendererCheckers)renderer).render(s, loc, g);
	}
	
}
