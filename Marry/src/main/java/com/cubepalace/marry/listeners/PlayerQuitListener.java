package com.cubepalace.marry.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cubepalace.marry.Marry;
import com.cubepalace.marry.util.Couple;

public class PlayerQuitListener implements Listener {

	private Marry plugin;
	
	public PlayerQuitListener(Marry plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		List<Couple> pendingCouplesTo = plugin.getCoupleHandler().getPendingProposalsTo(player);
		for (Couple couple : pendingCouplesTo) {
			UUID otherUUID = couple.getOther(player.getUniqueId());
			Player other = plugin.getServer().getPlayer(otherUUID);
			other.sendMessage(ChatColor.RED + player.getName() + " has disconnected, cancelling the pending proposal");
			plugin.removePendingCouple(couple);
		}
		
		List<Couple> pendingCouplesFrom = plugin.getCoupleHandler().getPendingProposalsFrom(player);
		for (Couple couple : pendingCouplesFrom) {
			UUID otherUUID = couple.getOther(player.getUniqueId());
			Player other = plugin.getServer().getPlayer(otherUUID);
			other.sendMessage(ChatColor.RED + player.getName() + " has disconnected, cancelling the pending proposal");
			plugin.removePendingCouple(couple);
		}
	}
}
