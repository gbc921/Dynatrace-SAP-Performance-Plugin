package com.dynatrace.perfomance.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SAPCalls {

	private static final Logger log = Logger.getLogger(Config.class.getName());

	public void Login(JCoDestination jcoDestination) throws JCoException {
		String function = "BAPI_XMI_LOGON";
		ArrayList<String> exportList = new ArrayList<>();
		Map<String, String> importList = new HashMap<>();
		Map<String, String> returnTable = new HashMap<>();
		
		importList.put("EXTCOMPANY", "Dynatrace");
		importList.put("INTERFACE", "XAL");
		importList.put("VERSION", "1.0");
		
		exportList.add("SESSIONID");
				
		returnTable = callBAPI(jcoDestination, function, importList, exportList);

		log.fine(function + " SESSION-ID: " + returnTable.get("SESSIONID"));
		log.finer(function + "\n\n" + returnTable.get("RETURN"));
	}

	public void Logoff(JCoDestination jcoDestination) throws JCoException {
		String function = "BAPI_XMI_LOGOFF";
		ArrayList<String> exportList = new ArrayList<>();
		Map<String, String> importList = new HashMap<>();
		Map<String, String> returnTable = new HashMap<>();
		
		returnTable = callBAPI(jcoDestination, function, importList, exportList);

		log.finer(function + "\n\n" + returnTable.get("RETURN"));
	}

	public JCoDestination connect(Config conf, JCoDestination jcoDestination) throws JCoException {
		MyDestinationDataProvider provider = new MyDestinationDataProvider(setProperties(conf));

		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
		jcoDestination = JCoDestinationManager.getDestination(conf.getSysname());
		jcoDestination.ping();

		log.fine("Connection Success! :)");

		return jcoDestination;
	}

	private Map<String, String> callBAPI(JCoDestination jcoDestination,
											String functionName,
											Map<String, String> importList,
											ArrayList<String> exportList)
			throws JCoException {

		Map<String, String> returnTable = new HashMap<>();

		JCoFunction jcoFunction = setFunction(jcoDestination, functionName);

		// set import values
		for (Map.Entry<String, String> entry : importList.entrySet()) {
			jcoFunction = setImportValue(jcoFunction, entry.getKey(), entry.getValue());
		}
		
		jcoFunction.execute(jcoDestination);
		
		// always return export parameter name RETURN
		returnTable.put("RETURN", getReturn(jcoFunction, ""));
		
		// iterate through other export parameters types
		for (String exportName : exportList) {
			returnTable.put(exportName, getReturn(jcoFunction, exportName));
		}

		return returnTable;

	}

	private Properties setProperties(Config conf) {
		Properties connectProperties = new Properties();

		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, conf.getAsname());
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, conf.getSysnum());
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, conf.getClinum());
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, conf.getUsername());
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, conf.getPassword());
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "en");

		return connectProperties;
	}

	private JCoFunction setFunction(JCoDestination jcoDestination, String functionName) throws JCoException {

		return jcoDestination.getRepository().getFunction(functionName);
	}

	private JCoFunction setImportValue(JCoFunction jcoFunction, String param, String value) {

		jcoFunction.getImportParameterList().setValue(param, value);

		return jcoFunction;
	}

	private String getReturn(JCoFunction jcoFunction, String parameterName) {

		JCoParameterList paramList = jcoFunction.getExportParameterList();
		String structure = "";
		String value = "";

		// default value for structureName is RETURN
		if (parameterName.isEmpty())
			parameterName = "RETURN";

		if ("SESSIONID".equals(parameterName)) {
			// Some functions return a Value instead of a Structure
			// e.g.: BAPI_XMI_LOGON
			value = paramList.getValue(parameterName).toString();
			return value;

		} else {
			// Returns a structure
			structure = paramList.getStructure(parameterName).toString();
			return structure; //TODO: Parse the type field in case of error
		}
	}

}
	