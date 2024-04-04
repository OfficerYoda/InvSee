package Manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Enums.ViewedInvType;
import Listener.GUI.InventoryListener;
import Utils.Base64;
import Utils.config;

public class EnderchestManager {
	public static List<Inventory> enderchests;

	static String emptyEc = "rO0ABXcEAAAAG3BwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==\r\n";

	public EnderchestManager() {
		enderchests = new ArrayList<>();
	}

	public static Inventory ecFromConfig(String path, Player openingForPlayer) throws IllegalArgumentException, IOException {
		String playername = path.replace("enderchests.", "");
		Inventory enderchest = Bukkit.createInventory(null, 27, playername + "'s enderchest");
		enderchest.setContents(loadEc(playername));
		enderchests.add(enderchest);
		InventoryListener.setViewedInv(openingForPlayer, enderchest, ViewedInvType.ENDERCHEST);
		return enderchest;
	}

	public static void saveEc(Player player) throws IllegalStateException, IOException {
		if(player == null)
			return;
		String path = "enderchests." + player.getName();
		config.put(path, Base64.itemStackArrayToBase64(player.getEnderChest().getContents()), emptyEc);
	}

	public static void saveEcItemStackArray(String playername, ItemStack[] content) throws IllegalStateException, IOException {
		if(content == null || playername == null)
			return;
		String path = "enderchests." + playername;
		config.put(path, Base64.itemStackArrayToBase64(content), emptyEc);
	}

	public static ItemStack[] loadEc(Player player) throws IOException {
		String path = "enderchests." + player.getName();
		if(!config.contains(path))
			return (Bukkit.getOnlinePlayers().contains(player)) ? player.getEnderChest().getContents() : null;
		return Base64.itemStackArrayFromBase64((String) config.get(path, emptyEc));
	}

	public static ItemStack[] loadEc(String playername) throws IOException {
		String path = "enderchests." + playername;
		if(!config.contains(path))
			return null;
		return Base64.itemStackArrayFromBase64((String) config.get(path, emptyEc));
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

	public static void removeEcFromList(Inventory enderchest) {
		enderchests.remove(enderchest);
	}
}