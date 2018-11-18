package server;

import data.Blog;
import data.Post;
import request.PostRequest;
import request.PostSubmission;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;


    public class Receptor extends Thread {
        private Socket connection;
        private ObjectOutputStream output;
        static private ObjectInputStream input;
        static Date timestamp = Calendar.getInstance().getTime();
        private static Post post = new Post("Author not initialized! Serverside.", "Tweet not initialized! Serverside.", timestamp);
        static PostRequest userRequest = new PostRequest(post,"0");
        static PostSubmission user = new PostSubmission(post);
        private static Blog blog = new Blog();
        private Object userObject = new Object();

        public Receptor(Socket connection) {
            this.connection = connection;
        }
        public void run(){
            try {
                streamSetup();
                doSomething();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("Connection ended.");
            }finally{
                close();			// Method to close output, input and connection.
            }
        }

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

