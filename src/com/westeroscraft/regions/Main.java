package com.westeroscraft.regions;
//This plugin was written by Will Blew for WesterosCraft in 2014
//This plugin is based on the concepts of ALL team members.

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.westeroscraft.regions.MysqlConnect;

import java.sql.Connection;
import java.util.logging.Level;

import com.avaje.ebean.Query;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class Main extends JavaPlugin implements Listener  {
	
	//on disable
	public void onDisable() {
		log("WesterosRegions Closing...");
		
	}
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
        log("WesterosRegions Enabled!");
        
        //make connection
        ConfigManager cSQL = new ConfigManager();
        //dial up
        Connection con = (Connection) cSQL.conJoner();
	}
	
    //logger
    public void log(String text) {
    	this.getLogger().log(Level.INFO, text);
    }
    
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e)
	{
		Player p = e.getPlayer();
		Double price = e.getRegion().getFlag(DefaultFlag.PRICE);
		
		if (p.getTotalExperience() < price.floatValue()) 
		{
		// Say no.
		e.getPlayer().sendMessage(ChatColor.RED + "You just tried to enter " + e.getRegion().getId() + " it takes " + price + "XP and you only have " + ((int)p.getExp()));
		// Kill it with fire
		e.setCancelled(true);
		// Set new location with smallest X as possible.
		Location location = new Location(p.getWorld(),(p.getLocation().getX() - 10),p.getLocation().getY(),p.getLocation().getZ());
		p.teleport(location); //do it
		
		
		
		}
		else {
			p.giveExp((int) -price);
			e.getPlayer().sendMessage(ChatColor.GREEN + "You just entered " + e.getRegion().getId() + " it cost you " + price);
			
			// Location logic goes here
			
				// Aka check t ype of area
			
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
}
