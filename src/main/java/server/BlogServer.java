package server;

import java.io.*;
import java.net.*;

public class BlogServer {

	private static ServerSocket server;
	private static Socket connection;

	// constructor
	public BlogServer(){
	}

	public static void main(String[] args) throws Exception {
				// Creates an object 'newServer', which will later hold all connections (?)
		try{
			server = new ServerSocket(4040, 100);		// portnumber, 100 connection can wait at the port (called backlog)
			while(true){								// look forever for new connections
					connection = server.accept();	 	// accepts if connection is possible and creates socket
					Receptor receptor=new Receptor(connection);
					receptor.start();
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
									// Start running 'server'.
	}


}

