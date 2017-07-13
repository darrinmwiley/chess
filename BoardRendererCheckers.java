import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class BoardRendererCheckers extends BoardRenderer{

	public BoardRendererCheckers(Board b,BufferedImage i)
	{
		super(b,i);
	}
	
	public void render(String locations,HashSet<Location> locs,Graphics g)
	{
		g.drawImage(image,0,0,null);
		if(locations!=null)
			while(!locations.isEmpty())
			{
				Location loc = Location.parseLocation(locations.substring(0,3));
				locations = locations.substring(3).trim();
				g.drawImage(ImageData.selectionOverlay,loc.col*64,loc.row*64,null);
			}
		for(int r = 0;r<8;r++)
		{
			for(int c = 0;c<8;c++)
			{
				Location loc1 = Location.get(r,c);
				Piece p = board.get(loc1);
				if(p!=null)
				{
					p.render(c*64,r*64,g);
				}
			}
		}
	}
	
}
