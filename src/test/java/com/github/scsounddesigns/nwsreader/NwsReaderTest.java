package com.github.scsounddesigns.nwsreader;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.io.IOException;

/**
 * Unit test for NwsReader.
 */
public class NwsReaderTest 
{
    /**
     * Rigorous Test ((.))_((.))
     */
    @Test
    public void foundStationKLWX() throws IOException {

	// check for radar station KLWX
	boolean isStation;
	// Baltimore, Washington D.C.
	double[] testCoordinates = {38.8894,-77.0352};
	NwsReader testReader = new NwsReader(testCoordinates);

	if (testReader.radarStation.equals("KLWX")) {
	    isStation = true;
	} else {
	    isStation = false;
	}

        assertTrue( isStation );
    }


    @Test
    public void foundStationKATX() throws IOException {

	// check for radar station KATX
	boolean isStation;
	// Seattle, Washington 47.5896,-122.3331
	double[] testCoordinates = {47.5896,-122.3331};
	NwsReader testReader = new NwsReader(testCoordinates);

	if (testReader.radarStation.equals("KATX")) {
	    isStation = true;
	} else {
	    isStation = false;
	}

        assertTrue( isStation );
    }
}
