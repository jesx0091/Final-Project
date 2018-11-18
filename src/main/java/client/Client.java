package client;
import request.PostRequest;
import request.PostSubmission;
import data.Post;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client{

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private String action;
	private String author;
	private String tweet;
	private Date timestamp;
	private Scanner stringScanner;
	private String action2;
	private String requestType;
	//private String goAgain = "yes";

	//constructor
	public Client(){
	}

    public static void main(String... args) throws Exception {

		Client client = new Client();
		client.startRunning();
    }

	//connect to server
	public void startRunning(){
		try{
			connectToServer();
			streamSetup();
		}catch(EOFException eofException){
			System.out.println("\n Client terminated the connection");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			close();
		}
	}

	//connect to server
	private void connectToServer() throws IOException{
		connection = new Socket("localhost", 4040);
	}

	//setup of IO streams
	private void streamSetup() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());

		doSomething();		// setup done. Continue to where stuff happens.
	}

	//Close connection
	private void close(){
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	private void doSomething() throws IOException {
		//do {
			System.out.println("1: Write a tweet");
			System.out.println("2: Read tweets");
			System.out.println("x: Exit");
			System.out.println("What do you want to do?");
			stringScanner = new Scanner(System.in);
			action = stringScanner.next(); //reads a char.

			if (action.equals("1")) {        // Ascii Dec value of char '1' is 49.


				System.out.println("Please enter your username:");
				author = stringScanner.next();

				System.out.println("What do you want to talk about ? (120 characters)");
				stringScanner.nextLine();
				tweet = stringScanner.nextLine();

				timestamp = Calendar.getInstance().getTime();

				System.out.println("send post: ");
				System.out.println("timestamp \"" + timestamp + "\"");
				System.out.println("author: \"" + author + "\"");
				System.out.println("tweet: \"" + tweet + "\"");

				Post post = new Post(author, tweet, timestamp);                //wrapping in a Post class before sending to the network

				PostSubmission user = new PostSubmission(post);
				output.writeObject(user);

			} else if (action.equals("2")) {
				// ADD READ ALL, READ SPECIFIC FOR AUTHOR

				System.out.println("1: Read last tweet");
				System.out.println("2: Read all tweets");
				System.out.println("3: Read your own tweets");
				System.out.println("What do you want to do?");

				stringScanner = new Scanner(System.in);
				requestType = stringScanner.next(); //reads a char.

				timestamp = Calendar.getInstance().getTime();
				Post emptyPost = new Post("Empty", "Empty", timestamp);
				PostRequest userRequest = new PostRequest(emptyPost, requestType);

				if (requestType.equals("1")) {
					output.writeObject(userRequest);
					Post outputPost = new Post("Empty", "Empty", timestamp);
					try {
						outputPost = (Post) input.readObject();
						System.out.println("Latest post:");
						System.out.println(outputPost.getAuthor());
						System.out.println(outputPost.getTweet());
						System.out.println(outputPost.getTimestamp());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (requestType.equals("2")) {
					output.writeObject(userRequest);
					List<Post> outputList = new LinkedList<Post>();
					try {
						outputList = (List<Post>) input.readObject();
						System.out.println("All posts:");
						for (Iterator<Post> it = outputList.iterator(); it.hasNext(); ) {
							emptyPost = it.next();
							System.out.println(emptyPost.getAuthor());
							System.out.println(emptyPost.getTweet());
							System.out.println(emptyPost.getTimestamp());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (requestType.equals("3")) {
					String authorName;
					System.out.println("Write your username:");
					authorName = stringScanner.next();
					Post authorPost = new Post(authorName, "Empty", timestamp);
					PostRequest userAuthorRequest = new PostRequest(authorPost, requestType);
					output.writeObject(userAuthorRequest);
					List<Post> outputList = new LinkedList<Post>();
					try {
						outputList = (List<Post>) input.readObject();
						System.out.println("All your posts:");
						for (Iterator<Post> it = outputList.iterator(); it.hasNext(); ) {
							emptyPost = it.next();
							System.out.println(emptyPost.getAuthor());
							System.out.println(emptyPost.getTweet());
							System.out.println(emptyPost.getTimestamp());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

			} else {
				//handling if it is a request
				System.out.println("Error. Action had wrong value. ");
			}
/*
			System.out.println("Do you want to make another action? ");
			System.out.println("Write 'yes' to make another action. Write anything else to end. ");
			goAgain = stringScanner.next(); //reads a char.

		}while(goAgain.equals("yes"));
		*/
	}
}
