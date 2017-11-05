import java.util.ArrayList;

/**
 * Created by adhoot on 03/03/2017.
 * Defines the data structure for all information required for 1 single journey between two destinations
 * Destinations are not stored in this class however, it is intended to be used as part of a 2d array (in JourneyHandler class)
 */
public class Journey {

    //All prices are stored in pence to remove any floating price rounding errors
    public int singlePrice = 1;
    public int returnPrice = 1;
    public int journeyTime = 30;
    public ArrayList<String> stops;

    Journey(int price1, int price2, int time) {
        singlePrice = price1;
        returnPrice = price2;
        journeyTime = time;
    }

}
