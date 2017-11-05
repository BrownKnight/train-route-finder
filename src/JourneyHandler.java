

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by adhoot on 03/03/2017.
 * Stores all journeys available in the program in a 2d ArrayList
 * Indexes:
 *  0: Leicester
 *  1: Loughborough
 *  2: Nottingham
 *  3: Derby
 *  4: York
 */
public class JourneyHandler {

    //Declare the data structure which the journey information will be stored in
    //essentially a 2d array with the elements being of the type Journey defined in the Journey class
    private static ArrayList<ArrayList<Journey>> journeys;


    /**
     * Intended to be used when  the program is started, build the journey data from the journeys.data
     * and stations.data files, and store it in a static context for the whole program to use.
     *
     * Reads from a file called "journeys.data" in the data directory of the project and extracts the information
     * about ticket prices and journey durations from that file creating a 2d array containing Journey elements
     * and storing that array as an ArrayList in the private variable journeys
     */
    public static void buildJourneyData() {


        //read the file containing all journey information and store the string
        String arrayString = "";
        try {
            arrayString = FileHandler.getStringFromFile("./data/journeys.data");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(WindowHandler.window,
                    "Fatal error, journeys.data file not found in data folder",
                    "Journey Data Not Found",
                    JOptionPane.WARNING_MESSAGE
            );
        }
        //sanitize the string input to remove new lines and empty characters
        arrayString = arrayString.replace("\n","");
        arrayString = arrayString.replace(" ","");
        arrayString = arrayString.replace("\r","");
        arrayString = arrayString.trim();


        // To extract all the information about ticket prices from the file string, the string must be split into
        // arrays in 3 iterations, to create a 3d array where the indexes map to [stationFrom][stationTo][journeyInfoIndex]
        // where journeyInfoIndex maps to 0: singlePrice 1: returnPrice 2: journeyTime
        ArrayList<String> arrayFirstIteration = new ArrayList<String>();
        ArrayList<ArrayList<String>> arraySecondIteration = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<String>>> arrayThirdIteration = new ArrayList<ArrayList<ArrayList<String>>>();


        // In all the following split functions, escape characters must be used
        // because of the special reserved characters in regex
        // Create the first iteration of the string split into an array
        Collections.addAll(arrayFirstIteration, arrayString.replaceFirst("\\[", "").split("\\["));


        for (int i = 0; i<arrayFirstIteration.size(); i++) {
            arraySecondIteration.add(new ArrayList<String>());
            Collections.addAll(arraySecondIteration.get(i), arrayFirstIteration.get(i).replaceFirst("\\{","").split("\\{"));
        }
        for (int i = 0; i<arrayFirstIteration.size(); i++) {
            arrayThirdIteration.add(new ArrayList<ArrayList<String>>());
            for (int j = 0; j<arraySecondIteration.get(i).size(); j++) {
                arrayThirdIteration.get(i).add(new ArrayList<String>());
                Collections.addAll(arrayThirdIteration.get(i).get(j), arraySecondIteration.get(i).get(j).split(","));
            }
        }


        // The current array is of type String, so this needs to be parsed into ints
        ArrayList<ArrayList<ArrayList<Integer>>> array = new ArrayList<ArrayList<ArrayList<Integer>>>();

        //Convert the array of strings to an array of ints so they can be passed into he Journey constructor
        for (int i = 0; i<arrayThirdIteration.size(); i++) {
            array.add(new ArrayList<ArrayList<Integer>>());
            for (int j = 0; j <arrayThirdIteration.get(i).size(); j++) {
                array.get(i).add(new ArrayList<Integer>());
                for (int k = 0; k<arrayThirdIteration.get(i).get(j).size(); k++) {
                    array.get(i).get(j).add(Integer.parseInt(arrayThirdIteration.get(i).get(j).get(k)));
                }
            }
        }




        //Access the ticket information using array[startStation][endStation][indexOfJourneyInfo]
        //JourneyInfo indexes: 0:singlePrice 1:returnPrice 2:journeyTime

        //Initialise the structure of the journey variable. Use ArrayList to avoid having to know the size of the array
        journeys = new ArrayList<ArrayList<Journey>>();


        //Iterate over every element in the array which was created from the journeys.data file
        for (int i = 0; i< array.size(); i++) {
            for (int j = 0; j<array.get(i).size(); j++) {
                //Initialise the first element with another ArrayList, as all elements will be null do to the nature of
                //the original initialisation when creating 2d ArrayLists
                journeys.add(new ArrayList<Journey>());
                //Initialise the Journey element
                journeys.get(i).add(new Journey(array.get(i).get(j).get(0),array.get(i).get(j).get(1),array.get(i).get(j).get(2)));
            }
        }

    }

    // The next three functions assume the class that calls these knows which station maps to each index
    // This allows the user to update the station names in the GUI, or adjust the journeys.data and stations.data files
    // to add new stations and the class will still work as intended
    public static int getSinglePrice(int from, int to) {
        return journeys.get(from).get(to).singlePrice;
    }
    public static int getReturnPrice(int from, int to) {
        return journeys.get(from).get(to).returnPrice;
    }
    public static int getJourneyTime(int from, int to) {
        return journeys.get(from).get(to).journeyTime;
    }
    public static Journey getJourney(int from, int to) {
        return journeys.get(from).get(to);
    }

    // Retrieve the names of the stations which are stored in the stations.data file, in a similar fashion to the journeys
    public static String[] getStationNames() {
        String pathName = "./data/stations.data";
        String fileStr = "No Stations Found";
        try {
            fileStr = FileHandler.getStringFromFile(pathName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(WindowHandler.window,
                    "Fatal error, stations.data file not found in data folder",
                    "Station Data Not Found",
                    JOptionPane.WARNING_MESSAGE
            );
        }
        String[] stations = fileStr.split(",");
        return stations;
    }
}







