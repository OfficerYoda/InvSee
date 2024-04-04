package Main;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Commands.cmdCraft;
import Commands.cmdEc;
import Commands.cmdEditSign;
import Commands.cmdInvSee;
import Enums.Commands;
import Items.Items;
import Listener.GUI.CustomPermissonListener;
import Listener.GUI.InventoryListener;
import Listener.GUI.OptionListener;
import Listener.Player.AllInvListener;
import Listener.Player.JoinListener;
import Listener.Player.QuitListener;
import Listener.Player.RightClickListener;
import Manager.EnderchestManager;
import Manager.InventoryManager;
import Manager.KnownPlayerManager;
import Manager.OptionManager;
import Manager.PermissionManager;
import Utils.config;

public class Main extends JavaPlugin {
	static Main instance;
	config config;
	
	PermissionManager permissionManager;
	
	public static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "InvSee" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

	@Override
	public void onLoad() {
		instance = this;
		config = new config();
	}

	/*
	 * TO-DO:
	 * -saven von permissions fixen
	 * -inventar von anderen nur saven wenn auch bearbeitet wurde
	 * -update inv view bei drop etc + item am cursor entfernen => vieleicht einfach methode wieder aufrufen ((schauen ob dropppen zu interact zählt) ? guiFromPlayerInv : null)
	 * -inv vom target bei jeder interaction updaten ==> keine buttons mehr nötig
	 * -wenn einer einloggt dessen ec gerade geviewed wird, von config view to ingame view wechseln
	 * 
	 * -custom Permissions(anzeige pannels ändern sich nur einmal)
	 */

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "InvSee 3.0");

		new KnownPlayerManager();
		permissionManager = new PermissionManager();
		new OptionManager();
		new InventoryManager();
		new EnderchestManager();

		Items.initItems();

		getCommand("invsee").setExecutor(new cmdInvSee());
		getCommand("ec").setExecutor(new cmdEc());
		getCommand("craft").setExecutor(new cmdCraft());
		getCommand("editsign").setExecutor(new cmdEditSign());

		PluginManager manager = Bukkit.getPluginManager();

		manager.registerEvents(new InventoryListener(), this);
		manager.registerEvents(new OptionListener(), this);
		manager.registerEvents(new CustomPermissonListener(), this);

		manager.registerEvents(new JoinListener(), this);
		manager.registerEvents(new QuitListener(), this);
		manager.registerEvents(new AllInvListener(), this);
		manager.registerEvents(new RightClickListener(), this);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onDisable() {
		try {
			closeCustomInventories();
			permissionManager.save();
			KnownPlayerManager.save();
		} catch (IllegalArgumentException | IOException e) {}
		config.save();
	}

	private void closeCustomInventories() throws IllegalArgumentException, IOException {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(InventoryManager.inventories.contains(player.getOpenInventory().getTopInventory())) {
				Inventory inventory = player.getOpenInventory().getTopInventory();
				InventoryListener.saveInventory(inventory.getContents(), InventoryListener.getViewedInvPlayerName(player));
				player.sendMessage(prefix + "Closed inventory because of server reload");
				player.closeInventory();
				continue;
			}
			if(EnderchestManager.enderchests.contains(player.getOpenInventory().getTopInventory())) {
				Inventory inventory = player.getOpenInventory().getTopInventory();
				InventoryListener.saveEnderchest(inventory, player, InventoryListener.getViewedInvPlayerName(player));
				player.closeInventory();
				player.sendMessage(prefix + "Closed enderchest because of server reload");
				continue;
			}
			if(OptionManager.optionMenus.contains(player.getOpenInventory().getTopInventory())) {
				player.closeInventory();
				player.sendMessage(prefix + "Closed options because of server reload");
			}
			for(Commands cmd : Commands.values()) {
				if(OptionManager.customPerMenus.get(cmd) == null) continue;
				
				if(OptionManager.customPerMenus.get(cmd).contains(player.getOpenInventory().getTopInventory())) {
					player.closeInventory();
					player.sendMessage(prefix + "Closed custom permissions because of server reload");
				}
			}
		}
	}

	public static Main getInstance() {
		return instance;
	}

	public config getConfiguration() {
		return config;
	}
	
	public PermissionManager getPermissionManager() {
		return permissionManager;
	}
}