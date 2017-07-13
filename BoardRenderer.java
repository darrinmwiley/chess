import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class BoardRenderer {

	Board board;
	BufferedImage image;
	public BoardRenderer(Board b,BufferedImage i)
	{
		board = b;
		image = i;
	}
	
	public void render(Location loc,HashSet<Location> locs,Graphics g)
	{
		g.drawImage(image,0,0,null);
		if(loc!=null)
			g.drawImage(ImageData.selectionOverlay,loc.col*64,loc.row*64,null);
		for(Location l:locs)
			g.drawImage(ImageData.moveOverlay,l.col*64,l.row*64,null);
		for(int r = 0;r<8;r++)
		{
			for(int c = 0;c<8;c++)
			{
				Location loc1 = Location.get(r,c);
				Piece p = board.get(loc1);
				if(p!=null)
				{
					p.render(c*64+16,r*64,g);
				}
			}
		}
	}
	
}
