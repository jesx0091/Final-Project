package request;
import data.Post;

import java.io.Serializable;
import java.util.*;

public class PostSubmission implements Serializable {
    private Post post;
    public PostSubmission( Post post ){
        this.post=post;
	}

    public Post getPost() {
    	return this.post;
    }
    public String getAuthor() {
        return post.getAuthor();
    }

    public String getTweet() {
        return post.getTweet();
    }

    public Date getTimestamp() {
        return post.getTimestamp();
    }

}
