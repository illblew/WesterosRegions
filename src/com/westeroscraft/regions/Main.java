package com.westeroscraft.regions;
//This plugin was written by Will Blew for WesterosCraft in 2014
//This plugin is based on the concepts of ALL team members.

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;

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
	}
	
    //logger
    public void log(String text) {
    	this.getLogger().log(Level.INFO, text);
    }
    
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e)
	{
	  e.getPlayer().sendMessage("You just entered " + e.getRegion().getId());
	}
}
