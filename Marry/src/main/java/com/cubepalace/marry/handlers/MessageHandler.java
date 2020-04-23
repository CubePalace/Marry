package com.cubepalace.marry.handlers;

import org.bukkit.ChatColor;

import com.cubepalace.marry.Marry;

public class MessageHandler {

	private Marry plugin;
	
	public MessageHandler(Marry plugin) {
		this.plugin = plugin;
	}
	
	public String getMessage(String input) {
		return ChatColor.translateAlternateColorCodes('&', plugin.getMsgFile().getConfig().getString(input));
	}
	
	public String getMessagePlayerReplacer(String input, String replaceText) {
		return ChatColor.translateAlternateColorCodes('&', plugin.getMsgFile().getConfig().getString(input).replace("%PLAYER%", replaceText));
	}
	
	public String getMessageTwoPlayerReplacer(String input, String player1, String player2) {
		return ChatColor.translateAlternateColorCodes('&', plugin.getMsgFile().getConfig().getString(input).replace("%PLAYER1%", player1).replace("%PLAYER2%", player2));
	}
	
	public String getMessageValueReplacer(String input, String replaceText) {
		return ChatColor.translateAlternateColorCodes('&', plugin.getMsgFile().getConfig().getString(input).replace("%VALUE%", replaceText));
	}
	
	public String getMessageTwoNumberReplacer(String input, String current, String total) {
		return ChatColor.translateAlternateColorCodes('&', plugin.getMsgFile().getConfig().getString(input).replace("%CURRENT%", current).replace("%TOTAL%", total));
	}
}
