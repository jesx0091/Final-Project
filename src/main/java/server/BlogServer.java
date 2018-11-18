package server;
import request.PostRequest;
import request.PostSubmission;
import data.Post;
import data.Blog;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BlogServer{
	private ObjectOutputStream output;
	static private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	static Date timestamp = Calendar.getInstance().getTime();
	private static Post post = new Post("Author not initialized! Serverside.", "Tweet not initialized! Serverside.", timestamp);
	static PostRequest userRequest = new PostRequest(post,"0");
	static PostSubmission user = new PostSubmission(post);
	private static Blog blog = new Blog();
	private Object userObject = new Object();
	// constructor
	public BlogServer(){
	}

	public static void main(String[] args) throws Exception {
		BlogServer newServer = new BlogServer();			// Creates an object 'newServer', which will later hold all connections (?)
		newServer.startRunning();							// Start running 'server'.
	}

	// setup of server
	public void startRunning(){
		try{
			server = new ServerSocket(4040, 100);		// portnumber, 100 connection can wait at the port (called backlog)
			while(true){								// look forever for new connections
				try{
					connection = server.accept();	 	// accepts if connection is possible and creates socket
					streamSetup();
					doSomething();
				}catch(EOFException eofException){		// when someone ends the connection, the following message will be displayed. 
					System.out.print("Connection ended.");
				}finally{
					close();							// Method to close output, input and connection. 
				}
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	// Get stream to send and receive data
	private void streamSetup() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());	// creates object 'ObjectOutputStream'. From documentation about getOutputStream(): This method returns an OutputStream where the data can be written. 
		output.flush();													// cleans the stream, if all bytes were not correctly send. 
		input = new ObjectInputStream(connection.getInputStream());		// creates object 'ObjectInputStream'. From documentation about getInputStream(): This method returns an InputStream representing the data.
	}

	// to close streams and sockets when connection is ended
	private void close(){
		try{
			output.close();							// closes stream i&o
			input.close();
			connection.close();						// closes socket to not waste memory
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	private void doSomething(){
		try {
			userObject = (Object)input.readObject();		// reads object send from clientside

			if (userObject instanceof PostRequest){
				userRequest = (PostRequest) userObject;

				String request;
				request = userRequest.getRequestType();

				if(request.equals("1")){
					Post outputPost = new Post("Empty","Empty", timestamp);
					outputPost = blog.readOne();
					output.writeObject(outputPost);
				}else if(request.equals("2")){
					List<Post> outputList = new LinkedList<Post>();
					outputList = blog.readAll();
					output.writeObject(outputList);
				}else if(request.equals("3")){
					List<Post> outputList = new LinkedList<Post>();
					outputList = blog.readOwnPost(userRequest.getAuthor());
					output.writeObject(outputList);
				}

			}else if(userObject instanceof PostSubmission) {
				user = (PostSubmission) userObject;

				blog.addPost(user.getPost());
				blog.save();
				System.out.println("This is the latest post: " + (blog.readOne()).getTweet());

				System.out.println("And now read all:");
				List<Post> outputList = new LinkedList<Post>();
				outputList = blog.readAll();
				Post outputPost = new Post("default", "default", timestamp);
				for (Iterator<Post> it = outputList.iterator(); it.hasNext(); ) {
					outputPost = it.next();
					System.out.println(outputPost.getAuthor());
					System.out.println(outputPost.getTweet());
					System.out.println(outputPost.getTimestamp());
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
