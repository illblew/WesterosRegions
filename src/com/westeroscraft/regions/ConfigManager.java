package com.westeroscraft.regions;

import com.westeroscraft.regions.*;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

// Author: Will Blew

public class ConfigManager {
	private Statement query;
	
	public void write(Statement query) {
		// Set $thing
		this.query = query;
	}
	
	public void read(Statement query) {
		// Read $thing
		this.query = query;
	}
	
	public Connection conJoner() throws Exception {
		// Do the deal;
		// Totes real creds. -_-
		return DriverManager.getConnection("jdbc:mysql://localhost/WesterosRegions?user=WesterosRegions&password=hunter2");
	}

}
