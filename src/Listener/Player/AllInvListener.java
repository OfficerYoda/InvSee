package Listener.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Enums.ViewedInvType;
import Listener.GUI.InventoryListener;
import Manager.InventoryManager;


public class AllInvListener implements Listener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) throws IllegalStateException, IOException {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getWhoClicked();
		int clickedSlot = event.getSlot();
		Inventory topInv = player.getOpenInventory().getTopInventory();
		if(topInv == null)
			return;
		//		Bukkit.broadcastMessage(topInv.toString());
		if(InventoryListener.getViewedInv(player) == null)
			return;
		if(InventoryListener.getViewedInv(player).equals(topInv)) {
			if (InventoryListener.getViewedInvType(player) == ViewedInvType.INVENTORY) {
				InventoryListener.saveInventory(removeItemStackFromList(inv.getContents(), clickedSlot), InventoryListener.getViewedInvPlayerName(player));
			} else if (InventoryListener.getViewedInvType(player) == ViewedInvType.ENDERCHEST) {
				InventoryListener.saveEnderchest(inv, player, InventoryListener.getViewedInvPlayerName(player));
			}
		}
	}

	public ItemStack[] removeItemStackFromList(ItemStack[] content, int slot) {
		List<ItemStack> content2 = new ArrayList<>();
		content2 = InventoryManager.getItemStackArrayList(content);
		content2.set(slot, new ItemStack(Material.AIR));
		return InventoryManager.getItemStackArray(content2);
	}
}
