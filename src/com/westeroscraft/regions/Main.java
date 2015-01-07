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
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
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
	public int count;
	public int locked;
	public int Switch = 0;
	public String Region;
	public Player p;
	ConfigManager cSQL = new ConfigManager();
	commander CMD = new commander();
	
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
		String p = event.getPlayer().getName().toString();
		String csql = ("SELECT COUNT(name) as rowcount FROM Players WHERE name ='" + p + "'");
		ResultSet results = cSQL.executeQuery(csql);
		int count = results.getInt("rowcount");
		results.close();
		log("Players Found: " + count);
		if (count == 0) {
			//construct
			String usql = ("INSERT INTO Players VALUES (0,'" + p + "','none',0,Now())");
			//create them	
			cSQL.writeQuery(usql);
		}
			
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e) throws SQLException
	{
		String pname =  e.getPlayer().getName().toString();
		Player pReg = e.getPlayer();
		Double price = e.getRegion().getFlag(DefaultFlag.PRICE);
		Region = e.getRegion().getId();
		
		if (pReg.getTotalExperience() < price.floatValue()) 
		{
		// Say no
		e.getPlayer().sendMessage(ChatColor.RED + "You just tried to enter " + Region + " it takes " + price + "XP and you only have " + ((int)pReg.getExp()));
		// Kill it with fire
		e.setCancelled(true);
		// Set new location with smallest X as possible.
		Location location = new Location(pReg.getWorld(),(pReg.getLocation().getX() - 20),pReg.getLocation().getY(),pReg.getLocation().getZ());
		pReg.teleport(location); //do it
		
		
		
		}
		else {
			Region = e.getRegion().getId();
			// Location logic goes here
			//get region info
			String regSql = ("SELECT * FROM Regions WHERE name ='" + Region + "'");
			String updateSql = ("UPDATE Players SET area = '" + Region + "' WHERE name = '" + pname + "'");
			try {
				ResultSet results = cSQL.executeQuery(regSql);
				String regId = results.getString("id");
				int regLock = results.getInt("locked");
				String regactions = results.getString("actions");
				int regMax = results.getInt("max");
				String regType = results.getString("type");
				count = (results.getInt("count"));
				if (count == regMax || regLock == 1) {
					// kick if max is met or locked
					// Say no
					e.getPlayer().sendMessage(ChatColor.RED + "You just tried to enter " + Region + ". Sorry, it's already full or locked!");
					// Kill it with fire
					e.setCancelled(true);
					// Set new location with smallest X as possible.
					Location location = new Location(pReg.getWorld(),(pReg.getLocation().getX() - 20),pReg.getLocation().getY(),pReg.getLocation().getZ());
					pReg.teleport(location); //do it
					results.close();
				}
				else 
				{
					count = (count + 1);
					String countSql = ("UPDATE Regions SET count = '" + count + "' WHERE name ='" + Region + "'");
				if (regMax == count && regType.equals("instance")) {
					log("Attempting to lock instance.");
					String lockSql = ("UPDATE Regions SET locked = 1 WHERE name ='" + Region + "'");
					CMD.sender("region flag " + Region + " -w " + pReg.getWorld().toString() + " exit deny");
					this.getServer().broadcastMessage("Event at region " + Region + "will begin shortly. " + Region + " is now locked!");
					cSQL.writeQuery(lockSql);
					log("Locked instance! World: " + pReg.getWorld().getName());
				}
				log("Count: " + count + " Type: " + regType + " Max: " + regMax);
				pReg.giveExp((int) -price);
				e.getPlayer().sendMessage(ChatColor.GREEN + "You just entered " + Region + " it cost you " + price);
				cSQL.writeQuery(updateSql);
				cSQL.writeQuery(countSql);
				results.close();
				}
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
			
				// Call regen based on Prisim ? WHAT WHAT!
		}
	}
	
	@EventHandler
	public void OnRegionLeave(RegionLeaveEvent event) throws SQLException {
		String pname = event.getPlayer().getName().toString();
		Player pReg = event.getPlayer();
		//query
		String countSql = ("SELECT * FROM Regions WHERE name ='" + Region + "'");
		String updatePlayer = ("UPDATE Players SET area = 'none' WHERE name ='" + pname  + "'");
		//Region info
		Double price = event.getRegion().getFlag(DefaultFlag.PRICE);
		ResultSet results = cSQL.executeQuery(countSql);
		count = results.getInt("count");
		//User real current count
		String updateRegion = ("UPDATE Regions SET count ='" + (count - 1) + "' WHERE name = '" + Region + "'");
		results.close();
		
		//update
		cSQL.writeQuery(updatePlayer); //Do player.
		cSQL.writeQuery(updateRegion); //Do region.
		}	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("wrc")) {
				log("Getting count of Westeros regions");
					ResultSet results;
					try {
						log("Trying to read results of region count.");
						results = cSQL.executeQuery("SELECT COUNT(id) AS rowcount FROM Regions");
						log("Sent Query.");
						int count = results.getInt("rowcount");
						log("Got count.");
						this.getServer().broadcastMessage("There are currently " + count + " regions.");
						log("Done getting region count");
						return true;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						log(e.toString());
						this.getServer().broadcastMessage(e.toString());
						return false;
					}
			}
		return false; 
		
	}
}