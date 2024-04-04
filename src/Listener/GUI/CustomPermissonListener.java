package Listener.GUI;

import java.io.IOException;
import java.util.HashMap;
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

import Enums.Commands;
import Items.Items;
import Main.Main;
import Manager.OptionManager;
import Manager.PermissionManager;

public class CustomPermissonListener implements Listener {
	String prefix = Main.prefix;
	static Map<Player, Commands> viewedCmd;
	PermissionManager permissionManager = Main.getInstance().getPermissionManager();

	public CustomPermissonListener() {
		viewedCmd = new HashMap<>();
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) throws IllegalStateException, IOException {
		Inventory inventory = event.getClickedInventory();
		if(!OptionManager.getAllCustomPerMenus().contains(inventory)) return;
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		Player target;
		Commands cmd = getViewedCmd(player);
		if(cmd == null) {
			player.sendMessage(ChatColor.RED + "Viewed Command is null!!!");
			return;
		}
		ItemStack[] content = inventory.getContents();
		ItemStack clickedItem = (event.getCurrentItem() == null) ? new ItemStack(Material.AIR) : event.getCurrentItem();
		int clickedSlot = event.getSlot();

		if(!player.isOp()) {
			player.sendMessage(prefix + ChatColor.RED + "Only operators can interact with this gui!");
			player.closeInventory();
			return;
		}
		if(clickedItem.getType() == Material.AIR) return;
		
		if(clickedItem.equals(Items.cPerTrue) || clickedItem.equals(Items.cPerFalse)) {
			target = Bukkit.getPlayerExact(content[clickedSlot - 9].getItemMeta().getDisplayName());
			return;
		} else {
			target = Bukkit.getPlayerExact(clickedItem.getItemMeta().getDisplayName());
		}
		Bukkit.broadcastMessage(ChatColor.RED + "command" + cmd.name());
		permissionManager.switchCustomPermission(cmd, target);
		OptionManager.updateCustomPermissionGui(cmd);
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent event) throws IllegalStateException, IOException {
		Inventory closedInv = event.getInventory();
		Player eventPlayer = (Player) event.getPlayer();
		//remove all GUI Items from inventories
		for(Player player  : Bukkit.getOnlinePlayers()) {
			Inventory inventory = player.getInventory();
			for(ItemStack item : Items.getCustomPerItems())
				while(inventory.contains(item)) {
					inventory.remove(item);
					player.sendMessage(prefix + "Removed item: " + (item.equals(Items.spaceholder) ? ChatColor.DARK_GRAY + "spaceholder" : item.getItemMeta().getDisplayName()) + ChatColor.GRAY + ".");
				}
		}
		if(OptionManager.getAllCustomPerMenus().contains(closedInv)) {
			OptionManager.removeCustomPerFromList(closedInv, getViewedCmd(eventPlayer));
			resetViewedCmd(eventPlayer);
			permissionManager.save();
		}
	}
	
	public static void setViewedCmd(Player player, Commands cmd) {
		viewedCmd.put(player, cmd);
	}

	public static void resetViewedCmd(Player player) {
		viewedCmd.remove(player);
	}

	public static Commands getViewedCmd(Player player) {
		return viewedCmd.get(player);
	}
}