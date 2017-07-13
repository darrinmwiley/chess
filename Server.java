
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

	private ServerSocket server;
	private Socket socket;
	private static final int maxClients =2;
	private String ip;
	private static final ClientThread[] threads = new ClientThread[maxClients];
	public Server(int p) throws Exception
	{
		ip = InetAddress.getLocalHost().getHostAddress()+"";
		int port = p;	
		try{
			server = new ServerSocket(port);
		}catch(Exception ex){
			System.out.println("Port Occupied");
			System.exit(0);
		}
		
		System.out.println("Server Initiated:\nPort #:"+port+"\nIP:"+ip);	
		
	}
	public void run()
	{
		while(true)
		{
			try{
				socket = server.accept();
				int i = 0;
				for(;i<maxClients;i++)
				{
					if(threads[i]==null)
					{
						threads[i] = new ClientThread(socket,threads);
						threads[i].start();	
						break;
					}
				}
				if(i==maxClients){
					PrintStream os = new PrintStream(socket.getOutputStream());
					os.println("server too busy");
					os.close();
					socket.close();
				}
			}catch(Exception ex){}
		}
	}
}
class ClientThread extends Thread{

	protected String name;
	BufferedReader input;
	ObjectOutputStream output;
	private Socket socket;
	private final ClientThread[] threads;
	private int maxClients;
	public ClientThread(Socket so,ClientThread[] th){
		socket = so;
		threads = th;
		maxClients = threads.length;
    }

    public void run(){
    	try{
    		setupStreams();   		

    		init();

    		exit();

    		close();

    	}catch(Exception ex){}
    }

    public void init()throws Exception
    {
    	try{
    		while(true)
    		{ 
    			String line = input.readLine();
	    		broadcast(line);
    		}
    	}catch(Exception ex){return;}
    }

   	public synchronized void broadcast(String str)throws Exception
   	{
   			
   		for(int i = 0; i<maxClients;i++)
    		if(threads[i] != null&&threads[i]!=this)
    		{
    			threads[i].output.flush();
    			threads[i].output.writeObject(str);
    		}
    			
   	}

   	public synchronized void exit()throws Exception
   	{
    	for(int i = 0;i<maxClients;i++)
    		if(threads[i]==this)
    			threads[i]=null;
   	}

   	public void close()throws Exception
   	{
   		input.close();
   		output.close();
   		socket.close();
   	}

    public void setupStreams()throws Exception
    {
    	input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	output = new ObjectOutputStream(socket.getOutputStream());
    }
}