package Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Enums.Commands;
import Enums.Permission;
import Utils.config;

public class PermissionHandler {
	private Commands command;
	private Permission activePermission;
	private List<Player> customPermission;
	
	public PermissionHandler(Commands command, Permission activePermission) {
		this.activePermission = activePermission;
		this.customPermission= new ArrayList<>();
		this.command = command;
		Bukkit.broadcastMessage(ChatColor.RED + "COMMAND" + this.command.name());
		load();
	}

	public Permission getPermission() {
		return activePermission;
	}

	public void setPermission(Permission activePermission) {
		this.activePermission = activePermission;
	}

	public List<Player> getCustomPermission() {
		return customPermission;
	}

	public void setCustomPermission(List<Player> customPermission) {
		this.customPermission = customPermission;
	}
	
	public ItemStack getItem() {
		return this.activePermission.getItem();
	}
	
	public ChatColor getColor() {
		return this.activePermission.getColor();
	}
	
	public void switchCustomPermission(Player player) {
		if(this.customPermission.contains(player))
			this.customPermission.remove(player);
		else 
			this.customPermission.add(player);
	}
	
	public void save() {
		config.put("permissions." + this.command.name(), this.activePermission.name(), Permission.OP);
		config.put("permissions.custom." + this.command.name(), this.customPermission, new ArrayList<>());
	}
	
	@SuppressWarnings("unchecked")
	private void load() {
		this.activePermission = Permission.valueOf((String) config.get("permissions." + this.command.name(), Permission.OP));
		this.customPermission = (List<Player>) config.get("permissions.custom." + this.command.name(), new ArrayList<>());
	}
	
	public void reset() {
		this.activePermission = Permission.OP;
		this.customPermission = new ArrayList<>();
	}
}
