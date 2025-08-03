package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class servidores extends Thread

{
    
  public static void main (String args[])
  {
    ServerSocket sfd = null;
    try
    {
      sfd = new ServerSocket(9000);
    }
    catch (IOException ioe)
    {
      System.out.println("Comunicación rechazada."+ioe);
      System.exit(1);
    }

    while (true)
    {
      try
      {
        Socket nsfd = sfd.accept();
        System.out.println("Conexion aceptada de: "+nsfd.getInetAddress());
        
        File directory = new File("rec/data");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        DataInputStream oos = new DataInputStream(nsfd.getInputStream());
        DataOutputStream escritor = new DataOutputStream(new FileOutputStream(new File("rec/data/serie_respaldo.dat")));
        int unByte;
        try {
			while ((unByte = oos.read()) != -1)
			   escritor.write(unByte);
			oos.close();
	        escritor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
      } 
      catch(IOException ioe)
      {
        System.out.println("Error: "+ioe);
      }
    }
  }
}