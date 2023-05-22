package com.github.scsounddesigns.nwsreader;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
    public void shouldAnswerWithTrue() throws IOException {

	// check for radar station KLWX
	boolean isStation;
	// Baltimore, Washington D.C.
	double[] testCoord = {38.8894,-77.0352};
	NwsReader testReader = new NwsReader(testCoord);

	if (testReader.radarStation.equals("KLWX")) {
	    isStation = true;
	} else {
	    isStation = false;
	}

        assertTrue( isStation );
    }
}
