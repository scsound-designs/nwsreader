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
public class NwsReader {
    public static void main( String[] args ) throws IOException {
	    // TODO: init git.
	    // TODO: refactor to a class package to be used in both a terminal reader and an android widget.

	    // get first json from NWS url points
	    //String nwsUrl = "https://api.weather.gov/points/38.8894,-77.0352"; // Baltimore/Washington DC
	    String nwsUrl = "https://api.weather.gov/points/47.5896,-122.3331"; // Seattle/Wasington
	    //String nwsUrl = "https://api.weather.gov/points/26.0813,-80.2802"; // Davie/FL

	    String nwsJson = IOUtils.toString(new URL(nwsUrl).openStream());
	    //System.out.println(json); // DEGBUG   
	    JsonObject nwsObj = new Gson().fromJson(nwsJson, JsonObject.class);
	   
	    JsonParser parser = new JsonParser();

	    // get geometry json, and get coordinates array from geometry json
	    JsonObject geometryObj = nwsObj.getAsJsonObject("geometry");
	    double[] coordinates = {
		    parser.parse(geometryObj.getAsJsonArray("coordinates").get(1).toString()).getAsDouble(),
		    parser.parse(geometryObj.getAsJsonArray("coordinates").get(0).toString()).getAsDouble()
	    };
	    DecimalFormat coordFormat = new DecimalFormat("00.0000");
	    
	    // get properties json
	    JsonObject propertiesObj = nwsObj.getAsJsonObject("properties");

	    // get and parse forcast office json
	    String forecastOfficeJson = IOUtils.toString(
			    new URL(parser.parse(propertiesObj
				    .get("forecastOffice")
				    .toString()).getAsString())
			    .openStream());
	    JsonObject forecastOfficeObj = new Gson().fromJson(forecastOfficeJson, JsonObject.class);
	    String forecastOfficeName = parser.parse(forecastOfficeObj
			    .get("name")
			    .toString()).getAsString();

	    // get relative localtion json and its properties json
	    JsonObject relativeLocationObj = propertiesObj.getAsJsonObject("relativeLocation");
	    JsonObject relLocPropertiesObj = relativeLocationObj.getAsJsonObject("properties");
           
	    // get and parse city and state
	    String city = parser.parse(relLocPropertiesObj
			    .get("city")
			    .toString())
		    .getAsString();
	    String state = parser.parse(relLocPropertiesObj
			    .get("state")
			    .toString())
		    .getAsString();

	    // get and parse radar station
	    String radarStation = parser.parse(propertiesObj
			    .get("radarStation")
			    .toString())
		    .getAsString();
	
	    // get and parse forecast url from first json
	    String forecastJson = IOUtils.toString(
			    new URL(parser.parse(propertiesObj
				    .get("forecast")
				    .toString()).getAsString())
			    .openStream());
	    JsonObject forecastObj = new Gson()
		    .fromJson(forecastJson, JsonObject.class);
	    
	    // get properties json, and get periods json array
	    JsonObject forecastPropertiesObj = forecastObj.getAsJsonObject("properties");
	    JsonArray forecastPeriods = forecastPropertiesObj.getAsJsonArray("periods");

	    // output formatted header 
	    System.out.print("\n" + city + ", " + state + "    ");
	    System.out.println(coordFormat.format(coordinates[0]) + ", " 
			    + coordFormat.format(coordinates[1]));
	    System.out.println("Forecast Office: " + forecastOfficeName);
	    System.out.println("Radar Station " + radarStation + ": " 
			    + "https://radar.weather.gov/station/" + radarStation + "/standard\n");

	    // get and parse values for each period
	    for (JsonElement period : forecastPeriods) {
		    JsonObject periodObj = period.getAsJsonObject();

		    String name = parser.parse(periodObj
				    .get("name")
				    .toString())
			    .getAsString();
		    String temperature = parser.parse(periodObj
				    .get("temperature")
				    .toString())
			    .getAsString();
		    String temperatureUnit = parser.parse(periodObj
				    .get("temperatureUnit")
				    .toString())
			    .getAsString();
		    String windSpeed = parser.parse(periodObj
				    .get("windSpeed")
				    .toString())
			    .getAsString();
		    String windDirection = parser.parse(periodObj
				    .get("windDirection")
				    .toString())
			    .getAsString();
	    	    String shortForecast = parser.parse(periodObj
				    .get("shortForecast")
				    .toString())
			    .getAsString();

		    // output formatted values
		     System.out.println(name);
		     System.out.println(temperature + temperatureUnit + " " 
				     + windDirection + " " + windSpeed);
		     System.out.println(shortForecast + ".\n" );

	    }
	    System.out.println( "NwsReader!" );	    

    }   

}
