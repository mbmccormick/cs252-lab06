import java.util.*; 
import java.io.*; 
import java.net.*;

public class DirectoryServer implements Runnable
{
    private int _port = 0;   
    ArrayList<User> _directory = null;
 
    DirectoryServer(int port)
    {
        _port = port;
        _directory = new ArrayList<User>();
    }
    
    public void run()
    {
        System.out.println("Directory Server started.");
        
        try
        {
            // create a socket for handling incoming requests
            ServerSocket server = new ServerSocket(_port);

            while (true)
            {
                System.out.println("Listening for conections on port " + _port + "...");
                
                // wait for an incoming connection
                Socket clientSocket = server.accept();
                
                System.out.println("Connection received.");
                
                System.out.println("Launching new thread for connection...");
                
                // create a new connection for this socket
                DirectoryServerConnection cn = new DirectoryServerConnection(this, clientSocket);
                
                // launch a new thread for this connection
                Thread th = new Thread(cn);
                th.start();
            }
        }
        catch (IOException ex)
        {
            // TODO: handle this exception
        }
    }
}

