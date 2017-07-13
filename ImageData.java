import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageData {

	public static String[] strs = "King Queen Bishop Rook Knight Pawn".split(" ");
	public static BufferedImage[][] pieces = new BufferedImage[2][6];
	public static BufferedImage[] disks = new BufferedImage[5];
	public static BufferedImage checkersBoard,othelloBoard,chessBoard,otherSprites,sprites,moveOverlay,selectionOverlay; 
	public static void init()
	{
		try{
			chessBoard = ImageIO.read(new File("graphics/chessBoard.png"));
			checkersBoard = ImageIO.read(new File("graphics/checkersBoard.png"));
			othelloBoard = ImageIO.read(new File("graphics/othelloBoard.png"));
			sprites = ImageIO.read(new File("graphics/chessSprites.png"));
			otherSprites = ImageIO.read(new File("graphics/sprites.png"));
			moveOverlay = ImageIO.read(new File("graphics/space.png"));
			selectionOverlay = ImageIO.read(new File("graphics/selection.png"));
		}catch(Exception ex){}
		for(int i = 0;i<pieces[0].length;i++)
		{
			pieces[0][i] = sprites.getSubimage(i*32,0,32,64);
			pieces[1][i] = sprites.getSubimage(i*32,64,32,64);
		}
		for(int i = 0;i<5;i++)
		{
			disks[i] = otherSprites.getSubimage(i*64,0,64,64);
		}
	}
	
	public static BufferedImage getSprite(String s,boolean white)
	{
		int i =0;
		for(;!s.equals(strs[i]);i++){}
		return pieces[white?1:0][i];
	}
	
	public static BufferedImage getSprite(Piece p)
	{
		if(p instanceof ChessPiece)
			return getSprite(p.pieceName(),p.isWhite());
		switch(p.pieceName())
		{
			case "Red King":return disks[2];
			case "Black King":return disks[4];
			case "Red Checker":return disks[1];
			case "White Disk":return disks[0];
			default:return disks[3];
		}	
	}
}
