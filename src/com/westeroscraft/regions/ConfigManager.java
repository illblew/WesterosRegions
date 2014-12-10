package com.westeroscraft.regions;

import com.westeroscraft.regions.*;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.DriverManager;
import java.sql.Statement;

// Author: Will Blew

public class ConfigManager {
	private String password;
	private Statement query;
	
	public void write(Statement query) {
		// Set $thing
		this.query = query;
	}
	
	public void read(Statement query) {
		// Read $thing
		this.query = query;
	}
	
	public Object conJoner() throws Exception {
		Statement query = null;
		String host = "127.0.0.1"; //yaml
		String port = "3306"; // More yaml
		String user = "WesterosRegions"; //yaml
		String password = "hunter2"; // Even more yaml load
		
		// Do the deal
		String url = "jdbc:mysql://" + host + ":" + port + "/"
				+ query + "autoReconnectForPools=true";
		return DriverManager.getConnection(url,user,password);
	}

}
