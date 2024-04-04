package Listener.Player;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import Manager.EnderchestManager;
import Manager.InventoryManager;
import Manager.KnownPlayerManager;

public class QuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) throws IllegalArgumentException, IOException {
		Player player = event.getPlayer();
		
		InventoryManager.saveInv(player);
		EnderchestManager.saveEc(player);
		KnownPlayerManager.addPlayer(player.getName());
	}
}
