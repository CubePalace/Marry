package com.cubepalace.marry.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cubepalace.marry.Marry;
import com.cubepalace.marry.util.Couple;

public class MarryCommand implements CommandExecutor {

	private Marry plugin;

	public MarryCommand(Marry plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equals("marry")) {
			if (!sender.hasPermission("marry.cmd")) {
				sender.sendMessage(plugin.getMsgHandler().getMessage("no-permission"));
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getMsgHandler().getMessage("console-command"));
				return true;
			}

			Player player = (Player) sender;

			if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
				showHelp(sender);
				return true;
			}

			if (args[0].equalsIgnoreCase("propose")) {
				if (isMarried(player)) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("propose.married"));
					return true;
				}

				if (args.length == 1) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("propose.usage"));
					return true;
				}

				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("propose.offline"));
					return true;
				}

				if (target.getUniqueId().equals(player.getUniqueId())) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("propose.self-marry"));
					return true;
				}

				if (isMarried(target)) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("propose.target-married"));
					return true;
				}

				target.sendMessage(
						plugin.getMsgHandler().getMessagePlayerReplacer("propose.receive", player.getName()));
				sender.sendMessage(
						plugin.getMsgHandler().getMessagePlayerReplacer("propose.confirmation", target.getName()));
				plugin.addPendingCouple(new Couple(player.getUniqueId(), target.getUniqueId()), player.getUniqueId());
			} else if (args[0].equalsIgnoreCase("accept")) {
				List<Couple> pendingProposals = plugin.getCoupleHandler().getPendingProposalsTo(player);
				if (pendingProposals.size() == 0) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("accept.no-pending"));
					return true;
				}

				if (args.length == 1) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("accept.usage"));
					return true;
				}

				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("accept.offline"));
					return true;
				}

				boolean validTarget = false;
				for (Couple couple : pendingProposals) {
					if (couple.getOther(player.getUniqueId()).equals(target.getUniqueId()))
						validTarget = true;
				}

				if (!validTarget) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("accept.wrong-player"));
					return true;
				}

				plugin.getCoupleHandler().addNewCouple(player, target);
				plugin.getServer().broadcastMessage(plugin.getMsgHandler()
						.getMessageTwoPlayerReplacer("accept.broadcast", player.getName(), target.getName()));
			} else if (args[0].equalsIgnoreCase("reject")) {
				List<Couple> pendingProposals = plugin.getCoupleHandler().getPendingProposalsTo(player);
				if (pendingProposals.size() == 0) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("reject.no-pending"));
					return true;
				}

				if (args.length == 1) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("reject.usage"));
					return true;
				}

				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("reject.offline"));
					return true;
				}

				boolean validTarget = false;
				for (Couple couple : pendingProposals) {
					if (couple.getOther(player.getUniqueId()).equals(target.getUniqueId()))
						validTarget = true;
				}

				if (!validTarget) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("reject.wrong-player"));
					return true;
				}

				player.sendMessage(plugin.getMsgHandler().getMessage("reject.confirmation"));
				target.sendMessage(
						plugin.getMsgHandler().getMessagePlayerReplacer("reject.proposer", player.getName()));
				plugin.removePendingCouple(plugin.getCoupleHandler().getPendingCoupleFromPlayers(player, target));
			} else if (args[0].equalsIgnoreCase("divorce")) {
				if (!isMarried(player)) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("divorce.not-married"));
					return true;
				}
				Couple couple = plugin.getCoupleHandler().getCoupleFromPlayer(player);

				plugin.getServer()
						.broadcastMessage(plugin.getMsgHandler().getMessageTwoPlayerReplacer("divorce.broadcast",
								player.getName(),
								plugin.getServer().getOfflinePlayer(couple.getOther(player.getUniqueId())).getName()));
				plugin.getCoupleHandler().removeCouple(player);
			} else if (args[0].equalsIgnoreCase("status")) {
				if (args.length == 1) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("status.usage"));
					return true;
				}

				UUID uuid;
				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					@SuppressWarnings("deprecation")
					OfflinePlayer offlineTarget = plugin.getServer().getOfflinePlayer(args[1]);
					uuid = offlineTarget.getUniqueId();
				} else {
					uuid = target.getUniqueId();
				}

				Couple couple = plugin.getCoupleHandler().getCoupleFromUUID(uuid);

				if (couple == null) {
					sender.sendMessage(plugin.getMsgHandler().getMessagePlayerReplacer("status.not-married", args[1]));
				} else {
					OfflinePlayer other = plugin.getServer().getOfflinePlayer(couple.getOther(uuid));
					sender.sendMessage(plugin.getMsgHandler().getMessageTwoPlayerReplacer("status.married", args[1], other.getName()));
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				if (args.length == 1) {
					showMarryList(sender, 1);
				} else {
					try {
						int page = Integer.parseInt(args[1]);
						showMarryList(sender, page);
					} catch (NumberFormatException ex) {
						sender.sendMessage(plugin.getMsgHandler().getMessageValueReplacer("list.invalid", args[1]));
					}
				}
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("marry.reload")) {
					sender.sendMessage(plugin.getMsgHandler().getMessage("no-permission"));
					return true;
				}

				plugin.getMsgFile().reload();
				sender.sendMessage(plugin.getMsgHandler().getMessage("reload"));
			} else {
				showHelp(sender);
			}
		}
		return true;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "/marry help: Shows this help menu");
		sender.sendMessage(ChatColor.GREEN + "/marry propose <player>: Propose to a player");
		sender.sendMessage(ChatColor.GREEN + "/marry accept <player>: Accept a pending proposal");
		sender.sendMessage(ChatColor.GREEN + "/marry reject <player>: Reject a pending proposal");
		sender.sendMessage(ChatColor.GREEN + "/marry divorce: End your current marriage");
		sender.sendMessage(ChatColor.GREEN + "/marry status <player>: Get the status of a player's marriage");
		sender.sendMessage(ChatColor.GREEN + "/marry list [page]: List the married couples on the server");
		if (sender.hasPermission("marry.reload"))
			sender.sendMessage(ChatColor.GREEN + "/marry reload: Reloads the plugin");
	}

	private boolean isMarried(Player player) {
		return plugin.getCoupleHandler().getCoupleFromPlayer(player) != null;
	}

	private void showMarryList(CommandSender sender, int page) {
		sender.sendMessage(plugin.getMsgHandler().getMessage("list.header"));
		sender.sendMessage(plugin.getMsgHandler().getMessageTwoNumberReplacer("list.page-format", String.valueOf(page), String.valueOf(pageCount())));
		if (plugin.getCouples().size() == 0) {
			sender.sendMessage(plugin.getMsgHandler().getMessage("list.no-couples"));
		} else {
			for (int i = (page - 1) * 10; i < page * 10 - 1; i++) {
				try {
					Couple couple = plugin.getCouples().get(i);
					String player1 = plugin.getServer().getOfflinePlayer(couple.getPlayer1()).getName();
					String player2 = plugin.getServer().getOfflinePlayer(couple.getPlayer2()).getName();
					sender.sendMessage(plugin.getMsgHandler().getMessageTwoPlayerReplacer("list.entry-format", player1, player2));
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
			}
		}
	}

	private int pageCount() {
		if (plugin.getCouples().size() % 10 == 0)
			return plugin.getCouples().size() / 10;
		else
			return plugin.getCouples().size() / 10 + 1;
	}
}
