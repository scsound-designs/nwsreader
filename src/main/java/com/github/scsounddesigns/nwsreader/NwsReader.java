package com.github.scsounddesigns.nwsreader;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;


import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * an app to read National Weather Service json data and output to terminal.
 *
 */
public class NwsReader { 

    // field
    private String nwsUrlString;
    private String nwsJsonString;
    private JsonObject nwsObj;
    private JsonObject propertiesObj;
    private JsonObject relativeLocationObj;
    private JsonObject relLocPropertiesObj;
    private JsonObject forecastOfficeObj;
    private JsonObject forecastObj;
    private JsonObject forecastPropertiesObj;
    private JsonArray forecastPeriodsArr;

    public String city;
    public String state;
    public String forecastOffice;
   public String radarStation;

    private Coordinates currentCoordinates;
    private Coordinates nwsCoordinates;

    private ArrayList<ForecastPeriod> ForecastPeriods 
	= new ArrayList<ForecastPeriod>();

    JsonParser parser = new JsonParser();

    class ForecastPeriod { 
	//field
	String name;
	String temperature;
	String temperatureUnit;
	String windDirection;
	String windSpeed;
	String shortForecast;

	int id;
	JsonObject periodObj;

	//constructor
	public ForecastPeriod(JsonElement periodElement){
	    periodObj = periodElement.getAsJsonObject();

	    name = parse("name", periodObj);
	    temperature = parse("temperature", periodObj);
	    temperatureUnit = parse("temperatureUnit", periodObj);
	    windSpeed = parse("windSpeed", periodObj);
	    windDirection = parse("windDirection", periodObj);
	    shortForecast = parse("shortForecast", periodObj);
	    id++;
	}

	public void print() {
	    System.out.println("\n" + name);
	    System.out.print(temperature + temperatureUnit + " ");
	    System.out.println(windDirection + " " + windSpeed);
	    System.out.println(shortForecast + ".\n");

	}
    }

    class Coordinates {
	// field
	double latitude;
	double longitude;
	DecimalFormat coordFormat = new DecimalFormat("00.0000");

	// constructor
	public Coordinates(double[] coordinates ) {
	    latitude = coordinates[0];
	    longitude = coordinates[1];
	}
    }


    // constructor 
    public NwsReader(double[] coordinates) throws IOException { 
	//constrtor takes latitude and longitude 
	currentCoordinates = new Coordinates(coordinates); 	

	// get first json from NWS url points
	nwsUrlString = "https://api.weather.gov/points/" 
	    + currentCoordinates.latitude + ","
	    + currentCoordinates.longitude;

	URL nwsUrl = new URL(nwsUrlString);
	HttpsURLConnection nwsConnection = (HttpsURLConnection)nwsUrl.openConnection();
	nwsConnection.setRequestMethod("GET");
	//nwsConnection.setRequestProperty("User-agent", System.getProperty("http.agent"));
	nwsConnection.connect();
	try {
	    InputStream in = new BufferedInputStream(nwsConnection.getInputStream());

	    nwsJsonString = IOUtils.toString(in);

	} catch (IOException e) {
		System.out.println("HTTP ERROR: " + nwsConnection.getErrorStream());
		System.out.println(nwsConnection.getHeaderFields());

	} finally {
		nwsConnection.disconnect();
	}

	//String nwsJson = IOUtils.toString(new URL(nwsUrl).openStream());
	nwsObj = new Gson().fromJson(nwsJsonString, JsonObject.class);

	// get properties json
	propertiesObj = nwsObj.getAsJsonObject("properties");

	// get and parse forcast office json
	String forecastOfficeJson = parseUrl("forecastOffice", propertiesObj);
	forecastOfficeObj = new Gson()
	    .fromJson(forecastOfficeJson, JsonObject.class);
	forecastOffice = parse("name", forecastOfficeObj);

	// get relative localtion json and its properties json
	relativeLocationObj = propertiesObj
	    .getAsJsonObject("relativeLocation");
	relLocPropertiesObj = relativeLocationObj
	    .getAsJsonObject("properties");

	// get and parse city and state
	city = parse("city", relLocPropertiesObj);
	state = parse("state", relLocPropertiesObj);

	// get and parse radar station
	radarStation = parse("radarStation", propertiesObj);

	// get and parse forecast url from first json
	String forecastJson = parseUrl("forecast", propertiesObj);
	forecastObj = new Gson()
	    .fromJson(forecastJson, JsonObject.class);

	// get properties json, and get periods json array
	forecastPropertiesObj = forecastObj
	    .getAsJsonObject("properties");
	forecastPeriodsArr = forecastPropertiesObj
	    .getAsJsonArray("periods");

	// get and parse values for each period
	for (JsonElement period : forecastPeriodsArr) {
	    ForecastPeriods.add(new ForecastPeriod(period));
	}
    }     

    // methods
    private String parse(String key, JsonObject object) {
	String value = parser.parse(object
		.get(key)
		.toString())
	    .getAsString();

	return value;
    }

    private String parseUrl(String key, JsonObject object) throws IOException, MalformedURLException {
	String value = IOUtils.toString(
		new URL(parse(key, object))
		.openStream());

	return value;
    }

    public void printHeader() {
	DecimalFormat coordFormat = new DecimalFormat("00.0000");
	System.out.print("\n" + city + ", " + state + "    ");
	System.out.println(coordFormat.format(currentCoordinates.latitude) + ", "
		+ coordFormat.format(currentCoordinates.longitude));
	System.out.println("Forecast Office: " + forecastOffice);
	System.out.println("Radar Station " + radarStation + ": " 
		+ "https://radar.weather.gov/station/" 
		+ radarStation + "/standard\n");
    }

    public void printForecastPeriods() {
	for (ForecastPeriod period : ForecastPeriods) {
	    period.print();
	}	
    }


    public static void main( String[] args ) {

    }

}   


