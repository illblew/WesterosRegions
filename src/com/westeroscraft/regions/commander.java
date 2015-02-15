package com.westeroscraft.regions;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class commander {
	Server server = Bukkit.getServer();
	public void sender(String command) {
		server.dispatchCommand(Bukkit.getConsoleSender(), command);
		log("Command: " + command);
	}
	
    //logger
    public static void log(String text) {
    	Bukkit.getLogger().log(Level.WARNING, text);
    }
}
