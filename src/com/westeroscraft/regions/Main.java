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

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

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
	public String command;
	public String Region;
	public Player p;
	public State exStatus;
	public State entStatus;
	
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
		String pWorld = pReg.getWorld().getName();
		String updateSql = ("UPDATE Players SET area = '" + Region + "' WHERE name = '" + pname + "'");
		exStatus = e.getRegion().getFlag(DefaultFlag.EXIT);
		entStatus = e.getRegion().getFlag(DefaultFlag.ENTRY);
		log(updateSql);
		cSQL.writeQuery(updateSql);
		
		if (getLockState(Region) == 0 && takeCash(price,pReg,Region) == true) {
			log( pname + " entered " + Region + " Status:" + entStatus.toString());
		} else {
			log("Trying to kill event!");
			e.isCancelled();
			log("I killed the enter event!");
		}
		
	}
	
	@EventHandler
	public void OnRegionLeave(RegionEnterEvent event) throws SQLException {
		String pname = event.getPlayer().getName().toString();
		Player pReg = event.getPlayer();
		Region = event.getRegion().getId();
		if (getLockState(Region) == 0) {
			log(pname + " has left " + Region + " Status:" + entStatus.toString());
		} else 
		{
			log("Trying to kill event!");
			event.isCancelled();
			log("I killed the exit event!");
		}
		}

	
	public void PrisimCleanup(String region) {
		
		
	}
	
	public int getLockState(String region) throws SQLException {
		String checkSQL = "SELECT locked FROM Regions WHERE name = '" + region + "'";
		ResultSet results = cSQL.executeQuery(checkSQL);
		return results.getInt("locked");
	}
	
	public boolean takeCash(Double price, Player player, String region) {
		int playerXp = player.getTotalExperience();
		if (playerXp < price.floatValue()) {
			log("Player has only " + playerXp + " XP");
			return false;
		} else {
			log("Player had the XP, they may enter.");
			return true;
		}
	}
	
	public void CheckState(String region, int lock,int count, String world) throws SQLException {
		String sqlUnlock = "UPDATE Regions SET locked = '0' WHERE name ='" + region + "'";
		if (count == 0 && lock == 0) {
			cSQL.writeQuery(sqlUnlock);
		}
	}
	
	public void LockExit (String region,String world) throws SQLException {
		String lockCommand = "region flag -w " + world + " " + region + " exit deny";
		String sqlLock = "UPDATE Regions SET locked = '1' WHERE name ='" + region + "'";
		CMD.sender(lockCommand);
		cSQL.writeQuery(sqlLock);
	}
	public void LockEnter (String region,String world) throws SQLException {
		String lockCommand = "region flag -w " + world + " " + region + " entry deny";
		String sqlLock = "UPDATE Regions SET locked = '1' WHERE name ='" + region + "'";
		CMD.sender(lockCommand);
		cSQL.writeQuery(sqlLock);
		this.getServer().broadcastMessage("Event at region " + Region + " will begin shortly. " + Region + " is now locked!");
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