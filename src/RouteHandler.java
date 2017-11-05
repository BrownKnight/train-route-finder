import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by adhoot on 14/03/2017.
 * This class creates files which store route information between two stations, and also returns a String array of
 * a route. Routes are the intermediate stops between two stations on a journey, and so are referenced using the
 * station index id's which are defined in JourneyHandler, but are infinitely changeable and expandable due to
 * all information being saved in human-readable and editable files
 */
public class RouteHandler {
    public static void addRoute(int from, int to, String[] route) {
        String pathName = "./data/routes/" + String.valueOf(from) + "-" + String.valueOf(to) + ".data";
        String routeStr = Arrays.toString(route);
        try {
            FileHandler.writeStringToFile(pathName, routeStr);
        }
        catch (IOException e) {
//            If the file write failed, let the user know an error occurred
            JOptionPane.showMessageDialog(WindowHandler.window,
                    "File write failed, please try again",
                    "File Write Failed",
                    JOptionPane.ERROR_MESSAGE);
        }


    }

    public static String[] getRoute(int from, int to) {
        String pathName = "./data/routes/" + String.valueOf(from) + "-" + String.valueOf(to) + ".data";

        String fileStr = "No route found";
        try {
            fileStr = FileHandler.getStringFromFile(pathName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(WindowHandler.window,
                    "Route file not found, please add one in the admin pane for the stations selected",
                    "Route not found",
                    JOptionPane.ERROR_MESSAGE);
        }
        // Remove the leading and trailing '[' and ']' from the string so that the split function works correctly
        // These characters are created by the Arrays.toString function when the route is added
        fileStr = fileStr.replace("[", "");
        fileStr = fileStr.replace("]", "");

        String[] routeArray = fileStr.split(",");

//        For each element of routeArray, remove leading and trailing whitespace, but not whitespace inside the station names
//        If this is not done, alphabetical ordering will not work as intended due to a space character being
//        considered the first character of the station name

        for (int i = 0; i < routeArray.length; i++) {
            routeArray[i] = routeArray[i].trim();
        }

        return routeArray;
    }

    public static void createRouteFileIfNotFound(int from, int to) {
        String pathName = "./data/routes/" + String.valueOf(from) + "-" + String.valueOf(to) + ".data";

        try {
            String fileStr = FileHandler.getStringFromFile(pathName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(WindowHandler.window,
                    "The route you have selected does not have an associated route file, so a new one has been created",
                    "New Route Created",
                    JOptionPane.INFORMATION_MESSAGE);
            addRoute(from, to, new String[]{"No Route Defined"});
        }
    }
}
