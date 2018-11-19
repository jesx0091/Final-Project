// IST JAVA project
// JES RYDALL LARSEN
// MELANIE BRANDL
// IRENE ARROYO

package data;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractPost implements Serializable {
    protected String author;
    protected String tweet;
    protected Date timestamp;

}
