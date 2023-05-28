package com.github.scsounddesigns.nwsreader;

import java.text.DecimalFormat;

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
