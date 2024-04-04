package Listener.GUI;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Enums.ViewedInvType;
import Items.Items;
import Main.Main;
import Manager.EnderchestManager;
import Manager.InventoryManager;

public class InventoryListener implements Listener {
	String prefix = Main.prefix;
	public static Map<Player, String> viewedInvPlayerName;
	public static Map<Player, Inventory> viewedInv;
	private static Map<Player, Boolean> editedInv;
	static Map<Player, ViewedInvType> viewedInvType;
	/*
	 * smoove Inv edit
	 * 1. man speicher bei jedem click das geklickte item(wenn kein custom item)
	 * 2. bei
	 * */
	public InventoryListener() {
		viewedInvPlayerName = new HashMap<>();
		viewedInv = new HashMap<>();
		editedInv = new HashMap<>();
		viewedInvType = new HashMap<>();
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) throws IllegalStateException, IOException {
		Inventory inventory = event.getClickedInventory();
//		event.setCursor(new ItemStack(Material.BAKED_POTATO));
		if(!InventoryManager.inventories.contains(inventory)) return;
		event.setCurrentItem(new ItemStack(Material.BREAD));
		
		customInvEdit(event);
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent event) throws IllegalStateException, IOException {
		Inventory closedInv = event.getInventory();
		Player player = (Player) event.getPlayer();
		String targetName = viewedInvPlayerName.get(player);
		//remove all GUI Items from inventories
		for(Player plyer  : Bukkit.getOnlinePlayers()) {
			Inventory inventory = plyer.getInventory();
			for(ItemStack invItem : Items.allItems)
				for(ItemStack item : inventory.getContents()) {
					if(item == null)
						continue;
					if(item.getItemMeta().equals(invItem.getItemMeta())) {
						inventory.remove(item);
						plyer.sendMessage(prefix + "removed item: " + (invItem.equals(Items.spaceholder) ? ChatColor.DARK_GRAY + "spaceholder" : invItem.getItemMeta().getDisplayName()) + ChatColor.GRAY + ".");
					}
				}
		}
		if(InventoryManager.inventories.contains(closedInv)) {
			if(hasEditedInv(player))
				saveInventory(closedInv.getContents(), targetName);
			InventoryManager.removeInvFromList(closedInv);
			resetViewedInv(player);
			resetViewedInvPlayerName(player);
			setEditedInv(player, false);
		}
		if(EnderchestManager.enderchests.contains(closedInv)) {
			if(hasEditedInv(player))
				saveEnderchestToConfig(targetName, closedInv);
			EnderchestManager.removeEcFromList(closedInv);
			setEditedInv(player, false);
		}
	}

	private void customInvEdit(InventoryClickEvent event) throws IllegalStateException, IOException {
		Inventory inventory = event.getClickedInventory();
		Player player = (Player) event.getWhoClicked();
		Player target = (Player) inventory.getHolder();
		String targetName = viewedInvPlayerName.get(player);
		ItemStack clickedItem = (event.getCurrentItem() == null) ? new ItemStack(Material.AIR) : event.getCurrentItem();

		if(!player.isOp()) {
			event.setCancelled(true);
			player.sendMessage(prefix + ChatColor.RED + "Only operators can interact with this gui!");
			player.closeInventory();
			return;
		}

		if(clickedItem.equals(Items.spaceholder))
			event.setCancelled(true);

		setEditedInv(player, true);
		saveInventoryToConfig(target.getName(), replaceContent(inventory.getContents(), clickedItem, event.getSlot()));
		if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(targetName)))
			target.getInventory().setContents(InventoryManager.loadInv(targetName));
		player.sendMessage(prefix + ChatColor.GOLD + "Updated" + ChatColor.GRAY + " the inventory of "+ ChatColor.GREEN + target.getName() + ChatColor.GRAY + ".");
	}

	private ItemStack[] replaceContent(ItemStack[] content,ItemStack item ,int slot) {
		List<ItemStack> contentList = InventoryManager.getItemStackArrayList(content);
		for(int i = 0; i < contentList.size(); i++) {
			if(contentList.get(i) == null)
				contentList.set(i, new ItemStack(Material.AIR));
			//			Bukkit.broadcastMessage(prefix + "1: " + i + " " + contentList.get(i).getType());
		}
		contentList.set(slot, item);
		//		for(int i = 0; i < contentList.size(); i++)
		//			Bukkit.broadcastMessage(prefix + "2: " + i + contentList.get(i).getType());
		//		Bukkit.broadcastMessage(prefix + "Slot: " + slot + " " + item.getType());
		return InventoryManager.getItemStackArray(contentList);
	}

	public static void saveInventory(ItemStack[] content, String targetName) throws IllegalArgumentException, IOException {
		saveInventoryToConfig(targetName, content);
		//		if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(viewedInvPlayerName.get(player))))
		if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(targetName)))
			Bukkit.getPlayer(targetName).getInventory().setContents(InventoryManager.loadInv(targetName));
	}

	public static void saveEnderchest(Inventory closedInv, Player player, String targetName) throws IllegalArgumentException, IOException {
		saveEnderchestToConfig(targetName, closedInv);
		if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(viewedInvPlayerName.get(player))))
			Bukkit.getPlayer(targetName).getEnderChest().setContents(EnderchestManager.loadEc(targetName));
	}

	private static void saveInventoryToConfig(String playername, ItemStack[] content) throws IllegalStateException, IOException {
		InventoryManager.saveInvItemStackArray(playername, InventoryManager.guiToPlayerInventory(content));
	}

	private static void saveEnderchestToConfig(String playername, Inventory inventory) throws IllegalStateException, IOException {
		EnderchestManager.saveEcItemStackArray(playername, inventory.getContents());
	}

	public static void setViewedInvPlayerName(Player player, String playername, ViewedInvType type) {
		viewedInvPlayerName.put(player, playername);
		viewedInvType.put(player, type);
	}

	public static void resetViewedInvPlayerName(Player player) {
		viewedInvPlayerName.remove(player);
	}

	public static String getViewedInvPlayerName(Player player) {
		return viewedInvPlayerName.get(player);
	}

	public static void setViewedInv(Player player, Inventory inv, ViewedInvType type) {
		viewedInv.put(player, inv);
		viewedInvType.put(player, type);
	}

	public static void resetViewedInv(Player player) {
		viewedInv.remove(player);
	}

	public static Inventory getViewedInv(Player player) {
		return viewedInv.get(player);
	}

	public static ViewedInvType getViewedInvType(Player player) {
		return viewedInvType.get(player);
	}

	public static void setEditedInv(Player player, boolean value) {
		editedInv.put(player, value);
	}

	public static boolean hasEditedInv(Player player) {
		if(editedInv.containsKey(player))
			return editedInv.get(player);
		editedInv.put(player, false);
		return false;
	}
}