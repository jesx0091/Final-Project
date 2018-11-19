// IST JAVA project
// JES RYDALL LARSEN
// MELANIE BRANDL
// IRENE ARROYO
package server;

import java.io.*;
import java.net.*;

public class BlogServer {

	private static ServerSocket server;
	private static Socket connection;

	public BlogServer(){
	}

	public static void main(String[] args) throws Exception {

		try{
			server = new ServerSocket(4040);
			while(true){								// look forever for new connections
					connection = server.accept();	 	// accepts if connection is possible and creates socket
					Receptor receptor=new Receptor(connection);
					receptor.start();
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}

	}


}

