package data;

import java.io.IOException;
import java.util.List;

public interface Readable {
    Post readOne(); //returns latest tweet
    List<Post> readAll() throws IOException; //returns all tweets
    List<Post> readOwnPost(String author) throws IOException; //returns only E from the same author

}
