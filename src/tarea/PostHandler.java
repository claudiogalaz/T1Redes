package tarea;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;

public class PostHandler 
{   
	private static final int fNumberOfThreads = 100;
	private static final Executor fThreadPool = Executors.newFixedThreadPool(fNumberOfThreads);

	public static void main(String[] args) throws IOException 
	{ 

		ServerSocket socket = null;
		try
		{
		
			socket = new ServerSocket(8080);
			
	  		while (true) 
	  		{ 
	  			final Socket connection = socket.accept();
	  			Runnable task = new Runnable() 
	  			{ 
	  				@Override 
	  				public void run() 
	  				{ 
	  					HandleRequest(connection);
	  				} 
	  			};
				fThreadPool.execute(task);
			}
		}
		catch (IOException e)
		{			
		}
		finally
		{
			try 
	        {
	        	socket.close();
	        } 
	        catch (IOException e1)
	        {
	            e1.printStackTrace(System.err);
	        }
		}
    }
   

	private static void HandleRequest(Socket s) 
	{ 
		BufferedReader in;
		PrintWriter out;
		String request, a;

 		try 
 		{
 			InputStream archivo = new FileInputStream ("home.html");
 			String home = IOUtils.toString(archivo, "UTF-8");
 			
 			String webServerAddress = s.getInetAddress().toString();
 			System.out.println("New Connection:" + webServerAddress);
 			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

 			request = in.readLine();
 			a = in.readLine();
 			System.out.println("--- Client request: " + request + a);

 			out = new PrintWriter(s.getOutputStream(), true);
 			out.println(home);
 			out.flush();
 			out.close();
 			s.close();
 		} 
 		catch (IOException e) 
 		{ 
 			System.out.println("Failed respond to client request: " + e.getMessage());
 		} 
 		finally 
 		{ 
 			if (s != null) 
 			{ 
 				try 
 				{ 
 					s.close();
 				} 
 				catch (IOException e) 
 				{ 
 					e.printStackTrace();
 				} 
 			} 
 		} 
 		return;
 	}   
	
	private static void HandlePost()
	{
		blah
	}
 }