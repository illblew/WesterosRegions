package com.westeroscraft.regions;

import com.westeroscraft.regions.*;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

// Author: Will Blew

public class ConfigManager {
	private String query;
	private Statement statement;
	private String error;
	Connection conJonner;
	
	public ResultSet write(String query) throws SQLException {
		// Read $thing
		this.query = query;
		ResultSet results;
		//Do query
		statement = conJonner.createStatement();
		results = statement.executeQuery(query);
		results.next();
		results.close();
		return results;
	}
	
	
	public ResultSet read(String query) throws SQLException {
		// Read $thing
		
		this.query = query;
		ResultSet results;
		//Do query
		statement = conJonner.createStatement();
		results = statement.executeQuery(query);
		results.next();
		results.close();
		return results;
	}
	
	public Connection conJoner() throws Exception {
		// Do the deal;
		// Totes real creds. -_-
		return DriverManager.getConnection("jdbc:mysql://88.198.107.46/WesterosRegions?user=WesterosRegions&password=hunter2");
	}

}
