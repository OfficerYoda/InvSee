package Manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Enums.ViewedInvType;
import Items.Items;
import Listener.GUI.InventoryListener;
import Utils.Base64;
import Utils.config;

public class InventoryManager {
	public static List<Inventory> inventories;
	
	static String emptyInv = "rO0ABXcEAAAAKXBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw\r\\n";
	
	public InventoryManager() {
		inventories = new ArrayList<>();
	}
	
	public static Inventory guiFromPlayerInventory(Player target, Player player) {
		Inventory inventory = Bukkit.createInventory(target, 45, target.getName() + "'s inventory");
		inventory.setContents(target.getInventory().getContents());
		for(int i = 0; i < 4; i++) {
			inventory.setItem(41 + i, Items.spaceholder);
		}
		inventories.add(inventory);
		InventoryListener.setViewedInv(player, inventory, ViewedInvType.INVENTORY);
		InventoryListener.setEditedInv(player, false);
		return inventory;
	}
	
	public static Inventory guiFromConfig(String path, Player openingForPlayer) throws IllegalArgumentException, IOException {
		String playername = path.replace("inventories.", "");
		Inventory inventory = Bukkit.createInventory(Bukkit.getPlayer(playername), 45, playername + "'s inventory");
		inventory.setContents(loadInv(playername));
		for(int i = 0; i < 4; i++) {
			inventory.setItem(41 + i, Items.spaceholder);
		}
		inventories.add(inventory);
		InventoryListener.setViewedInv(openingForPlayer, inventory, ViewedInvType.INVENTORY);
		return inventory;
	}
	
	public static ItemStack[] guiToPlayerInventory(ItemStack[] content) {
		List<ItemStack> contentArrayList = getItemStackArrayList(content);
		ItemStack[] contentArray;
		for(int i = 0; i < 4; i++)
			contentArrayList.remove(contentArrayList.size() - 1);
		contentArray = getItemStackArray(contentArrayList);
		return contentArray;
	}
	
	public static void saveInv(Player player) throws IllegalStateException, IOException {
		String path = "inventories." + player.getName();
		config.put(path, Base64.itemStackArrayToBase64(player.getInventory().getContents()), emptyInv);
	}

	public static void saveInvItemStackArray(String playername, ItemStack[] content) throws IllegalStateException, IOException {
		if(content == null)
			return;
		String path = "inventories." + playername;
		config.put(path, Base64.itemStackArrayToBase64(content), emptyInv);
	}
	
	public static ItemStack[] loadInv(String playername) throws IOException {
		String path = "inventories." + playername;
		if(!config.contains(path))
			return null;
		return Base64.itemStackArrayFromBase64((String) config.get(path, emptyInv));
	}
	
	public static ItemStack[] getItemStackArray(List<ItemStack> list) {
		return list.toArray(new ItemStack[] {});
	}

	public static List<ItemStack> getItemStackArrayList(ItemStack[] array) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack item = null;
		for(int i = 0; i< array.length; i++) {
			item = array[i];
			list.add(item);
		}
		return list;
	}
	
	public static void removeInvFromList(Inventory inventory) {
		inventories.remove(inventory);
	}
	
	public static boolean openInvContainsPlayer(Player player) {
		
		return false;
	}
}