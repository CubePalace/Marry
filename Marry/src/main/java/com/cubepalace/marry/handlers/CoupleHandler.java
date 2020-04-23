package com.cubepalace.marry.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.cubepalace.marry.Marry;
import com.cubepalace.marry.util.Couple;

public class CoupleHandler {

	private Marry plugin;

	public CoupleHandler(Marry plugin) {
		this.plugin = plugin;
	}

	public void addNewCouple(Player player1, Player player2) {
		Couple newCouple = new Couple(player1.getUniqueId(), player2.getUniqueId());
		plugin.addCouple(newCouple);
		for (Couple couple : getPendingProposalsTo(player1)) {
			plugin.removePendingCouple(couple);
			if (!couple.equals(newCouple))
				plugin.getServer().getPlayer(couple.getOther(player1.getUniqueId())).sendMessage(
						ChatColor.RED + player1.getName() + " is now married, your proposal has been rejected.");
		}
		
		for (Couple couple : getPendingProposalsTo(player2)) {
			plugin.removePendingCouple(couple);
			if (!couple.equals(newCouple))
				plugin.getServer().getPlayer(couple.getOther(player2.getUniqueId())).sendMessage(
						ChatColor.RED + player2.getName() + " is now married, your proposal has been rejected.");
		}
		
		for (Couple couple : getPendingProposalsFrom(player1)) {
			plugin.removePendingCouple(couple);
			if (!couple.equals(newCouple))
				plugin.getServer().getPlayer(couple.getOther(player1.getUniqueId())).sendMessage(
						ChatColor.RED + player1.getName() + " is now married, they have rescinded their proposal.");
		}
		
		for (Couple couple : getPendingProposalsFrom(player2)) {
			plugin.removePendingCouple(couple);
			if (!couple.equals(newCouple))
				plugin.getServer().getPlayer(couple.getOther(player2.getUniqueId())).sendMessage(
						ChatColor.RED + player2.getName() + " is now married, they have rescinded their proposal.");
		}
	}

	public void removeCouple(Player player) {
		plugin.removeCouple(getCoupleFromPlayer(player));
	}

	public Couple getCoupleFromPlayer(Player player) {
		for (Couple couple : plugin.getCouples()) {
			if (couple.getPlayer1().equals(player.getUniqueId()) || couple.getPlayer2().equals(player.getUniqueId())) {
				return couple;
			}
		}
		return null;
	}

	public Couple getCoupleFromUUID(UUID uuid) {
		for (Couple couple : plugin.getCouples()) {
			if (couple.getPlayer1().equals(uuid) || couple.getPlayer2().equals(uuid))
				return couple;
		}
		return null;
	}

	public Couple getPendingCoupleFromPlayers(Player player1, Player player2) {
		for (Couple couple : plugin.getPendingCouples().keySet()) {
			if ((couple.getPlayer1().equals(player1.getUniqueId()) && couple.getPlayer2().equals(player2.getUniqueId()))
					|| couple.getPlayer1().equals(player2.getUniqueId()) && couple.getPlayer2().equals(player1.getUniqueId())) {
				return couple;
			}
		}
		return null;
	}

	// Returns a list of all proposals SENT TO player
	public List<Couple> getPendingProposalsTo(Player player) {
		List<Couple> pendingCouples = new ArrayList<>();
		for (Couple couple : plugin.getPendingCouples().keySet()) {
			if (couple.getPlayer1().equals(player.getUniqueId()) || couple.getPlayer2().equals(player.getUniqueId())) {
				if (!plugin.getPendingCouples().get(couple).equals(player.getUniqueId()))
					pendingCouples.add(couple);
			}
		}
		return pendingCouples;
	}

	// Returns a list of all proposals SENT FROM player
	public List<Couple> getPendingProposalsFrom(Player player) {
		List<Couple> pendingCouples = new ArrayList<>();
		for (Couple couple : plugin.getPendingCouples().keySet()) {
			if (couple.getPlayer1().equals(player.getUniqueId()) || couple.getPlayer2().equals(player.getUniqueId())) {
				if (plugin.getPendingCouples().get(couple).equals(player.getUniqueId()))
					pendingCouples.add(couple);
			}
		}
		return pendingCouples;
	}

}
