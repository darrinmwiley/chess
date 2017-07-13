import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class PreGameClient extends JPanel implements Runnable,Client {

	private static Socket socket;
	PrintStream output;
	ObjectInputStream input;
	boolean isHosting,wh;
	int port;
	String ip;
	Runner runner;
	String gameType;
	public PreGameClient(int p,String i,boolean host,Runner r, String t) throws Exception{
		setVisible(true);
		gameType = t;
		port = p;
		ip = i;
		isHosting = host;
		runner = r;
    	setupNetwork(port,ip);
    	new Thread(this).start();
    }
	
	public void setupNetwork(int port,String ip)
	{
		try{
			socket = new Socket(InetAddress.getByName(ip),port);
			output = new PrintStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			if(!isHosting)
			{
				send("init "+gameType);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
      
	public void run()
   	{	
   		try
   		{
	    	receive((String)input.readObject());
      	}catch(Exception e){System.exit(0);}
  	}
    
    public synchronized void send(String s) throws Exception
    {
    	output.flush();
    	output.println(s);
    }
    
    public synchronized void receive(String s) throws Exception
    {
    	if(s.startsWith("init "))
    	{
    		if(isHosting)
    		{
           		final JButton black = new JButton();
           		switch(gameType)
           		{
           		case "Chess":black.setIcon(new ImageIcon(ImageData.getSprite("King",false)));break;
           		default:black.setIcon(new ImageIcon(ImageData.getSprite(new CheckerPiece(null, null, false, false))));break;
           		}
           		black.addActionListener(new ActionListener() {
           			public void actionPerformed(ActionEvent evt) {
           				Window w = SwingUtilities.getWindowAncestor(black);
           					if (w != null) {
           						w.setVisible(false);
           						wh = false;
           					}
           			}
           		});
           		final JButton white = new JButton();
           		switch(gameType)
           		{
           		case "Chess":white.setIcon(new ImageIcon(ImageData.getSprite("King",true)));break;
           		case "Othello":white.setIcon(new ImageIcon(ImageData.disks[0]));break;
           		default:white.setIcon(new ImageIcon(ImageData.getSprite(new CheckerPiece(null, null, true, false))));break;
           		}
           		white.addActionListener(new ActionListener() {
           			public void actionPerformed(ActionEvent evt) {
        				Window w = SwingUtilities.getWindowAncestor(white);
        				if (w != null) {
        					w.setVisible(false);
        					wh = true;
        				}
        			}
        		});
           		JOptionPane.showOptionDialog(this, "Pick a color:","Color Selection",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, new Object[]{black,white}, null); 
           		send(wh?"init black "+gameType:"init white "+gameType);
           		runner.startGame(port,ip,wh,input,output,gameType);
    		}else
    		{
    			String[] strs = s.split(" ");
    			boolean white = strs[1].equals("white");
    			runner.startGame(port, ip, white,input,output,strs[2]);
    		}
    		
    	}
    }

	@Override
	public void MousePressed(MouseEvent e) {}
    
}