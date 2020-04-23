package com.cubepalace.marry.util;

import java.util.UUID;

import org.bukkit.ChatColor;

public class Couple {

	private UUID player1;
	private UUID player2;
	
	public Couple(UUID player1, UUID player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public UUID getPlayer1() {
		return player1;
	}
	
	public UUID getPlayer2() {
		return player2;
	}
	
	public boolean equals(Couple couple) {
		return (player1.equals(couple.getPlayer1()) && player2.equals(couple.getPlayer2())) || (player1.equals(couple.getPlayer2()) && player2.equals(couple.getPlayer1()));
	}
	
	public UUID getOther(UUID player) {
		if (player.equals(player1))
			return player2;
		if (player.equals(player2))
			return player1;
		return null;
	}
	
	public String toString() {
		return ChatColor.GREEN + player1.toString() + " is married to " + player2.toString();
	}
}
