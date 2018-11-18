package data;

import java.util.Date;

public class Post extends AbstractPost {
    private String author;
    private String tweet;
    private Date timestamp;
    public Post( String author,String tweet,Date timestamp ){
        this.author=author;
        this.tweet=tweet;
        this.timestamp=timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getTweet() {
        return tweet;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}

