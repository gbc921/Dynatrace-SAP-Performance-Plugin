package com.dynatrace.perfomance.sap;

import java.util.logging.Logger;

import com.dynatrace.diagnostics.pdk.MonitorEnvironment;

public class Config {
	
	private static final Logger log = Logger.getLogger(Config.class.getName());
	
	String asname;
	String sysname;
	String sysnum;
	String clinum;
	String username;
	String password;
	String domain;
	String fqdn;

	Config(MonitorEnvironment env) {
		log.finer("Config Begin");
		
		setAsname(env.getConfigString("asname"));
		setSysnum(env.getConfigString("sysname"));
		setSysnum(env.getConfigLong("sysnum").toString());
		setClinum(env.getConfigLong("clinum").toString());
		setUsername(env.getConfigString("username"));
		setPassword(env.getConfigPassword("password"));
		setSysname(env.getConfigString("sysname"));
		
		log.finer("Config End");
	}
	
	String getSysnum() {
		return sysnum;
	}
	String getSysname() {
		return sysname;
	}
	String getUsername() {
		return username;
	}
	String getPassword() {
		return password;
	}
	String getClinum() {
		return clinum;
	}
	String getAsname() {
		return asname;
	}
	String getDomain() {
		return domain;
	}
	String getFqdn() {
		return fqdn;
	}
	void setSysnum(String sysnum) {
		this.sysnum = sysnum;
	}
	void setSysname(String sysname) {
		this.sysname = sysname;
	}
	void setUsername(String username) {
		this.username = username;
	}
	void setPassword(String password) {
		this.password = password;
	}
	void setClinum(String clinum) {
		this.clinum = clinum;
	}
	void setAsname(String asname) {
		this.asname = asname;
	}
	void setDomain(String domain) {
		this.domain = domain;
	}
	void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}
}
