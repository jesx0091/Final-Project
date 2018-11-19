// IST JAVA project
// JES RYDALL LARSEN
// MELANIE BRANDL
// IRENE ARROYO

package data;

import java.io.IOException;

public interface Writable {
    void save() throws IOException; //save() rewrites the whole file, erasing previous data
}
