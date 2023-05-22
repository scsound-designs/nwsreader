package com.github.scsounddesigns.nwsreader;

import java.io.IOException;
import com.github.scsounddesigns.nwsreader.NwsReader;

public class Main {

    public static void main( String[] args ) throws IOException {
	// coordinate points 
	//
	// Baltimore/Washington DC
	double[] coordBaltimore = {38.8894,-77.0352}; 
	//
	//
	// Seattle/Wasington
	//double[] coordSeattle = [47.5896,-122.3331]; 
	//
	// Davie/FL
	//double[] coordDavie = [26.0813,-80.2802]; 

	NwsReader testReader = new NwsReader(coordBaltimore);
	testReader.printHeader();
	testReader.printForecastPeriods();

	System.out.println("NwsReader!");
    }
}
