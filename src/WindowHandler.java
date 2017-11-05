import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adhoot on 05/03/2017.
 *
 * This class handles all window operations the program has, beginning with opening the main window
 * It has functions to build each pane of the application, and also controls when the RouteWindowBuilers
 * class is used to build those extra windows
 *
 * All button actions performed with ActionListeners are placed in this class as anonymous classes, rather than being
 * passed into a separate EventListener class (this would've been a better way to do it)
 *
 */
public class WindowHandler {

//    Allow the window which has been created to be referenced in other parts of the program
    public static JFrame window;

    /**
     * Method returns a completely constructed window with all control elements, to the design the main window will have.
     * @return JFrame
     */
    public static JFrame buildMainWindow() {
        window = new JFrame("Route Finder");

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Route", buildRoutePane());
        tabbedPane.addTab("Admin", buildAdminPane());

        window.add(tabbedPane);
        window.setSize(400,400);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        return window;
    }

    /**
     * Builds the JPanel to go inside the first tabbed pane called route of the main window, and returns it to the
     * buildMainWIndow function it is called from
     * @return JPanel to go inside the first tabbed pane of the main window
     */
    private static JPanel buildRoutePane() {
//        Make a layout JPanel to go inside the Route pane of the main tabbed pane
//      Set layout to GridBagLayout to allow for a grid-like layout that supports elements spanning multiple columns
        JPanel pane = new JPanel(new GridBagLayout());

        GridBagConstraints fullWidth = new GridBagConstraints();
        GridBagConstraints rightSide = new GridBagConstraints();
        GridBagConstraints leftSide = new GridBagConstraints();
        fullWidth.fill = GridBagConstraints.HORIZONTAL;
        fullWidth.gridx = 0;
        fullWidth.gridy = 0;
        fullWidth.anchor = GridBagConstraints.CENTER;

        leftSide.fill = GridBagConstraints.NONE;
        leftSide.gridx = 0;
        leftSide.gridy = 0;
        leftSide.anchor = GridBagConstraints.LINE_END;
        leftSide.insets = new Insets(5,10,5,0);


        rightSide.fill = GridBagConstraints.NONE;
        rightSide.gridx = 1;
        rightSide.gridy = 0;
        rightSide.anchor = GridBagConstraints.LINE_START;
        rightSide.insets = new Insets(5,10,5,10);



//        Row 1 of layout, containing JLabel saying that the textbox next to it is to input date of travel
//        and a JFormattedTextField to allow a travel date to be entered, its value defaulting to the current day
        leftSide.gridy = 0;
        JLabel dateLabel = new JLabel("Date of travel:");
        pane.add(dateLabel, leftSide);
        rightSide.gridy = 0;
        rightSide.fill = GridBagConstraints.HORIZONTAL;
//        Set the format of the date field with a mask, to only allow numbers in the format ##/##/####,
//        to try and get a date in the format of dd/mm/yyyy. This value is further validated when its value is processed
        MaskFormatter dateMask = null;
        try {
            dateMask = new MaskFormatter("##/##/####");
        }
        catch (ParseException e) {
            System.out.print(e.getMessage());
        }
        JFormattedTextField dateField = new JFormattedTextField(dateMask);
//        Set the date in the textbox to be todays date
        Date todaysDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateField.setValue(dateFormat.format(todaysDate));
        pane.add(dateField, rightSide);
        rightSide.fill = GridBagConstraints.NONE;

//        Row 2 of layout containing a label for the from and to station combo boxes below it
        fullWidth.gridy = 1;
        fullWidth.gridwidth = 2;
        JLabel journeyLabel = new JLabel("Journey between", JLabel.CENTER);
        pane.add(journeyLabel, fullWidth);

//        Row 3 of layout
//        The to & from stations are selected using a dropdown combo box
        fullWidth.gridx = 0;
        fullWidth.gridy = 2;
        fullWidth.gridwidth = 1;
        JComboBox<String> from = new JComboBox<String>();
        String[] stations = JourneyHandler.getStationNames();
        for (int i = 0; i<stations.length; i++) {
            from.addItem(stations[i]);
        }
        pane.add(from, fullWidth);

        fullWidth.gridx = 1;
        fullWidth.gridy = 2;
        JComboBox<String> to = new JComboBox<String>();
        for (int i = 0; i<stations.length; i++) {
            to.addItem(stations[i]);
        }
        pane.add(to, fullWidth);


//        Set the Get Price & Time button to be full width, on the 3rd row of the layout
        fullWidth.gridx = 0;
        fullWidth.gridy = 3;
        fullWidth.gridwidth = 2;
        JButton goButton = new JButton("Get Price & Time");
        pane.add(goButton, fullWidth);

//        Row 4 of layout, containing a JLabel to show error messages with the input
//        Use a new constraints so that the errorLabel can be added back easily after it is removed
        GridBagConstraints errorConstraints = new GridBagConstraints();
        errorConstraints.gridy = 4;
        errorConstraints.fill = GridBagConstraints.HORIZONTAL;
        errorConstraints.gridwidth = 2;
        JLabel errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
//        errorLabel is not added to the panel because it should only be shown if an error occurs,
//        otherwise it will take up space and leave it blank on the screen, looking odd

//        Row 5 of layout, containing single price
        leftSide.gridy = 5;
        JLabel singlePriceLabel = new JLabel("Price for a single ticket:");
        pane.add(singlePriceLabel, leftSide);
        rightSide.gridy = 5;
        JLabel singlePrice = new JLabel("");
        pane.add(singlePrice, rightSide);

//        Row 6 of layout, containing return price
        leftSide.gridy = 6;
        JLabel returnPriceLabel = new JLabel("Price for a return ticket:");
        pane.add(returnPriceLabel, leftSide);
        rightSide.gridy = 6;
        JLabel returnPrice = new JLabel("");
        pane.add(returnPrice, rightSide);

//        Row 7 of layout, containing Journey Time
        leftSide.gridy = 7;
        JLabel journeyTimeLabel = new JLabel("Journey Length:");
        pane.add(journeyTimeLabel, leftSide);
        rightSide.gridy = 7;
        JLabel journeyTime = new JLabel("");
        pane.add(journeyTime, rightSide);

//        Row 8 of the layout, with an option on how to sort the stations
        leftSide.gridy = 8;
        JLabel sortLabel = new JLabel("Sort by:");
        pane.add(sortLabel, leftSide);

        rightSide.gridy = 8;
        JComboBox<String> sortSelection = new JComboBox<String>(new String[] {"Manual Order", "Alphabetical Order"});
        pane.add(sortSelection, rightSide);


//        Row 9 of the layout, containing the 'Route' button which will show intermediate stops
        fullWidth.gridy = 9;
        JButton routeButton = new JButton("Show Route");
        routeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (from.getSelectedIndex() == to.getSelectedIndex()) {
//                    If the from and to station are the same, show an error saying so as you cant have a route start
//                    and end at the same place
                    errorLabel.setText("Please select unique stations");
                    errorLabel.setForeground(new Color(231, 76, 60));
                    pane.add(errorLabel, errorConstraints);
//                    Force the pane to redo its layout, to ensure the error actually gets displayed
//                    as sometimes does not display the new object correctly
                    pane.revalidate();
                    pane.repaint();
                } else {
                    pane.remove(errorLabel);
//                    Force the pane to redo its layout, to ensure the error actually gets removed
//                    as sometimes does not display the new object correctly
                    pane.revalidate();
                    pane.repaint();
                    RouteWindowBuilder.buildViewWindow(from.getSelectedIndex(), to.getSelectedIndex(), sortSelection.getSelectedIndex());
                }
            }
        });
        pane.add(routeButton, fullWidth);


//        Row 10 of the layout, containing an exit buttons in case somebody doesn't click the x in the corner of the window
        fullWidth.gridy = 10;
        JButton exitButton = new JButton("Exit Application");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Exit with status of 0, to show execution went correctly
                System.exit(0);
            }
        });
        pane.add(exitButton, fullWidth);


        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean errorOccurred = false;
//                Remove any error messages that occurred previous times the button was pressed
                pane.remove(errorLabel);

//                Perform some validation on the stations selected, and the date entered,
//                end execution of function if validation fails

//                Check if the from and to stattions are the same, if so display an error
                if (from.getSelectedIndex() == to.getSelectedIndex()) {
                    errorLabel.setText("Please choose two unique stations");
                    errorOccurred = true;
                }

//                Check if the date entered is valid
                Date checkedDate = null;
                try {
                    dateFormat.setLenient(false);
                    checkedDate = dateFormat.parse(dateField.getText());
                } catch (ParseException parseError) {
                    errorLabel.setText("Date entered is invalid");
                    errorOccurred = true;
                }

                if (errorOccurred) {
                    singlePrice.setText("");
                    returnPrice.setText("");
                    journeyTime.setText("");
                    errorLabel.setForeground(new Color(231, 76, 60));
                    pane.add(errorLabel, errorConstraints);
//                    Force the pane to redo its layout, to ensure the error actually gets displayed
//                    as sometimes does not display the new object correctly
                    pane.revalidate();
                    pane.repaint();
                    return;
                }


//                Check if the date entered is the last day of the month, if so set isLastDay to true to apply 10% discount
                Calendar dateEntered = Calendar.getInstance();
                dateEntered.setTime(checkedDate);
                boolean isLastDay = false;

                if (dateEntered.get(Calendar.DAY_OF_MONTH) == dateEntered.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    isLastDay = true;
//                    Use the errorLabel to show that the discount has been applied
                    errorLabel.setText("10% discount has been applied");
                    errorLabel.setForeground(new Color(46, 204, 113));
                    pane.add(errorLabel, errorConstraints);
                    pane.revalidate();
                    pane.repaint();
                }

//                If validation succeeds, display price and time info for the stations selected
                int singlePriceNum = JourneyHandler.getSinglePrice(from.getSelectedIndex(), to.getSelectedIndex());
                if (isLastDay) singlePriceNum = Math.round(singlePriceNum*0.9f);
                String singlePriceStr = String.valueOf(singlePriceNum);

                singlePriceStr = singlePriceStr.substring(0, singlePriceStr.length()-2)
                        + "."
                        + singlePriceStr.substring(singlePriceStr.length()-2, singlePriceStr.length()
                );


                int returnPriceNum = JourneyHandler.getReturnPrice(from.getSelectedIndex(), to.getSelectedIndex());
                if (isLastDay) returnPriceNum = Math.round(returnPriceNum*0.9f);
                String returnPriceStr = String.valueOf(returnPriceNum);

                returnPriceStr = returnPriceStr.substring(0, returnPriceStr.length()-2)
                        + "."
                        + returnPriceStr.substring(returnPriceStr.length()-2, returnPriceStr.length()
                );

                int journeyTimeInt = JourneyHandler.getJourneyTime(from.getSelectedIndex(), to.getSelectedIndex());
                String journeyTimeHour = String.valueOf(journeyTimeInt / 60);
                String journeyTimeMin = String.valueOf(journeyTimeInt % 60);

                if (journeyTimeMin.length() < 2) {
                    journeyTimeMin = "0" + journeyTimeMin;
                }

                String journeyTimeStr = journeyTimeHour + ":" + journeyTimeMin + " hours";

                singlePrice.setText("£" + singlePriceStr);
                returnPrice.setText("£" + returnPriceStr);
                journeyTime.setText(journeyTimeStr);

            }
        });

        pane.setSize(400,400);

        return pane;
    }
    private static JPanel buildAdminPane() {
        JPanel pane = new JPanel(new GridBagLayout());

        GridBagConstraints fullWidth = new GridBagConstraints();
        GridBagConstraints rightSide = new GridBagConstraints();
        GridBagConstraints leftSide = new GridBagConstraints();
        fullWidth.fill = GridBagConstraints.HORIZONTAL;
        fullWidth.gridx = 0;
        fullWidth.gridy = 0;
        fullWidth.gridwidth = 2;
        fullWidth.anchor = GridBagConstraints.CENTER;

        leftSide.fill = GridBagConstraints.NONE;
        leftSide.gridx = 0;
        leftSide.gridy = 0;
        leftSide.anchor = GridBagConstraints.LINE_END;
        leftSide.insets = new Insets(5,10,5,0);


        rightSide.fill = GridBagConstraints.NONE;
        rightSide.gridx = 1;
        rightSide.gridy = 0;
        rightSide.anchor = GridBagConstraints.LINE_START;
        rightSide.insets = new Insets(5,10,5,10);


//        Row 1 of layout containing a label for the from and to station combo boxes below it
        fullWidth.gridy = 0;
        JLabel routeLabel = new JLabel("View route between", JLabel.CENTER);
        pane.add(routeLabel, fullWidth);

//        Row 2 of layout
//        The to & from stations are selected using a dropdown combo box
        leftSide.gridy = 1;
        JComboBox<String> from = new JComboBox<String>();
        String[] stations = JourneyHandler.getStationNames();
        for (int i = 0; i<stations.length; i++) {
            from.addItem(stations[i]);
        }
        pane.add(from, leftSide);

        rightSide.gridy = 1;
        JComboBox<String> to = new JComboBox<String>();
        for (int i = 0; i<stations.length; i++) {
            to.addItem(stations[i]);
        }
        pane.add(to, rightSide);


//        Set the Get Route button on the 3rd row of the layout
        fullWidth.gridy = 3;
        JButton routeButton = new JButton("View Route");
        pane.add(routeButton, fullWidth);

//        Row 4 of layout, with a JButton which will open a window to allow you to add/change a route
        fullWidth.gridy = 4;
        JButton changeRoute = new JButton("Add/Change Route");
        pane.add(changeRoute, fullWidth);

//        Row 5 of layout, containing a JLabel to show error messages with the input
//        Use a new constraints so that the errorLabel can be added back easily after it is removed
        GridBagConstraints errorConstraints = new GridBagConstraints();
        errorConstraints.gridy = 5;
        errorConstraints.fill = GridBagConstraints.HORIZONTAL;
        errorConstraints.gridwidth = 2;
        JLabel errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setForeground(new Color(200,0,0));
//        errorLabel is not added to the panel because it should only be shown if an error occurs,
//        otherwise it will take up space and leave it blank on the screen, looking odd


//        Define the actionListener for the View Route button
        routeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Remove the errorLabel from the pane in case it has ben resolved, if it hasn't it will be added again anyway
                pane.remove(errorLabel);


//                Perform some validation to ensure the stations selected are unique, not the same
                if (from.getSelectedIndex() == to.getSelectedIndex()) {
                    errorLabel.setText("Please select unique stations");
                    pane.add(errorLabel, errorConstraints);
//                    Force the pane to redo its layout, to ensure the error actually gets displayed
//                    as sometimes does not display the new object correctly
                    pane.revalidate();
                    pane.repaint();
//                    End execution of the event, to prevent any processing happen when the data isn't valid
                    return;
                }

//                routeOrder of 0 signifies the window should display the route in the order it was originally inputted as
                RouteWindowBuilder.buildViewWindow(from.getSelectedIndex(), to.getSelectedIndex(), RouteWindowBuilder.MANUAL_ORDER);
            }
        });

//        Define the actionListener for the Add/Change route button, which will open a new window allows you to add a new route
        changeRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Perform some validation to ensure the stations selected are unique, not the same
                if (from.getSelectedIndex() == to.getSelectedIndex()) {
                    errorLabel.setText("Please select unique stations");
                    pane.add(errorLabel, errorConstraints);
//                    Force the pane to redo its layout, to ensure the error actually gets displayed
//                    as sometimes does not display the new object correctly
                    pane.revalidate();
                    pane.repaint();
//                    End execution of the event, to prevent any processing happen when the data isn't valid
                    return;
                }

//                If the stations are not the same, create a route file if one does not already exist
                RouteHandler.createRouteFileIfNotFound(from.getSelectedIndex(), to.getSelectedIndex());

//                Fianlly, open the Add/Change route window
                RouteWindowBuilder.buildChangeRouteWindow(from.getSelectedIndex(), to.getSelectedIndex());
            }
        });

        pane.setSize(400,400);

        return pane;
    }
}
