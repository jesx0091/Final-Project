// IST JAVA project
// JES RYDALL LARSEN
// MELANIE BRANDL
// IRENE ARROYO

package request;

import data.Post;

import java.io.Serializable;
import java.util.*;

public class PostRequest implements Serializable {
    private Post post;
    private String requestType;

    public PostRequest(Post post, String requestType){
        this.post = post;
        this.requestType = requestType;
    }

    public String getRequestType(){
        return requestType;
    }

    public String getAuthor() {
        return post.getAuthor();
    }
}
