import java.awt.Graphics;

public class OthelloBoard extends Board{

	 public OthelloBoard() {
		 super();
		 renderer = new BoardRendererOthello(this,ImageData.othelloBoard);
	 }
	
	@Override
	public boolean canAnyPiecesMove(boolean white) {
		for(int r = 0;r<8;r++)
		{
			for(int c =0;c<8;c++)
			{
				if(canPlace(Location.get(r,c),white))
					return true;
			}
		}
		return false;
	}
	
	public void flip(int r, int c, int dr, int dc,boolean white)
	{
		if(get(r,c).isWhite()==white)
		{
			((Disk)get(r,c)).flip();
			flip(r+dr,c+dc,dr,dc,white);
		}		
	}
	
	public void place(Location loc,boolean white)
	{
		set(loc,new Disk(this,loc,white));
		int[][] ints = new int[][]{{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
		for(int i = 0;i<8;i++)
		{
			Location l = Location.get(loc.row+ints[0][i],loc.col+ints[1][i]);
			if(l.inBounds()&&get(l)!=null&&get(l).isWhite()^white&&search(l.row,l.col,ints[0][i],ints[1][i],white))
				flip(l.row,l.col,ints[0][i],ints[1][i],!white);
		}		
	}
	
	public boolean search(int r, int c,int dr, int dc,boolean white)
	{
		return(!Location.get(r,c).inBounds()||board[r][c]==null)?false:board[r][c].isWhite()==white||search(r+dr,c+dc,dr,dc,white);	
	}
	
	public boolean canPlace(Location loc,boolean white)
	{
		if(get(loc)!=null)
			return false;
		int[][] ints = new int[][]{{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
		for(int i = 0;i<8;i++)
		{
			Location l = Location.get(loc.row+ints[0][i],loc.col+ints[1][i]);
			if(l.inBounds()&&get(l)!=null&&get(l).isWhite()^white&&search(l.row,l.col,ints[0][i],ints[1][i],white))
				return true;
		}	
		return false;
	}

	@Override
	public Board clone() {
		return null;
	}
	
	public void render(Graphics g)
	{
		((BoardRendererOthello)renderer).render(g);
	}

}
