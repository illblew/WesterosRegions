package com.westeroscraft.regions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;

// Author: Will Blew

public class ConfigManager {
	

    public ResultSet executeQuery(String query){
    	  Connection conn;
    	  ResultSet results;
    	  String url = "jdbc:mysql://localhost/WesterosRegions?user=WesterosRegions&password=hunter2";
    	 
    	  //Attempt to connect
    	  try{
    	    //Connection succeeded
    	    conn = DriverManager.getConnection(url);
    	    PreparedStatement statement = conn.prepareStatement(query);
    	    results = statement.executeQuery();
    	    results.next();
    	    log("Read some data!");
    	    return results;
    	  } catch(Exception e){
    	    //Couldn't connect to the database
    		  log(e.toString());
    		  return null;
    	  }
    	}
    
    public void writeQuery(String query) throws SQLException{
  	  Connection conn = null;
  	  PreparedStatement statement = null;
  	  String url = "jdbc:mysql://127.0.0.1/WesterosRegions?user=WesterosRegions&password=hunter2";
  	 
  	  	log("Writing Data...");
  	    //Attempt it
  	    conn = DriverManager.getConnection(url);
  	    statement = conn.prepareStatement(query);
  	    statement.executeUpdate();
  	    statement.close();
  	    conn.close();
  	    log("Wrote the data and closed the commection!");

  	}
	
    //logger
    public static void log(String text) {
    	Bukkit.getLogger().log(Level.WARNING, text);
    }

}
