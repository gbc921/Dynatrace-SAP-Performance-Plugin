package com.dynatrace.perfomance.sap;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import java.util.Properties;

public class MyDestinationDataProvider
implements DestinationDataProvider
{
	private final Properties properties;

	public MyDestinationDataProvider(Properties customProperties)
	{
	    properties = customProperties;
	}
	
	public Properties getDestinationProperties( String destinationName )
	{
	    return properties;
	}
	
	public void setDestinationDataEventListener( DestinationDataEventListener eventListener )
	{
	    // nothing to do
	}
	
	public boolean supportsEvents()
	{
	    return false;
	}
}
