package Listener.GUI;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Enums.Commands;
import Enums.Permission;
import Items.Items;
import Main.Main;
import Manager.OptionManager;
import Manager.PermissionManager;

public class OptionListener implements Listener {
	String prefix = Main.prefix;

	static PermissionManager permissionManager = Main.getInstance().getPermissionManager();
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) throws IllegalStateException, IOException {
		Inventory inventory = event.getClickedInventory();
		if(!OptionManager.optionMenus.contains(inventory))
			return;
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		ItemStack clickedItem = (event.getCurrentItem() == null) ? new ItemStack(Material.AIR) : event.getCurrentItem();
		int clickedSlot = event.getSlot();
		ClickType click = event.getClick();
		
		
		if(!player.isOp()) {
			player.sendMessage(prefix + ChatColor.RED + "Only operators can interact with this gui!");
			player.closeInventory();
			return;
		}

		if (clickedSlot >= 9 ? inventory.getItem(clickedSlot - 9).equals(Items.cmdEcOwn) : clickedItem.equals(Items.cmdEcOwn)) {
			if(click == ClickType.RIGHT && clickedItem.equals(Items.perCUSTOM) && permissionManager.getPermissionHandler(Commands.EC_OWN).getPermission() == Permission.CUSTOM) {
				OptionManager.openCustomPermissionGui(player, Commands.EC_OWN);
			} else {
				permissionManager.nextPermisions(Commands.EC_OWN);
				OptionManager.updateOptions();
			}
		} else if (clickedSlot >= 9 ? inventory.getItem(clickedSlot - 9).equals(Items.cmdEcAll) : clickedItem.equals(Items.cmdEcAll)) {
			if(click == ClickType.RIGHT && clickedItem.equals(Items.perCUSTOM) && permissionManager.getPermissionHandler(Commands.EC_ALL).getPermission() == Permission.CUSTOM) {
				OptionManager.openCustomPermissionGui(player, Commands.EC_ALL);
			} else {
				permissionManager.nextPermisions(Commands.EC_ALL);
				OptionManager.updateOptions();
			}
		} else if (clickedSlot >= 9 ? inventory.getItem(clickedSlot - 9).equals(Items.cmdInvsee) : clickedItem.equals(Items.cmdInvsee)) {
			if(click == ClickType.RIGHT && clickedItem.equals(Items.perCUSTOM) && permissionManager.getPermissionHandler(Commands.INVSEE).getPermission() == Permission.CUSTOM) {
				OptionManager.openCustomPermissionGui(player, Commands.INVSEE);
			} else {
				permissionManager.nextPermisions(Commands.INVSEE);
				OptionManager.updateOptions();
			}
		} else if (clickedSlot >= 9 ? inventory.getItem(clickedSlot - 9).equals(Items.cmdCraft) : clickedItem.equals(Items.cmdCraft)) {
			if(click == ClickType.RIGHT && clickedItem.equals(Items.perCUSTOM) && permissionManager.getPermissionHandler(Commands.CRAFT).getPermission() == Permission.CUSTOM) {
				OptionManager.openCustomPermissionGui(player, Commands.CRAFT);
			} else {
				permissionManager.nextPermisions(Commands.CRAFT);
				OptionManager.updateOptions();
			}
		} else if (clickedSlot >= 9 ? inventory.getItem(clickedSlot - 9).equals(Items.cmdEditSign) : clickedItem.equals(Items.cmdEditSign)) {
			if(click == ClickType.RIGHT && clickedItem.equals(Items.perCUSTOM) && permissionManager.getPermissionHandler(Commands.EDITSIGN).getPermission() == Permission.CUSTOM) {
				OptionManager.openCustomPermissionGui(player, Commands.EDITSIGN);
			} else {
				permissionManager.nextPermisions(Commands.EDITSIGN);
				OptionManager.updateOptions();
			}
		} else if (clickedItem.equals(Items.perOP) || clickedSlot == 7) {
			permissionManager.setAllPermissions(Permission.OP);
			OptionManager.updateOptions();
		} else if (clickedItem.equals(Items.perALL) || clickedSlot == 8) {
			permissionManager.setAllPermissions(Permission.ALL);
			OptionManager.updateOptions();
		} else if (clickedItem.equals(Items.perCUSTOM) || clickedSlot == 7 + 9) {
			permissionManager.setAllPermissions(Permission.CUSTOM);
			OptionManager.updateOptions();
		} else if (clickedItem.equals(Items.perNON) || clickedSlot == 8 + 9) {
			permissionManager.setAllPermissions(Permission.NON);
			OptionManager.updateOptions();
		}
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent event) throws IllegalStateException, IOException {
		Inventory closedInv = event.getInventory();
		//remove all GUI Items from inventories
		for(Player player  : Bukkit.getOnlinePlayers()) {
			Inventory inventory = player.getInventory();
			for(ItemStack item : Items.optionsGui)
				while(inventory.contains(item)) {
					inventory.remove(item);
					player.sendMessage(prefix + "Removed item: " + (item.equals(Items.spaceholder) ? ChatColor.DARK_GRAY + "spaceholder" : item.getItemMeta().getDisplayName()) + ChatColor.GRAY + ".");
				}
		}
		if(OptionManager.optionMenus.contains(closedInv)) {
			OptionManager.removeOptionsFromList(closedInv);
			permissionManager.save();
		}
	}
}