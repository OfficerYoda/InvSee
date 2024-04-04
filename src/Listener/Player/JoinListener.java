package Listener.Player;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Enums.ViewedInvType;
import Listener.GUI.InventoryListener;
import Main.Main;
import Manager.EnderchestManager;
import Manager.InventoryManager;
import Manager.KnownPlayerManager;

public class JoinListener implements Listener {
	String prefix = Main.prefix;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) throws IllegalArgumentException, IOException {
		Player joinedPlayer = event.getPlayer();
		
		if (KnownPlayerManager.knowsPlayer(joinedPlayer)) {
			joinedPlayer.getInventory().setContents(InventoryManager.loadInv(joinedPlayer.getName()).length > 0 ? InventoryManager.loadInv(joinedPlayer.getName()) : joinedPlayer.getInventory().getContents());
			joinedPlayer.getEnderChest().setContents(EnderchestManager.loadEc(joinedPlayer));
		}
		KnownPlayerManager.addPlayer(joinedPlayer.getName());
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(player.getOpenInventory() != null)
				if(InventoryListener.viewedInv.containsKey(player)) {
					InventoryListener.viewedInvPlayerName.keySet().forEach(plyer -> {
						if(InventoryListener.getViewedInvPlayerName(plyer) == joinedPlayer.getName()) {
							plyer.closeInventory();
							plyer.sendMessage(prefix + "Closed " + (InventoryListener.getViewedInvType(plyer) == ViewedInvType.INVENTORY) != null ? "inventory" : "enderchest"
							+ " because " + ChatColor.GREEN + joinedPlayer.getName() + ChatColor.GRAY + " joined.");
							InventoryManager.guiFromPlayerInventory(joinedPlayer, plyer);
							InventoryListener.setViewedInvPlayerName(plyer, joinedPlayer.getName(), ViewedInvType.INVENTORY);

						}
					});
				}
		});
	}
}
