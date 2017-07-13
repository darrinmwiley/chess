import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CheckersClient extends JPanel implements Runnable,Client {

	private static Socket socket;
	PrintStream output;
	ObjectInputStream input;
	BufferedImage back;
	CheckerBoard board;
	Location selectedLocation;
	boolean white,turn,closed;
	String locations = "";

	public CheckersClient(int port,String ip,boolean w, ObjectInputStream i, PrintStream o){
		input = i;
		output = o;
		white = w;
		turn = white;
    	InputListener listener = new InputListener(this);
    	addMouseListener(listener);
    	setupNetwork(port,ip);
    	new Thread(listener).start();
    	new Thread(new PainterThread2(this)).start();
    	board = new CheckerBoard();
    	setVisible(true);
    	setBoard();
    	
    }
	
	public void setBoard() {
		for(int i = 0;i<3;i++)
			for(int j = i%2==0?1:0;j<8;j+=2)
				board.set(Location.get(i,j),new CheckerPiece(board, Location.get(i,j),!white, false));
		for(int i = 5;i<8;i++)
			for(int j = i%2==0?1:0;j<8;j+=2)
				board.set(Location.get(i,j),new CheckerPiece(board, Location.get(i,j),white, false));
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
    	int i = e.getButton();
    	Piece p = null;
    	if(selectedLocation!=null)
    		p = board.get(selectedLocation);
    	Location l = Location.get(posY,posX);
    	Piece np = board.get(l);
    	if(i==1)
    	{
    		
    		if(locations.equals(selectedLocation+"")&&p!=null&&p.getMoveLocations().contains(l))
    		{	
    			send(p.getLocation().row+" "+p.getLocation().col+" "+posY+" "+posX);
    			p.moveTo(l);
    			turn = false;
    			selectedLocation = null;
    			locations = "";
    		}
    		else if(!locations.equals(selectedLocation+"")&&locations.trim().endsWith(l+""))
    		{
    			sendMoves();
    			selectedLocation = null;
    			locations = "";
    			turn = false;
    		}
    		else if(np!=null&&np.isWhite()==white)
    		{
    			locations = l+"";
    			selectedLocation = l;
    		}
    		for(int j = 0;j<8;j++)
    			if(board.get(0,j)!=null&&board.get(0,j).isWhite()==white)
    			{
    				((CheckerPiece)(board.get(0,j))).king = true;
    				send("king "+0+" "+j);
    			}
    			
    		
    	}
    	if(i==3)
    	{
    		if(selectedLocation!=null)
    		{
    			if(test(locations+" "+l))
	    		{

	    			locations+=" "+l;
	    		}
	    		else{

	    			locations = selectedLocation+"";
	    		}
    		}
    	}
    	
    		
    }
    
    private void sendMoves() throws Exception {
		String str = locations;
		while(str.length()!=3)
		{
			Location a = Location.parseLocation(str.substring(0,3));
			Location b = Location.parseLocation(str.substring(4,7));
			str = str.substring(3).trim();
			board.get(a).moveTo(b);
			send(a.row+" "+a.col+" "+b.row+" "+b.col);
		}
	}

	public boolean test(String s)
    {
    	if(selectedLocation==null)
    		return false;
    	CheckerPiece p = (CheckerPiece) board.get(selectedLocation);
    	if(p!=null)
    		for(String str:p.getJumpLocations())
    			if(str.contains(s))
    				return true;
    	return false;
    }
    
    public boolean test()
    {
    	return test(locations);
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
		
		
		((CheckerBoard)board).render(locations,set,window);
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
    	if(s.startsWith("king"))
    	{
    		int r = 7-Integer.parseInt(s.substring(5,6));
    		int c = 7-Integer.parseInt(s.substring(7,8));
    		((CheckerPiece)(board.get(r,c))).king = true;
    	}
    	else
    	{
    		String[] strs = translate(s).split(" ");
	    	int r = Integer.parseInt(strs[0]);
	    	int c = Integer.parseInt(strs[1]);
	    	int r1 = Integer.parseInt(strs[2]);
	    	int c1 = Integer.parseInt(strs[3]);
	    	board.get(r,c).moveTo(Location.get(r1,c1));
	    	turn = true;
	    	if(!board.canAnyPiecesMove(white))
	    	{
	    		send("end Tie Game!");
	    		receive("end Tie Game!");
	    	}	
	    	if(board.isOutOfPieces(white))
	    	{
	    		send("end You Win!");
	    		receive("end You Lose!");
	    	}	
    	}  	
    }
    
}
class PainterThread2 implements Runnable{

	CheckersClient chess;
	public PainterThread2(CheckersClient preGameClient)
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
