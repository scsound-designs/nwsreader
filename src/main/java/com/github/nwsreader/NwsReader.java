package com.github.nwsreader;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;

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
public class NwsReader { // TODO: refactor to a class package for terminal reader and an android widget.

    // field
    private String nwsUrl;
    private JsonObject nwsObj;
    private JsonObject propertiesObj;
    private JsonObject relativeLocationObj;
    private JsonObject relLocPropertiesObj;
    private JsonObject forecastOfficeObj;
    private JsonObject forecastObj;
    private JsonObject forecastPropertiesObj;
    private JsonArray forecastPeriodsArr;

    public String city;
    public String State;
    public String forecastOffice;
    public String radarStation;
    
    private Array<ForecastPeriod> forecastPeriods[];

    private double[] coordinates;

    private Coordinates currentCoordinates;
    private Coordinates nwsCoordinates;
    
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
    }

    class Coordinates {
	// field
	double latitude;
	double longitude;
	DecimalFormat coordFormat = new DecimalFormat("00.0000");

	// constructor
	public Coordinates(double[] coordinates) {
	    latitude = coordFormat.format(coordinates[0]);
	    longitude = coordFormat.format(coordinates[1]);
	}
    }

   // TODO: constructor 
    public NwsReader(double[] coordinates) { //constrtor takes coordinates
	DecimalFormat coordFormat = new DecimalFormat("00.0000");
	currentCoordinates = new Coordinates(coordinates); 	
 
	nwsUrl = "https://api.weather.gov/points/" 
	    + currentCoordinates.latitude + ","
	    + currentCorrdinates.longitude;

	String nwsJson = IOUtils.toString(new URL(nwsUrl).openStream());
	nwsObj = new Gson().fromJson(nwsJson, JsonObject.class);


    }     

    // TODO: declare class methods

    private String parse(String key, JsonObject object) {
	String value = parser.parse(object
	    .get(key)
	    .toString())
	    .getAsString();

	return value;
    }

    private String parseUrl(String key, JsonObject object){
	String value = IOUtils.toString(
		new URL(parse(key, object).openStream()));
	
	return value;
    }

    private static void printHeader() {
	DecimalFormat coordFormat = new DecimalFormat("00.0000");
	System.out.print("\n" + city + ", " + state + "    ");
	System.out.println(coordFormat.format(coordinates[0]) + ", "
		+ coordFormat.format(coordinates[1]));
	System.out.println("Forecast Office: " + forecastOffice);
	System.out.println("Radar Station " + radarStation + ": " 
		+ "https://radar.weather.gov/station/" 
		+ radarStation + "/standard\n");
    }
    

    public static void main( String[] args ) throws IOException {
	// get first json from NWS url points
	//
	// Baltimore/Washington DC
	//nwsUrl = "https://api.weather.gov/points/38.8894,-77.0352"; 
	//
	//
	// Seattle/Wasington
	nwsUrl = "https://api.weather.gov/points/47.5896,-122.3331"; 
	//
	// Davie/FL
	//nwsUrl = "https://api.weather.gov/points/26.0813,-80.2802"; 

	String nwsJson = IOUtils.toString(new URL(nwsUrl).openStream());
	nwsObj = new Gson().fromJson(nwsJson, JsonObject.class);

	//JsonParser parser = new JsonParser();

	/*
	// get geometry json, and get coordinates array from geometry json
	JsonObject geometryObj = nwsObj.getAsJsonObject("geometry");
	
	double[] coordinates = {
	    parser.parse(geometryObj
		    .getAsJsonArray("coordinates")
		    .get(1)
		    .toString())
		.getAsDouble(),
	    parser.parse(geometryObj
		    .getAsJsonArray("coordinates")
		    .get(0)
		    .toString())
		.getAsDouble()
	};
	DecimalFormat coordFormat = new DecimalFormat("00.0000");
	*/

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
	city = parse("city", relLocPropertiesObj)
	state = parse("state", relLocPropertiesObj)
	
	// get and parse radar station
	radarStation = parse("radarStation", propertiesObj)

	// get and parse forecast url from first json
	String forecastJson = parseUrl("forecast", propertiesObj);
	forecastObj = new Gson()
	    .fromJson(forecastJson, JsonObject.class);

	// get properties json, and get periods json array
	forecastPropertiesObj = forecastObj
	    .getAsJsonObject("properties");
	forecastPeriodsArr = forecastPropertiesObj
	    .getAsJsonArray("periods");

	// output formatted header 
	printHeader();
	/*
	System.out.print("\n" + city + ", " + state + "    ");
	System.out.println(coordFormat.format(coordinates[0]) + ", " 
		+ coordFormat.format(coordinates[1]));
	System.out.println("Forecast Office: " + forecastOfficeName);
	System.out.println("Radar Station " + radarStation + ": " 
		+ "https://radar.weather.gov/station/" 
		+ radarStation + "/standard\n");
	*/

	// get and parse values for each period
	
	int i = 0;
	for (JsonElement period : forecastPeriodsArr) {
	    forecastPeriods[i] = new ForecastPeriod(period);
 
	    /*
	    // output formatted values
	    System.out.println(name);
	    System.out.println(temperature + temperatureUnit + " " 
		    + windDirection + " " + windSpeed);
	    System.out.println(shortForecast + ".\n" );
	    */

	}
	System.out.println( "NwsReader!" );	    

    }   

}
