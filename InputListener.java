
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;


public class InputListener implements Runnable,KeyListener,MouseInputListener,MouseWheelListener{

	int lastMouseX,lastMouseY;
	Client panel;
	public InputListener(Client chessClient)
	{
		panel = chessClient;
	}

	public void mouseClicked(MouseEvent e){}

	public void mouseEntered(MouseEvent arg0){}

	public void mouseExited(MouseEvent arg0){}

	public void mousePressed(MouseEvent e)
	{
		try {
			panel.MousePressed(e);
		} catch (Exception e1) {}
	}

	public void mouseReleased(MouseEvent arg0){}

	public void mouseDragged(MouseEvent e){}

	public void mouseMoved(MouseEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent e){}

	public void keyPressed(KeyEvent arg0){}

	public void keyReleased(KeyEvent arg0){}

	public void keyTyped(KeyEvent arg0){}	
	
	public void run() 
	{
		for(;;){}
	}

}
