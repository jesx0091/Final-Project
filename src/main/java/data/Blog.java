// IST JAVA project
// JES RYDALL LARSEN
// MELANIE BRANDL
// IRENE ARROYO
package data;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Blog implements Readable, Writable{
    List<Post> tweets;

    public Blog(){
        tweets = new LinkedList<>();
        try{
            populateFromDisk();
        } catch (IOException e) {
			e.printStackTrace();
		}
        finally {
        }
    }

    public void addPost(Post newPost) {

        tweets.add(newPost);
    }
    // gets the list of our file
    private void populateFromDisk() throws IOException {
      
        	  try {
        	    FileInputStream fis = new  FileInputStream("database.txt");
        	    ObjectInputStream ois = new ObjectInputStream(fis);
        	    Object obj = ois.readObject();
        	    tweets = (LinkedList<Post>) obj;
        	  } 
        	  catch (Exception e) {
        	    System.out.println(e);
        	  } 
        	  
        	
    }


//returns latest tweet
    public Post readOne(){
        return ((LinkedList<Post>) tweets).getLast();

    }


//returns all tweets
    public List<Post> readAll() throws IOException {
    	
        List<Post> allTweets = new LinkedList<Post>();

        for(Iterator<Post> it = tweets.iterator(); it.hasNext();){
            allTweets.add(it.next());
        }

        if(readOne()!=null){
            return allTweets; //return of list right?
        }else{
            return null;
        }
        
    }

//returns only Tweets from the same author
   public List<Post> readOwnPost(String author) throws IOException{
    List<Post> ownTweets = new LinkedList<Post>();

        for(Iterator<Post> it = tweets.iterator(); it.hasNext();){
            Post post = it.next();
            if(post.getAuthor().equals(author))
            ownTweets.add(post);
        }
        return ownTweets;
    }


//save() rewrites the whole file, erasing previous data
   public void save() throws IOException{
		  try {
		    FileOutputStream fos = new FileOutputStream ("database.txt",false);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(tweets);
		    fos.close();
		  } 
		  catch (Exception e) {
		    System.out.println(e);   
		  }
 }

}
