import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by adhoot on 03/03/2017.
 *
 * This class has two public static functions which handle reading and writing strings to files, using new
 * libraries provided in Java 7 which allows you to read and overwrite files in just one line
 *
 * Both functions throw IOExceptions so these must be handled wherever the function is called
 *
 */
public class FileHandler {

    public static String getStringFromFile(String pathName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathName)));
    }

    public static void writeStringToFile(String pathName, String file) throws IOException {
        Files.write(Paths.get(pathName), file.getBytes());
    }
}
