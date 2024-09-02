import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.*;

/* Original "outcomes" statement
The user will be able to enter a location (city, ZIP code, etc.)
and data will be pulled from the internet using an API.
This data will then be parsed and sent back to the user in a graph format.
There will also be icons that represent the current state of weather
(partly cloudy, cloudy, rain, sun, etc.). It should also be able to display weather events
correctly using message boxes, such as thunderstorm warnings and wind advisories.

Note:
The API I used (openweathermap) does not give access to Weather Alerts without a paid subscription, so it is not
implemented. */

public class WeatherApp extends JFrame implements ActionListener, KeyListener {
    public JLabel imageLabel = new JLabel();
    public static String currentImgPath = "";
    public JLabel locationName = new JLabel();
//    public JTextArea locationName = new JTextArea();
    public JLabel temperature = new JLabel();
    public JTextField locationSearch = new JTextField(10);
    public JButton searchButton = new JButton(">");
    public Gson gson = new Gson();
    public HttpRequest request;
    public HttpResponse<String> response;
    public WeatherData weather;
    public GeocodeData[] geo;
    public ForecastData forecast;
    public int selectedGeoData = 0;
    public static JPanel top = new JPanel();
    public static JPanel day1 = new JPanel(new FlowLayout());
    public static JPanel day2 = new JPanel(new FlowLayout());
    public static JPanel day3 = new JPanel(new FlowLayout());
    public static JPanel day4 = new JPanel(new FlowLayout());
    public static JPanel day5 = new JPanel(new FlowLayout());
    public static JPanel day6 = new JPanel(new FlowLayout());
    public static JPanel[] forecastPanels = new JPanel[]{
            day1,day2,day3,day4,day5,day6
    };
    public static JPanel forecastInfo = new JPanel();
    public static JButton options = new JButton("Options");
    public static boolean td;
    public static JPanel searchPanel = new JPanel();
    private final String APIkey = null; // Replace with OpenWeatherMap API key as string "<key>"
    public final Color day = new Color (0, 200, 200);
    public final Color night = new Color(0, 153, 153);

    // Options
    public static JRadioButton tempC = new JRadioButton("Celsius");
    public static JRadioButton tempF = new JRadioButton("Fahrenheit");
    public static JRadioButton tempK = new JRadioButton("Kelvin");
    public static ButtonGroup tempGroup = new ButtonGroup();
    public static int tempFormat = 0;

    public static HashMap<String, ForecastData.List> sortByDate(HashMap<String, ForecastData.List> map) {
        HashMap<String, ForecastData.List> sortedMap = new LinkedHashMap<>();

        // Convert keys to Date objects and sort
        java.util.List<String> keys = new ArrayList<>(map.keySet());
        keys.sort(new Comparator<>() {
            final DateFormat df = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH);

            @Override
            public int compare(String o1, String o2) {
                try {
                    return df.parse(o1).compareTo(df.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        // Reconstruct HashMap based on sorted keys
        for (String key : keys) {
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

    public static class LoadingDialog extends JDialog {
        public LoadingDialog(JFrame parent) { // adding relation to parent frame prevents clicking on the main window
            super(parent, "Sending Request");
            setSize(200, 100);
            setLocationRelativeTo(parent); // creates dialog in relation to the main window
            JLabel loadText = new JLabel("Loading...");
            loadText.setHorizontalAlignment(SwingConstants.CENTER); // center the text
            add(loadText);
            setVisible(true);
        }
    }

    WeatherApp(){
        //Initialize main window
        setTitle("JWeather");
        setSize(600,400);
        setResizable(false);
        setLayout(new GridLayout(3,1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.setLayout(new GridLayout(1,2));
        searchPanel.setLayout(new FlowLayout());
        forecastInfo.setLayout(new GridLayout(1, 5));
        locationName.setPreferredSize(new Dimension(66, 133));

        //initialize with default query: Cupertino, California
        LoadingDialog load = new LoadingDialog(this);
        System.out.println("Sending Weather Data Request...");
        request = WeatherFunctions.createRequest("https://api.openweathermap.org/geo/1.0/direct?q=Cupertino&limit=5&appid=" + APIkey);
        response = null;
        response = WeatherFunctions.sendRequest(request);
        System.out.println("Received!");
        geo = gson.fromJson(response.body(), GeocodeData[].class);
        request = WeatherFunctions.createRequest("https://api.openweathermap.org/data/2.5/weather?lat="+geo[0].getLat()+"&lon="+
                geo[0].getLon()+"&appid=" + APIkey);
        response = null;
        response = WeatherFunctions.sendRequest(request);
        weather = gson.fromJson(response.body(), WeatherData.class);

        //set name of queried location
        locationName.setText(weather.getName() + ", " + geo[selectedGeoData].getState() + ", " + geo[selectedGeoData]
                .getCountry());

        //parse with correct temperature option
        if (tempFormat == 0) {
            temperature.setText(String.format("%.2f", (weather.getMain().getTemp() - 273.15)) + "*C");
        } else if (tempFormat == 1) {
            temperature.setText(String.format("%.2f", (((weather.getMain().getTemp() - 273.15) * 9 / 5) + 32)) + "*F");
        } else if (tempFormat == 2) {
            temperature.setText(String.format("%.2f", (weather.getMain().getTemp())) + "*K");
        }

        //load icon that matches weather data
        if (!currentImgPath.equals("/img/"+weather.getWeather().getFirst()
                .getIcon()+"@4x.png")) {
            imageLabel.setIcon(new ImageIcon(this.getClass().getResource("/img/" + weather.getWeather().getFirst()
                    .getIcon() + "@4x.png")));
            currentImgPath = "/img/" + weather.getWeather().getFirst()
                    .getIcon() + "@4x.png";
            imageLabel.setSize(200,200);
            imageLabel.setVisible(true);
        }

        //change background based on daytime or nighttime
        if (weather.getWeather().getFirst().getIcon().charAt(2) == 'd') {
            top.setBackground(day);
            forecastInfo.setBackground(day);
            searchPanel.setBackground(day);
            locationName.setForeground(new Color(0,0,0));
            temperature.setForeground(new Color(0,0,0));
            td = true;
        } else {
            top.setBackground(night);
            forecastInfo.setBackground(night);
            searchPanel.setBackground(night);
            locationName.setForeground(new Color(255,255,255));
            temperature.setForeground(new Color(255,255,255));
            td = false;
        }

        //build+send forecast request
        System.out.println("Sending Forecast Data Request...");
        request = WeatherFunctions.createRequest("https://api.openweathermap.org/data/2.5/forecast?lat="+geo[0].getLat()+"&lon="+
                geo[0].getLon()+"&appid=" + APIkey);
        response = null;
        response = WeatherFunctions.sendRequest(request);
        System.out.println("Received!");
        forecast = gson.fromJson(response.body(), ForecastData.class);
        ArrayList<ForecastData.List> forecastList = new ArrayList<>(forecast.getList());
        ArrayList<Date> forecastDate = new ArrayList<>();

        //create hashmaps for high/low temps (date -> temp)
        HashMap<String, ForecastData.List> high = new HashMap<>();
        HashMap<String, ForecastData.List> low = new HashMap<>();
        HashMap<String, ArrayList<ForecastData.List>> dtForecast = new HashMap<>();
        String[] icons = new String[6];

        //Find high and low temps, store in respective hashmaps
        for (int i = 0; i < forecastList.size(); i++) {
            forecastDate.add(new Date(forecastList.get(i).getDt() * 1000));

            String key = forecastDate.get(i).toString().substring(0,10);

            if (!high.containsKey(key)) {
                high.put(key, forecastList.get(i));
            } else if (forecastList.get(i).getMain().getTemp() > high.get(key).getMain().getTemp()){
                high.replace(key, forecastList.get(i));
            }

            if (!low.containsKey(key)) {
                low.put(key, forecastList.get(i));
            } else if (forecastList.get(i).getMain().getTemp() < low.get(key).getMain().getTemp()){
                low.replace(key, forecastList.get(i));
            }

            if (!dtForecast.containsKey(key)){
                dtForecast.put(key, new ArrayList<>());
            }
            dtForecast.get(key).add(forecastList.get(i));
        }

        //Pick a time in the middle of the day to source the data from, helps with preventing erroneous information being
        //sent back to the user
        int loopCount = 0;
        for (ArrayList<ForecastData.List> list : dtForecast.values()) {
            icons[loopCount] = list.get((int) (list.size()/2)).getWeather().getFirst().getIcon();
            loopCount++;
        }

        System.gc(); // cleanup memory
        //sort each list by date so it appears in the correct order on the GUI
        high = sortByDate(high);
        low = sortByDate(low);
        temperature.setFont(new Font("Arial", Font.BOLD, 20));

        add(top);
        top.add(imageLabel);
        top.add(locationName);
        top.add(temperature);

        add(forecastInfo);

        //Key sets contain all dates the forecast data has
        Object[] hKeySet = high.keySet().toArray();
//        Object[] lKeySet = low.keySet().toArray();

        System.out.println("Displaying results...");
        for(int i = 0; i < 6; i++){
            //each iteration i = each ith of 6 panels for the forecast information
            JPanel elem = forecastPanels[i];
            elem.removeAll();

            //set icon
            JLabel newIcon = new JLabel();
            newIcon.setSize(50, 50);
            newIcon.setIcon(new ImageIcon(this.getClass().getResource("/img/"+icons[i]+".png")));
            newIcon.setVisible(true);
            elem.add(newIcon);
            JLabel t1 = new JLabel(new Date(high.get(hKeySet[i]).dt * 1000).toString().substring(0,10));

            //set high/low temps
            String hlabelTxt = null;
            String llabelTxt = null;
            if (tempFormat == 0) {
                hlabelTxt = String.format("%.2f", (high.get(high.keySet().toArray()[i]).getMain().getTemp() - 273.15)) + "*C";
                llabelTxt = String.format("%.2f", (low.get(high.keySet().toArray()[i]).getMain().getTemp() - 273.15)) + "*C";
            } else if (tempFormat == 1) {
                hlabelTxt = String.format("%.2f", (((high.get(high.keySet().toArray()[i]).getMain().getTemp() - 273.15) * 9 / 5) + 32)) + "*F";
                llabelTxt = String.format("%.2f", (((low.get(low.keySet().toArray()[i]).getMain().getTemp() - 273.15) * 9 / 5) + 32)) + "*F";
            } else if (tempFormat == 2) {
                hlabelTxt = String.format("%.2f", (high.get(high.keySet().toArray()[i]).getMain().getTemp())) + "*K";
                llabelTxt = String.format("%.2f", (low.get(high.keySet().toArray()[i]).getMain().getTemp())) + "*K";
            }

            JLabel t2 = new JLabel("H:"+ hlabelTxt);
            JLabel t3 = new JLabel("L:"+ llabelTxt);
            //add to panel, change panel color based on daytime/nighttime
            elem.add(t1); elem.add(t2); elem.add(t3);
            if (td){
                elem.setBackground(day);
                t1.setForeground(new Color(0,0,0));
                t2.setForeground(new Color(0,0,0));
                t3.setForeground(new Color(0,0,0));
            } else {
                elem.setBackground(night);
                t1.setForeground(new Color(255,255,255));
                t2.setForeground(new Color(255,255,255));
                t3.setForeground(new Color(255,255,255));
            }
            forecastInfo.add(elem); //add panel to main window
        }
        //add window elements
        add(searchPanel);
        searchPanel.add(locationSearch);
        searchPanel.add(searchButton);
        searchPanel.add(options);
        locationSearch.setToolTipText("Please enter city name, or ZIP code");
        searchButton.addActionListener(this);
        locationSearch.addKeyListener(this);
        //configure options panel (created later during ActionEvent)
        options.addActionListener(this);
        tempGroup.add(tempC);
        tempGroup.add(tempF);
        tempGroup.add(tempK);
        //
        load.dispose(); // destroy loading msg
        setVisible(true);
    }

    public static void main(String[] args) {
        WeatherApp window = new WeatherApp();
        window.setVisible(true);
    }

    public boolean isNumeric(String input){
        if (input == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
        /* https://www.baeldung.com/java-check-string-number */
    }

    public void updateWeather(){
        LoadingDialog load = new LoadingDialog(this);
        /* Send Geolocation Request */
        System.out.println("Sending Geolocation Request...");
        boolean zip = isNumeric(locationSearch.getText());
        if (zip){
            request = WeatherFunctions.createRequest("https://api.openweathermap.org/geo/1.0/zip?zip=" +locationSearch.getText()
                    + "&limit=5&appid=" + APIkey);
        } else {
            request = WeatherFunctions.createRequest("https://api.openweathermap.org/geo/1.0/direct?q=" +locationSearch.getText()
                    .replace(" ", "%20")
                    + "&limit=5&appid=" + APIkey);
        }

        //collect response from API
        response = null;
        response = WeatherFunctions.sendRequest(request);
        System.out.println("Received!");

        //if using the zip code, there is an extra layer of geocoding required
        if (!zip) try {
            geo = gson.fromJson(response.body(), GeocodeData[].class);
        } catch (com.google.gson.JsonSyntaxException e) {
            JOptionPane.showMessageDialog(this, "No results found.", "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            load.dispose();
            return; // break execution flow
        }
        else {
            // need to do reverse geolocation shenanigans for ZIP codes
            RGeocodeData rgeo = gson.fromJson(response.body(), RGeocodeData.class);

            request = WeatherFunctions.createRequest("https://api.openweathermap.org/geo/1.0/reverse?lat="
                    + rgeo.getLat()
                    + "&lon="+rgeo.getLon()
                    + "&limit=5&appid=" + APIkey);

            response = null;
            response = WeatherFunctions.sendRequest(request);

            geo = gson.fromJson(response.body(), GeocodeData[].class);
        }

        ArrayList<String> locations = new ArrayList<>();

        //collect returned results as ArrayList
        for (GeocodeData geocodeData : geo) {
            locations.add(geocodeData.getName() + ", " + geocodeData.getState() + ", " + geocodeData.getCountry());
        }

        if (geo.length == 0) {
            JOptionPane.showMessageDialog(this, "No results found.", "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            JComboBox<String> results = new JComboBox<>(locations.toArray(new String[0]));
            results.setSelectedIndex(0); // default to first search result if the user does not pick result
            JOptionPane.showMessageDialog(this, results, "Search Results:",
                    JOptionPane.QUESTION_MESSAGE);
            selectedGeoData = results.getSelectedIndex(); // grab index of desired location
            /* Finish Geolocation Request */

            /* Begin Weather Request */
            //Sends request for current time weather data using specified location
            System.out.println("Sending Weather Data request...");
            request = WeatherFunctions.createRequest("https://api.openweathermap.org/data/2.5/weather?lat=" +
                    geo[selectedGeoData].getLat() + "&lon=" +
                    geo[selectedGeoData].getLon() + "&appid=" + APIkey);

            response = null;
            response = WeatherFunctions.sendRequest(request);
            System.out.println("Received!");
            weather = gson.fromJson(response.body(), WeatherData.class);
            /* Finish Weather Data Request */

            //update background based on daytime / nighttime
            if (weather.getWeather().getFirst().getIcon().charAt(2) == 'd') {
                td = true;
                top.setBackground(day);
                forecastInfo.setBackground(day);
                searchPanel.setBackground(day);
                locationName.setForeground(new Color(0,0,0));
                temperature.setForeground(new Color(0,0,0));
            } else {
                td = false;
                top.setBackground(night);
                forecastInfo.setBackground(night);
                searchPanel.setBackground(night);
                locationName.setForeground(new Color(255,255,255));
                temperature.setForeground(new Color(255,255,255));
            }

            //set icon to current icon
            if (!currentImgPath.equals("/img/"+weather.getWeather().getFirst()
                    .getIcon()+"@4x.png")) {
                imageLabel.setIcon(new ImageIcon(this.getClass().getResource("/img/" + weather.getWeather().getFirst()
                        .getIcon() + "@4x.png")));
                currentImgPath = "/img/" + weather.getWeather().getFirst()
                        .getIcon() + "@4x.png";
                imageLabel.setSize(200,200);
                imageLabel.setVisible(true);
            }

            //grab location name from grabbed weather data
            //Note: if the API does not have data for the specific location, name may be replaced with the closest city
            //Seems to be an issue with only Non-US locations
            String locationName_txt = weather.getName() + ", " + geo[selectedGeoData].getState() + ", " + geo[selectedGeoData].getCountry();
            locationName_txt = locationName_txt.replace("null,", "");
            if (locationName_txt.length() < 34){
                locationName.setText(locationName_txt);
            } else {
                locationName_txt = weather.getName() + ", " + geo[selectedGeoData].getCountry();
                locationName.setText(locationName_txt);
            }


            updateTempText();

            /* Begin Forecast Request */
            System.out.println("Sending Forecast Data Request...");
            request = WeatherFunctions.createRequest("https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=" + geo[selectedGeoData].getLat()
                    + "&lon=" + geo[selectedGeoData].getLon()
                    + "&appid=" + APIkey);

            response = null;
            response = WeatherFunctions.sendRequest(request);
            System.out.println("Received!");

            forecast = gson.fromJson(response.body(), ForecastData.class);
            ArrayList<ForecastData.List> forecastList = new ArrayList<>(forecast.getList());
            ArrayList<Date> forecastDate = new ArrayList<>();

            HashMap<String, ForecastData.List> high = new HashMap<>();
            HashMap<String, ForecastData.List> low = new HashMap<>();
            HashMap<String, ArrayList<ForecastData.List>> dtForecast = new HashMap<>();

            for (int i = 0; i < forecastList.size(); i++) {
                forecastDate.add(new Date((long) forecastList.get(i).getDt() * 1000));

                String key = forecastDate.get(i).toString().substring(0,10);

                if (!high.containsKey(key)) {
                    high.put(key, forecastList.get(i));
                } else if (forecastList.get(i).getMain().getTemp() > high.get(key).getMain().getTemp()){
                    high.replace(key, forecastList.get(i));
                }

                if (!low.containsKey(key)) {
                    low.put(key, forecastList.get(i));
                } else if (forecastList.get(i).getMain().getTemp() < low.get(key).getMain().getTemp()){
                    low.replace(key, forecastList.get(i));
                }

                if (!dtForecast.containsKey(key)){
                    dtForecast.put(key, new ArrayList<>());
                }
                dtForecast.get(key).add(forecastList.get(i));
            }

            high = sortByDate(high);
            low = sortByDate(low);

            //Pick a time in the middle of the day to source the data from, helps with preventing erroneous information being
            //sent back to the user
            int loopCount = 0;
            String[] icons = new String[6];
            for (ArrayList<ForecastData.List> list : dtForecast.values()) {
                icons[loopCount] = list.get((int) list.size()/2).getWeather().getFirst().getIcon();
                loopCount++;
            }

            /* Display Results */
            System.out.println("Displaying results...");
            for(int i = 0; i < 6; i++){
                JPanel elem = forecastPanels[i];
                elem.removeAll();

                JLabel newIcon = new JLabel();
                newIcon.setSize(50, 50);
                newIcon.setIcon(new ImageIcon(this.getClass().getResource("/img/"+icons[i]+".png")));
                newIcon.setVisible(true);
                elem.add(newIcon);

                JLabel t1 = new JLabel(new Date(high.get(high.keySet().toArray()[i]).dt * 1000).toString().substring(0,10));

                String hlabelTxt = null;
                String llabelTxt = null;

                if (tempFormat == 0) {
                    hlabelTxt = String.format("%.2f", (high.get(high.keySet().toArray()[i]).getMain().getTemp() - 273.15)) + "*C";
                    llabelTxt = String.format("%.2f", (low.get(high.keySet().toArray()[i]).getMain().getTemp() - 273.15)) + "*C";
                } else if (tempFormat == 1) {
                    hlabelTxt = String.format("%.2f", (((high.get(high.keySet().toArray()[i]).getMain().getTemp() - 273.15) * 9 / 5) + 32)) + "*F";
                    llabelTxt = String.format("%.2f", (((low.get(low.keySet().toArray()[i]).getMain().getTemp() - 273.15) * 9 / 5) + 32)) + "*F";
                } else if (tempFormat == 2) {
                    hlabelTxt = String.format("%.2f", (high.get(high.keySet().toArray()[i]).getMain().getTemp())) + "*K";
                    llabelTxt = String.format("%.2f", (low.get(high.keySet().toArray()[i]).getMain().getTemp())) + "*K";
                }

                JLabel t2 = new JLabel("H:"+ hlabelTxt);
                JLabel t3 = new JLabel("L:"+ llabelTxt);
                elem.add(t1); elem.add(t2); elem.add(t3);
                if (td){
                    elem.setBackground(new Color(0,200,200));
                    t1.setForeground(new Color(0,0,0));
                    t2.setForeground(new Color(0,0,0));
                    t3.setForeground(new Color(0,0,0));
                } else {
                    elem.setBackground(night);
                    t1.setForeground(new Color(255,255,255));
                    t2.setForeground(new Color(255,255,255));
                    t3.setForeground(new Color(255,255,255));
                }
            }
        }
        this.repaint(); // forcing a repaint fixes the issue where the temp may not be updated in time of drawing
        //resizing 1 pixel and back fixes a weird issue with text not being overwritten
        setSize(600, 401);
        setSize(600, 400);
        load.dispose(); // clear loading box
    }
    @Override
    public void actionPerformed(ActionEvent a) {
        //perform query with search click
        if (a.getSource() == searchButton) {
            updateWeather();
        }
        //draw options panel with correct options
        else if (a.getSource() == options){
            JPanel options = new JPanel();
            options.setSize(200, 200);
            options.setLayout(new FlowLayout());
            options.add(new JLabel("Temperature Settings:"));
            options.add(tempC);
            options.add(tempF);
            options.add(tempK);

            tempC.addActionListener(this);
            tempF.addActionListener(this);
            tempK.addActionListener(this);

            options.setVisible(true);
            tempC.setEnabled(true);

            int currentTempFormat = tempFormat;
            JOptionPane.showMessageDialog(this, options);
            if (currentTempFormat != tempFormat) {
                updateTempText();
                JOptionPane.showMessageDialog(this, "To see the forecast in the new format," +
                        " please re-search the same location.");
                setSize(600, 401);
                setSize(600, 400);
            }
        }
        else if (a.getSource() == tempC) {
            tempFormat = 0;
        }
        else if (a.getSource() == tempF) {
            tempFormat = 1;
        }
        else if (a.getSource() == tempK) {
            tempFormat = 2;
        }
    }

    private void updateTempText() {
        //Update main temperature text
        //String format keeps the decimal point rounded to 2 places
        if (tempFormat == 0) {
            temperature.setText(String.format("%.2f", (weather.getMain().getTemp() - 273.15)) + "*C");
        } else if (tempFormat == 1) {
            temperature.setText(String.format("%.2f", (((weather.getMain().getTemp() - 273.15) * 9 / 5) + 32)) + "*F");
        } else if (tempFormat == 2) {
            temperature.setText(String.format("%.2f", (weather.getMain().getTemp())) + "*K");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        //send query with Enter key as well as button
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == locationSearch) {
            updateWeather();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}