import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ChessClient extends JPanel implements Runnable,Client {

	private static Socket socket;
	final JButton knight,bishop,rook,queen;
	PrintStream output;
	ObjectInputStream input;
	BufferedImage back;
	ChessBoard board;
	Location selectedLocation;
	boolean white,turn,closed;
	String selection = "";

	public ChessClient(int port,String ip,boolean w, ObjectInputStream i, PrintStream o){
		input = i;
		output = o;
		white = w;
		knight = new JButton();
		knight.setIcon(new ImageIcon(ImageData.getSprite("Knight",white)));
		knight.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
			    Window w = SwingUtilities.getWindowAncestor(knight);
			    if (w != null) {
			      w.setVisible(false);
			      selection = "k";
			    }
			  }
			});
		bishop = new JButton();
		bishop.setIcon(new ImageIcon(ImageData.getSprite("Bishop",white)));
		bishop.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
			    Window w = SwingUtilities.getWindowAncestor(bishop);
			    if (w != null) {
			      w.setVisible(false);
			      selection = "b";
			    }
			  }
			});
		rook = new JButton();
		rook.setIcon(new ImageIcon(ImageData.getSprite("Rook",white)));
		rook.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
			    Window w = SwingUtilities.getWindowAncestor(rook);
			    if (w != null) {
			      w.setVisible(false);
			      selection = "r";
			    }
			  }
			});
		queen = new JButton();
		queen.setIcon(new ImageIcon(ImageData.getSprite("Queen",white)));
		queen.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
			    Window w = SwingUtilities.getWindowAncestor(queen);
			    if (w != null) {
			      w.setVisible(false);
			      selection = "q";
			    }
			  }
			});
		turn = white;
    	InputListener listener = new InputListener(this);
    	addMouseListener(listener);
    	setupNetwork(port,ip);
    	new Thread(listener).start();
    	new Thread(new PainterThread3(this)).start();
    	board = new ChessBoard();
    	setVisible(true);
    	setBoard();
    	
    }
	
    public void setBoard()
    {
    	board.set(Location.get(0,0), new PieceRook(board,Location.get(0,0),!white));
    	board.set(Location.get(0,1), new PieceKnight(board,Location.get(0,1),!white));
    	board.set(Location.get(0,2), new PieceBishop(board,Location.get(0,2),!white));
    	board.set(Location.get(0,3), white?new PieceKing(board,Location.get(0,3),!white):new PieceQueen(board,Location.get(0,3),!white));
    	board.set(Location.get(0,4), white?new PieceQueen(board,Location.get(0,4),!white):new PieceKing(board,Location.get(0,4),!white));
    	board.set(Location.get(0,5), new PieceBishop(board,Location.get(0,5),!white));
    	board.set(Location.get(0,6), new PieceKnight(board,Location.get(0,6),!white));
    	board.set(Location.get(0,7), new PieceRook(board,Location.get(0,7),!white));
    	board.set(Location.get(7,0), new PieceRook(board,Location.get(7,0),white));
    	board.set(Location.get(7,1), new PieceKnight(board,Location.get(7,1),white));
    	board.set(Location.get(7,2), new PieceBishop(board,Location.get(7,2),white));    	
    	board.set(Location.get(7,4), white?new PieceQueen(board,Location.get(7,4),white):new PieceKing(board,Location.get(7,4),white));
    	board.set(Location.get(7,3), white?new PieceKing(board,Location.get(7,3),white):new PieceQueen(board,Location.get(7,3),white));
    	board.set(Location.get(7,5), new PieceBishop(board,Location.get(7,5),white));
    	board.set(Location.get(7,6), new PieceKnight(board,Location.get(7,6),white));
    	board.set(Location.get(7,7), new PieceRook(board,Location.get(7,7),white));
    	for(int i = 0;i<8;i++)
    	{
    		board.set(Location.get(1,i), new PiecePawn(board,Location.get(1,i),!white));
        	board.set(Location.get(6,i), new PiecePawn(board,Location.get(6,i),white));
    	}
    }
	
	public void setupNetwork(int port,String ip)
	{
		try{
			socket = new Socket(InetAddress.getByName(ip),port);
			new Thread(this).start();
		}catch(Exception ex){
			System.exit(0);
		}
	}
	
    public void MousePressed(MouseEvent e)
    {
    	if(!turn)
    		return;
    	double wid = getWidth();
    	double ht = getHeight();
    	int mouseX = (int)(e.getX()*(512/wid));
    	int mouseY = (int)(e.getY()*(512/ht));
    	if(selectedLocation!=null)
    	{
    		Piece p = board.get(selectedLocation);
	    	if(p!=null&&!p.isWhite()^white&&p.getMoveLocations().contains(Location.get(mouseY/64,mouseX/64)))
	    	{
	    		if(turn&&p.isWhite()==white)
	    		{
	    			p.moveTo(Location.get(mouseY/64,mouseX/64));
	    			
	    			turn = false;
	    			try {
						send(selectedLocation.row+" "+selectedLocation.col+" "+mouseY/64+" "+mouseX/64);
					} catch (Exception e1) {
						System.exit(0);
					}
	    			for(int i = 0;i<8;i++)
	    			{
	    				Piece q = board.get(0,i);
	    				if(q!=null&&q.pieceName().equals("Pawn"))
	    				{
	    					JOptionPane.showOptionDialog(this, "Pick a piece to promote your pawn to:","Pawn Promotion",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, new Object[]{knight,bishop,rook,queen}, null);
	    					switch(selection.charAt(0))
	    					{
	    					case 'k':board.set(q.getLocation(),new PieceKnight(board,q.getLocation(),white));break;
	    					case 'q':board.set(q.getLocation(),new PieceQueen(board,q.getLocation(),white));break;
	    					case 'r':board.set(q.getLocation(),new PieceRook(board,q.getLocation(),white));break;
	    					case 'b':board.set(q.getLocation(),new PieceBishop(board,q.getLocation(),white));break;
	    					}
	    					try{
	    						send("prom "+q.getLocation().row+" "+q.getLocation().col+" "+selection.charAt(0));
	    					}catch(Exception ex){
	    						System.exit(0);
	    					}
	    				}
	    			}
	    			selectedLocation = null;		
	    		}
	    	}
	    	else
	    		selectedLocation = Location.get(mouseY/64,mouseX/64);
    	}
    	else
    		selectedLocation = Location.get(mouseY/64,mouseX/64);	
    }
    
    public String translate(String str)
    {
    	String[] strs = str.split(" ");
    	int r1 = Integer.parseInt(strs[0]);
    	int c1 = Integer.parseInt(strs[1]);
    	int r2 = Integer.parseInt(strs[2]);
    	int c2 = Integer.parseInt(strs[3]);
    	return 7-r1+" "+(7-c1)+" "+(7-r2)+" "+(7-c2);
    	
    }
    
    public void update(Graphics window)
    {
   		paint(window);
    }
    
    public void paint(Graphics g)
    {
    	Graphics2D twoDGraph = (Graphics2D)g;
		back = (BufferedImage)createImage(512,512);
		Graphics window = back.createGraphics();
		HashSet<Location> set = new HashSet<Location>();
		if(selectedLocation!=null)
		{
			Piece p = board.get(selectedLocation);
			if(p!=null&&p.isWhite()==white)
				set.addAll(p.getMoveLocations());
		}
		
		board.render(selectedLocation,set,window);
		AffineTransform trans = new AffineTransform();
		trans.scale((0.0+getWidth())/back.getWidth(),(0.0+getHeight())/back.getHeight());
		twoDGraph.drawImage(back,trans,null);
    }
    
	public void run()
   	{
   		try
   		{
   			String line;
	    	while((line = (String)input.readObject()) != null)
	    	{
	    		receive(line);
	    	}
	    	closed = true;
   			output.close();
			input.close();
			socket.close();
      	}catch(Exception e){System.exit(0);}
  	}
    
    public void send(String s) throws Exception
    {
    	output.println(s);
    }
    
    public void receive(String s) throws Exception
    {
    	if(s.startsWith("end"))
    	{
    		JOptionPane.showOptionDialog(this,s.substring(4),"Game Over",JOptionPane.INFORMATION_MESSAGE,JOptionPane.DEFAULT_OPTION,null,new Object[]{"OK"},null);
    		System.exit(0);
    		
    	}
    	else if(!s.startsWith("prom"))
    	{
    		String[] strs = translate(s).split(" ");
	    	int r = Integer.parseInt(strs[0]);
	    	int c = Integer.parseInt(strs[1]);
	    	int r1 = Integer.parseInt(strs[2]);
	    	int c1 = Integer.parseInt(strs[3]);
	    	board.get(r,c).moveTo(Location.get(r1,c1));
	    	turn = true;
	    	if(board.isStalemate(white))
	    	{
	    		send("end Tie Game!");
	    		receive("end Tie Game!");
	    	}	
	    	if(board.isCheckmate(white))
	    	{
	    		send("end You Win!");
	    		receive("end You Lose!");
	    	}	
    	}   	
    	else
    	{
    		String[] strs = s.split(" ");
    		int r = 7-Integer.parseInt(strs[1]);
    		int c = 7-Integer.parseInt(strs[2]);
    		char ch = strs[3].charAt(0);
    		Location loc = Location.get(r,c);
    		switch(ch)
			{
			case 'k':board.set(loc,new PieceKnight(board,loc,!white));break;
			case 'q':board.set(loc,new PieceQueen(board,loc,!white));break;
			case 'r':board.set(loc,new PieceRook(board,loc,!white));break;
			case 'b':board.set(loc,new PieceBishop(board,loc,!white));break;
			}
    	}
    	
    }
    
}
class PainterThread3 implements Runnable{

	ChessClient chess;
	public PainterThread3(ChessClient preGameClient)
	{
		chess = preGameClient;
	}
	
	@Override
	public void run() {	
		try{
			while(!chess.closed)
   			{
   				
   			   Thread.sleep(5);
        	   chess.repaint();
        	}
    	}catch(Exception ex){}
	}
	
}
