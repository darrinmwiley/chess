import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class OthelloClient extends JPanel implements Runnable,Client {

	private static Socket socket;
	PrintStream output;
	ObjectInputStream input;
	BufferedImage back;
	OthelloBoard board;
	boolean white,turn,closed;

	public OthelloClient(int port,String ip,boolean w, ObjectInputStream i, PrintStream o){
		input = i;
		output = o;
		white = w;
		turn = white;
    	InputListener listener = new InputListener(this);
    	addMouseListener(listener);
    	setupNetwork(port,ip);
    	new Thread(listener).start();
    	new Thread(new PainterThread(this)).start();
    	board = new OthelloBoard();
    	setVisible(true);
    	setBoard();
    	
    }
	
	public void setBoard() {
		board.set(Location.get(3,3),new Disk(board,Location.get(3,3),true));
		board.set(Location.get(4,4),new Disk(board,Location.get(4,4),true));
		board.set(Location.get(3,4),new Disk(board,Location.get(3,4),false));
		board.set(Location.get(4,3),new Disk(board,Location.get(4,3),false));
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
	
    public void MousePressed(MouseEvent e) throws Exception
    {
    	if(!turn)
    		return;
    	double wid = getWidth();
    	double ht = getHeight();
    	int mouseX = (int)(e.getX()*(512/wid));
    	int mouseY = (int)(e.getY()*(512/ht));
    	int posX = mouseX/64;
    	int posY = mouseY/64;
    	Location l = Location.get(posY,posX);
    	if(board.canPlace(l,white))
    	{
    		board.place(l,white);
    		send("place "+l.row+" "+l.col+" "+white);
    		turn = false;
    	}
    	
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
		((OthelloBoard)board).render(window);
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
    	if(s.startsWith("place"))
    	{
    		String[] strs = s.split(" ");
    		int r = Integer.parseInt(strs[1]);
    		int c = Integer.parseInt(strs[2]);
    		boolean w = Boolean.parseBoolean(strs[3]);
    		board.place(Location.get(r,c),w);
    		turn = true;
    	}
    	if(!board.canAnyPiecesMove(white))
    	{
    		String[] end = getEndText();
    		send("end "+end[0]);
    		receive("end "+end[1]);
    	}  	
    }
    
    public String[] getEndText()
    {
    	int black = 0;
    	int white = 0;
    	for(int r = 0;r<8;r++)
    	{
    		for(int c = 0;c<8;c++)
    		{
    			Disk p = (Disk)(board.get(r,c));
    			if(p==null)
    				continue;
    			if(p.isWhite()==this.white)
    				white++;
    			else
    				black++;
    		}
    	}
    	if(white>black)
    		return "You Lose!,You Win!".split(",");
    	if(white<black)
    		return "You Win!,You Lose!".split(",");
    	return "Tie Game!,Tie Game!".split(",");
    }
    
}
class PainterThread implements Runnable{

	OthelloClient chess;
	public PainterThread(OthelloClient preGameClient)
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
