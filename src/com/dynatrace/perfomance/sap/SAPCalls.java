package com.dynatrace.perfomance.sap;

import java.util.Properties;
import java.util.logging.Logger;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SAPCalls {
	
	private static final Logger log = Logger.getLogger(Config.class.getName());
	

	public void Login(JCoDestination jcoDestination) throws JCoException {
		String function = "BAPI_XMI_LOGON";
		JCoFunction jcoFunction = setFunction(jcoDestination, function);
		
		jcoFunction = setImportValueLogin(jcoFunction);
		
		jcoFunction.execute(jcoDestination);
		
		log.fine(function + " SESSION-ID: " +
					jcoFunction.getExportParameterList().getValue("SESSIONID"));
		log.finer(function + "\n\n" + getReturnStructure(jcoFunction, ""));
	}
	
	public void Logoff(JCoDestination jcoDestination) throws JCoException {
		String function = "BAPI_XMI_LOGOFF";
		JCoFunction jcoFunction = setFunction(jcoDestination, function);
		
		jcoFunction.execute(jcoDestination);
		
		log.finer(function + "\n\n" + getReturnStructure(jcoFunction, ""));
	}

	public JCoDestination connect(Config conf, JCoDestination jcoDestination) throws JCoException {
		MyDestinationDataProvider provider = 
				new MyDestinationDataProvider(setProperties(conf));

		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
		jcoDestination = JCoDestinationManager.getDestination(conf.getSysname());
		jcoDestination.ping();
		
		log.fine("Connection Success! :)");
		
		return jcoDestination;
	}

	
	private Properties setProperties(Config conf) {
		Properties connectProperties = new Properties();
		
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,	conf.getAsname());
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,	conf.getSysnum());
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,	conf.getClinum());
		connectProperties.setProperty(DestinationDataProvider.JCO_USER,		conf.getUsername());
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,	conf.getPassword());
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG,		"en");
		
		return connectProperties;
	}

	private JCoFunction setFunction(JCoDestination jcoDestination,
			String functionName) throws JCoException {
		
		return jcoDestination.getRepository().getFunction(functionName);
	}

	// generic setImport Value
	private JCoFunction setImportValue(JCoFunction jcoFunction,
										String param, String value) {
		
		jcoFunction.getImportParameterList().setValue(param, value);
		
		return jcoFunction;
	}
	
	private JCoFunction setImportValueLogin(JCoFunction jcoFunction) {
		
		jcoFunction.getImportParameterList().setValue("EXTCOMPANY", "Dynatrace");
		jcoFunction.getImportParameterList().setValue("INTERFACE", "XAL");
		jcoFunction.getImportParameterList().setValue("VERSION", "1.0");
		
		return jcoFunction;
	}
	
	private JCoStructure getReturnStructure (JCoFunction jcoFunction,
											String structureName) {
		// default value for structureName is RETURN
		if (structureName.isEmpty())
			structureName = "RETURN";
		
		return jcoFunction.getExportParameterList().getStructure(structureName);
	}
	
}
