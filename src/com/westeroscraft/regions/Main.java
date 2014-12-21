package com.westeroscraft.regions;
//This plugin was written by Will Blew for WesterosCraft in 2014
//This plugin is based on the concepts of ALL team members.

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import com.avaje.ebean.Query;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Main extends JavaPlugin implements Listener  {
	public Statement statement;
	public ResultSet resultSet;
	ConfigManager cSQL = new ConfigManager();
	
	//on disable
	public void onDisable() {
		log("WesterosRegions Closing...");
		
	}
	
    //logger
    public void log(String text) {
    	this.getLogger().log(Level.INFO, text);
    }
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
        log("WesterosRegions Enabled!");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
		//add to players if not found
		Connection con;
		con = cSQL.conJoner();
		String p = event.getPlayer().getName().toString();
		String csql = ("SELECT COUNT(name) as rowcount FROM Players WHERE name ='" + p + "'");
		statement = con.createStatement();
		ResultSet results = statement.executeQuery(csql);
		results.next();
		int count = results.getInt("rowcount");
		results.close();
		log("Players Found: " + count);
		if (count == 0) {
			//Date
	    	Calendar cal = Calendar.getInstance();
	    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//construct
			String usql = ("INSERT INTO Players VALUES (0,'" + p + "','none',0,Now())");
			//create them
			
			statement.executeUpdate(usql);
		}
			
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e) throws SQLException
	{
		Player p = e.getPlayer();
		Double price = e.getRegion().getFlag(DefaultFlag.PRICE);
		String Region = e.getRegion().getId();
		Connection con;
		
		if (p.getTotalExperience() < price.floatValue()) 
		{
		// Say no
		e.getPlayer().sendMessage(ChatColor.RED + "You just tried to enter " + Region + " it takes " + price + "XP and you only have " + ((int)p.getExp()));
		// Kill it with fire
		e.setCancelled(true);
		// Set new location with smallest X as possible.
		Location location = new Location(p.getWorld(),(p.getLocation().getX() - 20),p.getLocation().getY(),p.getLocation().getZ());
		p.teleport(location); //do it
		
		
		
		}
		else {
			p.giveExp((int) -price);
			e.getPlayer().sendMessage(ChatColor.GREEN + "You just entered " + Region + " it cost you " + price);
			
			// Location logic goes here
			//get region info
			String regSql = ("SELECT * FROM Regions WHERE name ='" + Region + "'");
			try {
				con = cSQL.conJoner();
				statement = con.createStatement();
				ResultSet results = statement.executeQuery(regSql);
				results.next();
				String regId = results.getString("id");
				String regLock = results.getString("locked");
				String regactions = results.getString("actions");
				results.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log(e1.toString());
			}
			// Spawn instance
			
				// Check minimum players for instance
			
					// Execute action to start event
			
			// Instance cleanup
			
		//OR
			
			// Lock area
			
				// Call events for area even if X players in area
			
			// Area cleanup
			
				// Call regen based on Prisim data? WHAT WHAT!
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("wrc")) {
			Connection con;
			try {
				con = cSQL.conJoner();
				log("Getting count of Westeros regions");
				try {
					statement = con.createStatement();
					resultSet = statement
							.executeQuery("SELECT COUNT(id) AS rowcount FROM Regions");
					resultSet.next();
					int count = resultSet.getInt("rowcount");
					resultSet.close();
					this.getServer().broadcastMessage("There are currently " + count + " regions.");
					con.close();
					return true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log(e.toString());
					return false;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log(e1.toString());
				return false;
			}
		}
		return false;
		
	}
}