package Enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Items.Items;
import Main.Main;

public enum Permission {

	ALL(ChatColor.GREEN),
	OP(ChatColor.RED),
	NON(ChatColor.GRAY),
	CUSTOM(ChatColor.GOLD);

	ChatColor color;

	private Permission(ChatColor color) {
		this.color = color;
	}

	public ChatColor getColor() {
		return color;
	}

	public ItemStack getItem() {
		switch (this) {
		case ALL:
			return Items.perALL;
		case OP:
			return Items.perOP;
		case NON:
			return Items.perNON;
		case CUSTOM:
			return Items.perCUSTOM;
		default:
			return new ItemStack(Material.STONE);
		}
	}

	public boolean hasPermission(Player player, Commands cmdName) {
		switch (this) {
		case ALL:
			return true;
		case OP:
			return player.isOp();
		case NON:
			return false;
		case CUSTOM:
			return Main.getInstance().getPermissionManager().getPermissionHandler(cmdName).getCustomPermission().contains(player);
		default:
			return false;
		}
	}

}
