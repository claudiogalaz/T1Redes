package tarea;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.ws.Response;

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
 			
 			StringTokenizer st = new StringTokenizer(request);
 			String method = st.nextToken();
 			
 	        String uri = decodePercent(st.nextToken());
 	        
 	        //System.out.println(method + " BLAH " + uri);
 	        
 			//EMPIEZA COSA
 			// Decode parameters from the URI
 	        Properties parms = new Properties();
 	        int qmi = uri.indexOf('?');
 	        if (qmi >= 0) {
 	          decodeParms(uri.substring(qmi + 1), parms);
 	          uri = decodePercent(uri.substring(0, qmi));
 	        }
 			
 			Properties header = new Properties();
 			
 			if (st.hasMoreTokens()) {
 		          String line = in.readLine();
 		          while (line.trim().length() > 0) {
 		            int p = line.indexOf(':');
 		            header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
 		            line = in.readLine();
 		          }
 		        }
 			
 			//a = in.readLine();
 			//EMPIEZA POST
 			if (method.equalsIgnoreCase("POST")) {
 		          long size = 0x7FFFFFFFFFFFFFFFl;
 		         
 		          String contentLength = header.getProperty("content-length");
 		         //System.out.println(header);
 		          if (contentLength != null) {
 		            try {
 		              size = Integer.parseInt(contentLength);
 		            } catch (NumberFormatException ex) {
 		            }
 		          }
 		          //System.out.println("\n1\n");
 		          String postLine = "";
 		          char buf[] = new char[512];
 		          int read = in.read(buf);
 		         //System.out.println("\n2\n");
 		          while (read >= 0 && size > 0 && !postLine.endsWith("\r\n")) {
 		            size -= read;
 		            postLine += String.valueOf(buf, 0, read);
 		            if (size > 0)
 		              read = in.read(buf);
 		           //System.out.println("\nifff\n");
 		          }
 		         //System.out.println("\n3\n");
 		          postLine = postLine.trim();
 		          //System.out.println(postLine);
 		          decodeParms(postLine, parms);
 		          
 		          String name = parms.getProperty("Name");
 		        System.out.println(name);
 		        }
 			//TERMINA POST
 			
 			
 			
 			System.out.println("--- Client request: " + request);

 			out = new PrintWriter(s.getOutputStream(), true);
 			out.println(home);
 			out.flush();
 			out.close();
 			s.close();
 		} 
 		catch (IOException e) 
 		{ 
 			System.out.println("Failed respond to client request: " + e.getMessage());
 		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private static void decodeParms(String parms, Properties p) throws InterruptedException {
	      if (parms == null)
	        return;

	      StringTokenizer st = new StringTokenizer(parms, "&");
	      while (st.hasMoreTokens()) {
	        String e = st.nextToken();
	        int sep = e.indexOf('=');
	        if (sep >= 0)
	          p.put(decodePercent(e.substring(0, sep)).trim(), decodePercent(e.substring(sep + 1)));
	      }
	}
	private static String decodePercent(String str) {
	      try {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < str.length(); i++) {
	          char c = str.charAt(i);
	          switch (c) {
	          case '+':
	            sb.append(' ');
	            break;
	          case '%':
	            sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
	            i += 2;
	            break;
	          default:
	            sb.append(c);
	            break;
	          }
	        }
	        return new String(sb.toString().getBytes());
	      } catch (Exception e) {
	        
	      }
		return str;
	    }
	
	private static void HandlePost(Socket s)
	{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println(in.readLine());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 }