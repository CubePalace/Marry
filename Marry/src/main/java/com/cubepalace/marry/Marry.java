package com.cubepalace.marry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.cubepalace.marry.commands.MarryCommand;
import com.cubepalace.marry.handlers.CoupleHandler;
import com.cubepalace.marry.handlers.MessageHandler;
import com.cubepalace.marry.listeners.PlayerQuitListener;
import com.cubepalace.marry.util.Couple;
import com.cubepalace.marry.util.CouplesFile;
import com.cubepalace.marry.util.MessagesFile;

public class Marry extends JavaPlugin {

	private List<Couple> couples;
	private Map<Couple, UUID> pendingCouples;
	private CoupleHandler coupleHandler;
	private MessageHandler msgHandler;
	private CouplesFile couplesFile;
	private MessagesFile msgFile;
	
	// TODO: Add config
	
	@Override
	public void onEnable() {
		register();
		init();
		getLogger().info("Marry enabled");
	}
	
	private void register() {
		getCommand("marry").setExecutor(new MarryCommand(this));
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
	}
	
	private void init() {
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		coupleHandler = new CoupleHandler(this);
		msgHandler = new MessageHandler(this);
		couplesFile = new CouplesFile(this, "couples.yml");
		couples = couplesFile.loadCouples();
		couplesFile.saveCouples(couples);
		pendingCouples = new HashMap<>();
		saveResource("messages.yml", false);
		msgFile = new MessagesFile(this, "messages.yml");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Marry disabled");
	}
	
	public CoupleHandler getCoupleHandler() {
		return coupleHandler;
	}
	
	public List<Couple> getCouples() {
		return couples;
	}
	
	public void addCouple(Couple couple) {
		couples.add(couple);
		couplesFile.saveCouples(couples);
	}
	
	public void removeCouple(Couple couple) {
		couples.remove(couple);
		couplesFile.saveCouples(couples);
	}
	
	public Map<Couple, UUID> getPendingCouples() {
		return pendingCouples;
	}
	
	public void addPendingCouple(Couple couple, UUID proposer) {
		pendingCouples.put(couple, proposer);
	}
	
	public void removePendingCouple(Couple couple) {
		pendingCouples.remove(couple);
	}
	
	public MessagesFile getMsgFile() {
		return msgFile;
	}
	
	public MessageHandler getMsgHandler() {
		return msgHandler;
	}
}
