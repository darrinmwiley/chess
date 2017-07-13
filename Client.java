
import java.awt.event.MouseEvent;

public interface Client{
	
	public void setupNetwork(int port,String ip);
	public void run();
	public void send(String s) throws Exception;
	public void receive(String s) throws Exception;
	public abstract void MousePressed(MouseEvent e) throws Exception;
}
