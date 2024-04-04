package Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import Enums.Commands;
import Items.Items;
import Listener.GUI.CustomPermissonListener;
import Main.Main;

public class OptionManager {

	public static List<Inventory> optionMenus;
	public static Map<Commands, List<Inventory>> customPerMenus;

	public OptionManager() {
		optionMenus = new ArrayList<>();
		customPerMenus = new HashMap<>();
		for(Commands cmd : Commands.values())
			customPerMenus.put(cmd, new ArrayList<>());
	}

	public static void openOptions(Player player) {
		Items.updateGui();
		Inventory inventory = Bukkit.createInventory(player, 18, "Options");
		inventory.setContents(Items.optionsGui);

		player.openInventory(inventory);
		optionMenus.add(inventory);
	}

	public static void updateOptions() {
		Items.updateGui();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!player.isOp())
				continue;
			if(optionMenus.contains(player.getOpenInventory().getTopInventory())) {
				openOptions(player);
			}
		}
	}

	public static void removeOptionsFromList(Inventory inventory) {
		optionMenus.remove(inventory);
	}

	/**Custom Permissions
	 * @param player
	 * @param cmd
	 */
	public static void openCustomPermissionGui(Player player, Commands cmd) {
		try {
			Bukkit.broadcastMessage(ChatColor.BOLD + "OPEN");
			Bukkit.broadcastMessage(ChatColor.BOLD + cmd.name());
			Inventory inventory = Bukkit.createInventory(player, 45, ChatColor.BOLD + "/" + cmd.name() + ChatColor.RESET + " | custom permissions");
			ArrayList<ItemStack> list = new ArrayList<>();
			int count = 0;
			for(int i = 0; i < 45; i++)
				list.add(new ItemStack(Material.AIR));
			for(String playername : KnownPlayerManager.getKnownPlayers()) {
				list.set(count, getPlayerHead(playername));
				list.set(count + 9, getCustomPermissionItem(Bukkit.getPlayerExact(playername), cmd));
				count++;
			}
			Items.setCustomPerItems(list);
			ItemStack[] array = InventoryManager.getItemStackArray(list);
			
			inventory.setContents(array);
			Bukkit.broadcastMessage("Size before: " + getCustomPerMenus(cmd).size());
			
			addCustomPerMenu(cmd, inventory);
			Bukkit.broadcastMessage("Size after: " + getCustomPerMenus(cmd).size());
			CustomPermissonListener.setViewedCmd(player, cmd);
			player.openInventory(inventory);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateCustomPermissionGui(Commands cmd) {
		Bukkit.broadcastMessage("Size 1: " + getCustomPerMenus(cmd).size());
		try {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if(!player.isOp()) continue;
				if(getCustomPerMenus(cmd).contains(player.getOpenInventory().getTopInventory()))
					openCustomPermissionGui(player, cmd);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bukkit.broadcastMessage("Size 2: " + getCustomPerMenus(cmd).size());
	}
	
	public static List<Inventory> getAllCustomPerMenus() {
		List<Inventory> list = new ArrayList<>();
		for(Commands cmd : customPerMenus.keySet()) {
			for(Inventory inv : customPerMenus.get(cmd)) {
				list.add(inv);
			}
		}
		return list;
	}
	
	public static List<Inventory> getCustomPerMenus(Commands cmd) {
		Bukkit.broadcastMessage("no menus: " + (customPerMenus.get(cmd) == null));
		return customPerMenus.get(cmd) == null ? new ArrayList<>() : customPerMenus.get(cmd);
	}
	
	private static void addCustomPerMenu(Commands cmd, Inventory inventory) {
		if(OptionManager.customPerMenus.get(cmd) == null) {
			Bukkit.broadcastMessage("array is null");
			ArrayList<Inventory> menus;
			menus = new ArrayList<>();
			menus.add(inventory);
			customPerMenus.put(cmd, menus);
			return;
		}
		getCustomPerMenus(cmd).add(inventory);
	}
	
	public static void removeCustomPerFromList(Inventory inventory, Commands cmd) {
		getCustomPerMenus(cmd).remove(inventory);
	}

	private static ItemStack getCustomPermissionItem(Player player, Commands cmd) {
		return Main.getInstance().getPermissionManager().getPermissionHandler(cmd).getCustomPermission().contains(player) ? Items.cPerTrue : Items.cPerFalse;
	}

	public static ItemStack getPlayerHead(String playername) {
		Player player = Bukkit.getPlayerExact(playername);
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();

		meta.setOwningPlayer(player);
		meta.setDisplayName(playername);
		if(player.isOp()) {
			/*LORE*/ArrayList<String> lore = new ArrayList<>();
			/*LORE*/lore.add("operator");
			/*LORE*/meta.setLore(lore);
		}

		item.setItemMeta(meta);

		return item;
	}
}