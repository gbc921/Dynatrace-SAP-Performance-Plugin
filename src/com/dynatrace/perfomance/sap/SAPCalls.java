package com.dynatrace.perfomance.sap;

import java.util.Properties;
import java.util.logging.Logger;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SAPCalls {
	
	private static final Logger log = Logger.getLogger(Config.class.getName());
	
	private static JCoDestination destination;
	
	public JCoDestination connect(Config conf) {
		MyDestinationDataProvider provider = 
				new MyDestinationDataProvider(setProperties(conf));
		
		try {
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
			destination = JCoDestinationManager.getDestination(conf.getSysname());
			destination.ping();
			log.info("Connection Success! :)");
			
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			log.severe(e.toString());
			log.severe("Connection Error!");
		}
		
		
		return null;
	}
	
	private Properties setProperties(Config conf) {
		Properties connectProperties = new Properties();
		
		log.warning(conf.getAsname());
		log.warning(conf.getSysnum());
		log.warning(conf.getClinum());
		log.warning(conf.getUsername());
		log.warning(conf.getPassword());
		
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,	conf.getAsname());
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,	conf.getSysnum());
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,	conf.getClinum());
		connectProperties.setProperty(DestinationDataProvider.JCO_USER,		conf.getUsername());
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,	conf.getPassword());
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG,		"en");
		
		return connectProperties;
	}

}
