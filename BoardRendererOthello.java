import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BoardRendererOthello extends BoardRenderer {

	public BoardRendererOthello(Board b, BufferedImage i) {
		super(b, i);
	}
	
	public void render(Graphics g)
	{
		g.drawImage(image,0,0,null);
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
