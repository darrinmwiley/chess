import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PieceRenderer {
	Piece piece;
	public PieceRenderer(Piece p)
	{
		piece = p;
	}
	
	public void render(int r, int c,Graphics window)
	{
		BufferedImage sprite = ImageData.getSprite(piece);
		window.drawImage(sprite,r,c,null);
	}
	
}
