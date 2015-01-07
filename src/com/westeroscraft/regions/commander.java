package com.westeroscraft.regions;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class commander {
	Server server = Bukkit.getServer();
	public void sender(String command) {
		server.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
}
