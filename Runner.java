import java.awt.Component;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Runner extends JFrame {
	private static final int WIDTH = 512;
	private static final int HEIGHT = 512;
	Server server = null;
    public Runner() throws Exception
    {	
    	super("Chess");
    	String ip = "localhost";
    	try {
			ip = InetAddress.getLocalHost().getHostAddress()+"";
		} catch (UnknownHostException e){e.printStackTrace();}
    	boolean isHosting = false;
    	JTextField portInput = new JTextField("2222");
    	JTextField ipInput = new JTextField(ip);
    	JComboBox<String> hostInput = new JComboBox<String>(new String[]{"Host","Guest"});
    	JComboBox<String> gameInput = new JComboBox<String>(new String[]{"Checkers","Chess","Othello"});
    	JOptionPane.showOptionDialog(this, "Enter port number and ip if not hosting","Server Setup",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, new Object[]{portInput,ipInput,hostInput,gameInput,"OK"}, null);
    	ImageData.init();
    	setSize(WIDTH,HEIGHT);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       	int port = 2222;
       	try{
       		port = Integer.parseInt(portInput.getText());
       	}catch(Exception ex){ex.printStackTrace();}	
       	ip = ipInput.getText();
       	isHosting = hostInput.getSelectedItem().equals("Host");
       	if(isHosting)
       		new Server(port).start();
      	new PreGameClient(port,ip,isHosting,this,(String) gameInput.getSelectedItem());	
    }
    
    public void startGame(int port,String ip,boolean wh,ObjectInputStream input,PrintStream output,String gameType)
    {
    	Client l = null;
    	if(gameType.equalsIgnoreCase("chess"))
    		l = new ChessClient(port,ip,wh,input,output);
    	else if(gameType.equalsIgnoreCase("checkers"))
    		l = new CheckersClient(port,ip,wh,input,output);
    	else
    		l = new OthelloClient(port,ip,wh,input,output);
    	getContentPane().add((Component) l);
    	this.setTitle(gameType);
	    setVisible(true);
    }
    
    public static void main(String[] args)
    {
    	try {
			new Runner();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }    
}