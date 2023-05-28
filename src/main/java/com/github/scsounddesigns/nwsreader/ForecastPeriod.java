package com.github.scsounddesigns.nwsreader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ForecastPeriod { 
	//field
	public String name;
	public String temperature;
	public String temperatureUnit;
	public String windDirection;
	public String windSpeed;
	public String shortForecast;

	public int id;
	public JsonObject periodObj;

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


    private String parse(String key, JsonObject object) {

	JsonParser parser = new JsonParser();
	String value = parser.parse(object
		.get(key)
		.toString())
	    .getAsString();

	return value;
    }

	public void print() {
		System.out.println("\n" + name);
		System.out.print(temperature + temperatureUnit + " ");
		System.out.println(windDirection + " " + windSpeed);
		System.out.println(shortForecast + ".\n");

	}
}
