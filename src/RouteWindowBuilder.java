import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Created by adhoot on 10/04/2017.
 * When this class is created in the WindowHandler class, it will make a new window which displays the
 * intermediate stops (route) between the 2 selected stops, in the selected sort order
 * Values of routeOrder can be: Manual Order (order in which it is stored) :0 or Alphabetical:1
 */
public class RouteWindowBuilder {

    public static int MANUAL_ORDER = 0;
    public static int ALPHABETICAL_ORDER = 1;

    public static void buildViewWindow(int startStation, int endStation, int routeOrder) {

//        Use the startStation and endStation to get the route between the 2 requested stations
        String[] route = RouteHandler.getRoute(startStation, endStation);

//        If the number of stations is 0, set it to one so that the window has at least 1 row in the grid layout
        int numStations = (route.length == 0) ? 1 : route.length;

//        If the routeOrder parameter is Alphabetical, sort the route array
        if (routeOrder == 1) Arrays.sort(route);


//        Build the window to display the intermediate stops, with the title showing the 2 stops the route is between
        JFrame routeWindow = new JFrame("Intermediate stops between "
                + JourneyHandler.getStationNames()[startStation]
                + " and "
                + JourneyHandler.getStationNames()[endStation]);

        JPanel pane;
        if (routeOrder == 1) {
            routeWindow.setSize(450,40*(route.length));
            pane = new JPanel(new GridLayout(numStations, 1));

            if (route.length != 0) {
                for (int i = 0; i < numStations; i++) {
                    JLabel stopName = new JLabel(route[i], JLabel.CENTER);
                    pane.add(stopName);
                }
            } else {
                JLabel noStops = new JLabel("There is no route defined for the journey between the two stations");
                pane.add(noStops);
            }
        } else {
            routeWindow.setSize(450,60*(route.length+1));
            pane = new JPanel(new GridLayout((numStations*2)+3, 1, 5, 0));

            JLabel startStationLabel = new JLabel(JourneyHandler.getStationNames()[startStation], JLabel.CENTER);
            Font boldFont = new Font(startStationLabel.getFont().getFontName(), Font.BOLD, startStationLabel.getFont().getSize()+2);

            startStationLabel.setFont(boldFont);
            pane.add(startStationLabel);
            // Use a different font for the down arrow as Windows default font connot display the down arrow correctly
            // even though Mac's default font can
            Font unicodeFont = new Font(Font.SERIF, Font.PLAIN, 16);
            JLabel downwardsArrow = new JLabel("\u25bc", JLabel.CENTER);
            downwardsArrow.setFont(unicodeFont);
            pane.add(downwardsArrow);
            for (int i = 0; i < route.length; i++) {
                pane.add(new JLabel(route[i], JLabel.CENTER));
                JLabel downArrow = new JLabel("\u25bc", JLabel.CENTER);
                downArrow.setFont(unicodeFont);
                pane.add(downArrow);
            }
            JLabel endStationLabel = new JLabel(JourneyHandler.getStationNames()[endStation], JLabel.CENTER);
            endStationLabel.setFont(boldFont);
            pane.add(endStationLabel);

        }

        routeWindow.add(pane);
        routeWindow.setVisible(true);
    }

    private static JFrame routeChangeWindow;


    public static void buildChangeRouteWindow(int startStation, int endStation) {

        String[] stationNames = JourneyHandler.getStationNames();
        routeChangeWindow = new JFrame("Add/Change Route between " + stationNames[startStation]
                                    + " and " + stationNames[endStation]);
        routeChangeWindow.setContentPane(buildRouteChangePanel(startStation, endStation));
        routeChangeWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        routeChangeWindow.setVisible(true);
    }

    private static JPanel buildRouteChangePanel(int startStation, int endStation) {
//        Use the startStation and endStation to get the route between the 2 requested stations
        String[] tempRoute = RouteHandler.getRoute(startStation, endStation);

//        Convert the string array to an ArrayList to make it easy to add and remove elements
        ArrayList<String> route = new ArrayList<>();
        Collections.addAll(route, tempRoute);
        JPanel routePane = new JPanel(new GridLayout(route.size()+3, 2, 5, 5));

        JLabel routeTextBoxLabel = new JLabel("Stops", JLabel.CENTER);
        routePane.add(routeTextBoxLabel);

        JLabel removeButtonLabels = new JLabel("Remove Stops", JLabel.CENTER);
        routePane.add(removeButtonLabels);


        ArrayList<JTextField> routeText = new ArrayList<>();
        ArrayList<JButton> removeButtons = new ArrayList<>();

        for (int i = 0; i < route.size(); i++) {
            JTextField textField = new JTextField(route.get(i));
            textField.getDocument().putProperty(Document.StreamDescriptionProperty, String.valueOf(i));
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    try {
                        route.set(
                                Integer.parseInt(e.getDocument().getProperty(Document.StreamDescriptionProperty).toString()),
                                e.getDocument().getText(0, e.getDocument().getLength())
                        );
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            routeText.add(textField);

            JButton removeButton = new JButton("Remove Stop");
//            Set the ActionCommand of the remove button to match the index of the stop it is related to
//            This allows you to know which index of the array needs to be removed when the button is pressed
            removeButton.setActionCommand(String.valueOf(i));
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    route.remove(Integer.parseInt(e.getActionCommand()));
                    RouteHandler.addRoute(startStation, endStation, route.toArray(new String[]{}));
                    routeChangeWindow.remove(routeChangeWindow.getContentPane());
                    routeChangeWindow.setContentPane(buildRouteChangePanel(startStation, endStation));
                    routeChangeWindow.revalidate();
                    routeChangeWindow.repaint();
                }
            });
            removeButtons.add(removeButton);
            routePane.add(routeText.get(i));
            routePane.add(removeButtons.get(i));
        }

        JButton addStop = new JButton("Add Stop");
        addStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Add a new element to the end of the array, then save the modified array
                route.add("New Stop");
                RouteHandler.addRoute(startStation, endStation, route.toArray(new String[]{}));
                routeChangeWindow.remove(routeChangeWindow.getContentPane());
                routeChangeWindow.setContentPane(buildRouteChangePanel(startStation, endStation));
                routeChangeWindow.revalidate();
                routeChangeWindow.repaint();
            }
        });
        routePane.add(addStop);

        JButton removeAllStops = new JButton("Remove All Stops");
        removeAllStops.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RouteHandler.addRoute(startStation, endStation, new String[]{"No Route Defined"});
                routeChangeWindow.remove(routeChangeWindow.getContentPane());
                routeChangeWindow.setContentPane(buildRouteChangePanel(startStation, endStation));
                routeChangeWindow.revalidate();
                routeChangeWindow.repaint();
            }
        });
        routePane.add(removeAllStops);

        JButton saveStops = new JButton("Save All Stops");
        saveStops.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RouteHandler.addRoute(startStation, endStation, route.toArray(new String[]{}));
                routeChangeWindow.setContentPane(buildRouteChangePanel(startStation, endStation));
                routeChangeWindow.revalidate();
                routeChangeWindow.repaint();
            }
        });
        routePane.add(saveStops);

        JButton exitWindow = new JButton("Save & Close");
        exitWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RouteHandler.addRoute(startStation, endStation, route.toArray(new String[]{}));
                routeChangeWindow.dispatchEvent(new WindowEvent(routeChangeWindow, WindowEvent.WINDOW_CLOSING));
            }
        });
        routePane.add(exitWindow);

        routePane.setSize(400,15*route.size());
        routeChangeWindow.setSize(400, 45*(route.size()+3));
        return routePane;
    }
}














